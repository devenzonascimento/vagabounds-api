package vagabounds.dtos.company;

import vagabounds.models.Company;

public record CompanyDTO(
    Long id,
    String name,
    String email,
    String cnpj,
    String address
) {
    public static CompanyDTO fromCompany(Company company) {
        return new CompanyDTO(
            company.getId(),
            company.getName(),
            company.getAccount().getEmail(),
            company.getCnpj(),
            company.getAddress()
        );
    }
}
