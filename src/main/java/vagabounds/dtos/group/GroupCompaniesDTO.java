package vagabounds.dtos.group;

import vagabounds.dtos.company.CompanyDTO;
import vagabounds.models.Group;

import java.util.ArrayList;
import java.util.List;

public record GroupCompaniesDTO(
    Long id,
    String name,
    List<CompanyDTO> admins,
    List<CompanyDTO> members
) {
    public static GroupCompaniesDTO fromGroup(Group group) {
        if (group == null) {
            return null;
        }

        List<CompanyDTO> admins = new ArrayList<>();
        List<CompanyDTO> members = new ArrayList<>();

        group.getMemberships().forEach(groupMembership -> {
            if (groupMembership.getIsAdmin()) {
                admins.add(CompanyDTO.fromCompany(groupMembership.getCompany()));
            } else {
                members.add(CompanyDTO.fromCompany(groupMembership.getCompany()));
            }
        });

        return new GroupCompaniesDTO(
            group.getId(),
            group.getName(),
            admins,
            members
        );
    }
}

