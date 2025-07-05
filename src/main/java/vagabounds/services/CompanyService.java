package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vagabounds.dtos.company.UpdateCompanyRequest;
import vagabounds.enums.GroupPermission;
import vagabounds.models.Company;
import vagabounds.repositories.CompanyRepository;
import vagabounds.security.SecurityUtils;


@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public void updateCompany(UpdateCompanyRequest request) {
        var company = getCurrentCompany();

        if (request.name() != null && !request.name().trim().isEmpty()) {
            company.setName(request.name().trim());
        }

        if (request.address() != null && !request.address().trim().isEmpty()) {
            company.setAddress(request.address().trim());
        }

        if (request.cnpj() != null && !request.cnpj().trim().isEmpty()) {
            var existingCompany = companyRepository.findByCnpj(request.cnpj());
            if (existingCompany.isPresent() && !existingCompany.get().getId().equals(company.getId())) {
                throw new RuntimeException("CNPJ already exists for another company.");
            }
            company.setCnpj(request.cnpj().trim());
        }

        companyRepository.save(company);
    }

    public Company findById(Long companyId) {
        var company = companyRepository.findById(companyId).orElse(null);

        if (company == null || company.getIsDeleted()) {
            throw new RuntimeException("Company not found.");
        }

        return company;
    }

    public Company getCurrentCompanyInfo() {
        return getCurrentCompany();
    }

    @Transactional
    public void deleteCompany() {
        var company = getCurrentCompany();

        var hasActiveJobs = company.getJobs().stream()
                .anyMatch(job -> !job.getIsDeleted() && job.getIsOpen());

        if (hasActiveJobs) {
            throw new RuntimeException("Cannot delete company with active jobs. Please close all jobs first.");
        }

        if (GroupPermission.OWNER.equals(company.getPermission()) &&
                company.getGroup() != null) {

            var otherMembers = company.getGroup().getCompanies()
                    .stream()
                    .filter(c -> !c.getId().equals(company.getId()) && !c.getIsDeleted())
                    .count();

            if (otherMembers > 0) {
                throw new RuntimeException("Cannot delete company. You are the group owner and there are other members.");
            }
        }

        company.setIsDeleted(true);

        companyRepository.save(company);
    }

    private Company getCurrentCompany() {
        var accountId = SecurityUtils.getAccountId();

        var company = companyRepository.findByAccountId(accountId).orElse(null);

        if (company == null || company.getIsDeleted()) {
            throw new RuntimeException("Company not found.");
        }

        return company;
    }
}