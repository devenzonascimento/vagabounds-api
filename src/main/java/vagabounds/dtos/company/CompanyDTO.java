package vagabounds.dtos.company;

import vagabounds.enums.GroupPermission;
import vagabounds.models.Company;

import java.util.List;

public record CompanyDTO(
        Long id,
        String name,
        String email,
        String cnpj,
        String address,
        Long groupId,
        String groupName,
        GroupPermission permission
) {
    public static CompanyDTO fromCompany(Company company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getAccount().getEmail(),
                company.getCnpj(),
                company.getAddress(),
                company.getGroup() != null ? company.getGroup().getId() : null,
                company.getGroup() != null ? company.getGroup().getName() : null,
                company.getPermission()
        );
    }

    public static List<CompanyDTO> fromCompanies(List<Company> companies) {
        return companies.stream()
                .map(CompanyDTO::fromCompany)
                .toList();
    }
}