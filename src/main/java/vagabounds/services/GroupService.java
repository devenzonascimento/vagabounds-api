package vagabounds.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vagabounds.dtos.auth.RegisterCompanyRequest;
import vagabounds.dtos.group.ChangePermissionRequest;
import vagabounds.dtos.group.RemoveMemberRequest;
import vagabounds.enums.GroupPermission;
import vagabounds.models.Group;
import vagabounds.models.Company;
import vagabounds.repositories.CompanyRepository;
import vagabounds.security.SecurityUtils;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    AuthService authService;

    public void removeMember(RemoveMemberRequest request) {
        var company = getCurrentCompany();

        if (!GroupPermission.OWNER.equals(company.getPermission())) {
            throw new RuntimeException("Only group owners can remove members.");
        }

        var memberToRemove = companyRepository.findById(request.memberId()).orElse(null);

        if (memberToRemove == null || memberToRemove.getIsDeleted()) {
            throw new RuntimeException("Member not found.");
        }

        if (memberToRemove.getGroup() == null ||
                !memberToRemove.getGroup().getId().equals(company.getGroup().getId())) {
            throw new RuntimeException("Member is not part of your group.");
        }

        if (memberToRemove.getId().equals(company.getId())) {
            throw new RuntimeException("Cannot remove yourself from the group.");
        }

        memberToRemove.setIsDeleted(true);

        companyRepository.save(memberToRemove);
    }

    @Transactional
    public void changePermission(ChangePermissionRequest request) {
        var company = getCurrentCompany();

        if (!GroupPermission.OWNER.equals(company.getPermission())){
            throw new RuntimeException("Only group owners can change permissions.");
        }

        var targetCompany = companyRepository.findById(request.companyId()).orElse(null);

        if (targetCompany == null || targetCompany.getIsDeleted()){
            throw new RuntimeException("Company not found.");
        }

        if (targetCompany.getGroup() == null ||
            !targetCompany.getGroup().getId().equals(company.getGroup().getId())){
            throw new RuntimeException("Target company is not part of your group.");
        }

        if (targetCompany.getId().equals(company.getId()) &&
                GroupPermission.OWNER.equals(company.getPermission())) {
            throw new RuntimeException("Cannot change your own OWNER permission.");
        }

        if (GroupPermission.OWNER.equals(request.permission())) {
            var hasOtherOwner = company.getGroup().getCompanies()
                    .stream()
                    .anyMatch(c -> !c.getId().equals(targetCompany.getId()) &&
                            GroupPermission.OWNER.equals(c.getPermission()) &&
                            !c.getIsDeleted());

            if (hasOtherOwner) {
                throw new RuntimeException("There can only be one OWNER per group.");
            }
        }

        targetCompany.setPermission(request.permission());
        companyRepository.save(targetCompany);

    }

    public List<Company> findAllMembers() {
        var company = getCurrentCompany();

        if (company.getGroup() == null) {
            throw new RuntimeException("Company is not associated with any group.");
        }

        return company.getGroup().getCompanies()
                .stream()
                .filter(c -> !c.getIsDeleted())
                .toList();
    }

    public Group findById(Long groupId) {
        var company = getCurrentCompany();

        if (company.getGroup() == null || !company.getGroup().getId().equals(groupId)) {
            throw new RuntimeException("You don't have access to this group.");
        }

        return company.getGroup();
    } // verificar se vamos utilizar

    private Company getCurrentCompany() {
        var accountId = SecurityUtils.getAccountId();

        return companyRepository.findByAccountId(accountId)
            .orElseThrow(() -> new RuntimeException("Company not found."));
    }

    @Transactional
    public void registerCompany(RegisterCompanyRequest request) {
        var company = getCurrentCompany();

        if (!GroupPermission.OWNER.equals(company.getPermission())) {
            throw new RuntimeException("Only group owners can register companies.");
        }

        RegisterCompanyRequest requestWithGroupId = new RegisterCompanyRequest(
                request.email(),
                request.password(),
                request.name(),
                request.cnpj(),
                request.address(),
                company.getGroup().getId() );

        authService.registerCompany(requestWithGroupId);
    }
}
