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

@Service
public class CompaniesGroupService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompaniesGroupRepository groupRepository;

    @Autowired
    GroupMembershipRepository membershipRepository;

    public void createGroup(Long accountId, String groupName) {
        Company company = companyRepository.findByAccountId(accountId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

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
}
