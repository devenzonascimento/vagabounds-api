package vagabounds.dtos.company;

import vagabounds.models.Company;

public record CompanySummary(
    Long id,
    String name,
    String email,
    String cnpj,
    String address
) {
    public static CompanySummary fromCompany(Company company) {
        return new CompanySummary(
            company.getId(),
            company.getName(),
            company.getAccount().getEmail(),
            company.getCnpj(),
            company.getAddress()
        );
    }
}
