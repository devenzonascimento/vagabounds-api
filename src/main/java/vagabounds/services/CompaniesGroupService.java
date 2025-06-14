package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
            .orElseThrow(() -> new RuntimeException("Company not found"));
    }
}
