
package vagabounds.dtos.group;

import vagabounds.enums.GroupPermission;
import vagabounds.models.Company;

import java.util.List;

public record GroupMemberDTO(
        Long id,
        String name,
        String email,
        String cnpj,
        GroupPermission permission
) {
    public static GroupMemberDTO fromCompany(Company company) {
        return new GroupMemberDTO(
                company.getId(),
                company.getName(),
                company.getAccount().getEmail(),
                company.getCnpj(),
                company.getPermission()
        );
    }

    public static List<GroupMemberDTO> fromCompanies(List<Company> companies) {
        return companies.stream()
                .map(GroupMemberDTO::fromCompany)
                .toList();
    }
}