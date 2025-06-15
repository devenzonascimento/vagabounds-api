package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vagabounds.dtos.companiesgroup.AddMemberRequest;
import vagabounds.dtos.companiesgroup.RemoveMemberRequest;
import vagabounds.models.CompaniesGroup;
import vagabounds.models.Company;
import vagabounds.models.GroupMembership;
import vagabounds.models.GroupMembershipId;
import vagabounds.repositories.CompaniesGroupRepository;
import vagabounds.repositories.CompanyRepository;
import vagabounds.repositories.GroupMembershipRepository;
import vagabounds.security.SecurityUtils;
import vagabounds.utils.Utils;

@Service
public class CompaniesGroupService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompaniesGroupRepository groupRepository;

    @Autowired
    GroupMembershipRepository membershipRepository;

    public void createGroup(String groupName) {
        var company = getCurrentCompany();

        CompaniesGroup group = new CompaniesGroup();
        group.setName(groupName);
        group = groupRepository.save(group);

        GroupMembership membership = new GroupMembership(
            new GroupMembershipId(company.getId(), group.getId()),
            company,
            group,
            true
        );

        membershipRepository.save(membership);
    }

    public void addMember(AddMemberRequest request) {
        var company = getCurrentCompany();

        var group = groupRepository.findById(request.groupId()).orElse(null);

        if (group == null) {
            throw new RuntimeException("Group not found.");
        }

        var isAdminOnThisGroup = group.getMemberships()
            .stream()
            .anyMatch(m -> m.getIsAdmin() && m.getCompany().equals(company));

        if (!isAdminOnThisGroup) {
            throw new RuntimeException("You don't have admin access to add members on group.");
        }

        var alreadyGroupMember = group.getMemberships()
            .stream()
            .anyMatch(m -> m.getCompany().getId().equals(request.newMemberId()));

        if (alreadyGroupMember) {
            throw new RuntimeException("The new member informed is already part of the group.");
        }

        var memberToAdd = companyRepository.findById(request.newMemberId()).orElse(null);

        if (memberToAdd == null) {
            throw new RuntimeException("The informed company to add to group is not found.");
        }

        GroupMembership membership = new GroupMembership(
            new GroupMembershipId(memberToAdd.getId(), group.getId()),
            memberToAdd,
            group,
            false
        );

        membershipRepository.save(membership);
    }

    public void removeMember(RemoveMemberRequest request) {
        var company = getCurrentCompany();

        var group = groupRepository.findById(request.groupId()).orElse(null);

        if (group == null) {
            throw new RuntimeException("Group not found.");
        }

        var isAdminOnThisGroup = group.getMemberships()
            .stream()
            .anyMatch(m -> m.getIsAdmin() && m.getCompany().equals(company));

        if (!isAdminOnThisGroup) {
            throw new RuntimeException("You don't have admin access to remove members on group.");
        }

        var membership = Utils.find(
            group.getMemberships(),
            m -> m.getCompany().getId().equals(request.memberId())
        );

        if (membership == null) {
            throw new RuntimeException("The member informed is not part of the group.");
        }

        group.getMemberships().remove(membership);
        membershipRepository.deleteById(membership.getId());
    }

    public CompaniesGroup findById(Long groupId) {
        var company = getCurrentCompany();

        var groups = company.getMemberships()
            .stream()
            .map(GroupMembership::getGroup)
            .toList();

        return Utils.find(groups, g -> g.getId().equals(groupId));
    }

    private Company getCurrentCompany() {
        var accountId = SecurityUtils.getAccountId();

        return companyRepository.findByAccountId(accountId)
            .orElseThrow(() -> new RuntimeException("Company not found."));
    }
}
