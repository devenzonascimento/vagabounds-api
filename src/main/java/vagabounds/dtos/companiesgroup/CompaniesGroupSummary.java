package vagabounds.dtos.companiesgroup;

import vagabounds.dtos.company.CompanySummary;
import vagabounds.models.CompaniesGroup;

import java.util.ArrayList;
import java.util.List;

public record CompaniesGroupSummary(
    Long id,
    String name,
    List<CompanySummary> admins,
    List<CompanySummary> members
) {
    public static CompaniesGroupSummary fromCompaniesGroup(CompaniesGroup group) {
        if (group == null) {
            return null;
        }

        List<CompanySummary> admins = new ArrayList<>();
        List<CompanySummary> members = new ArrayList<>();

        group.getMemberships().forEach(groupMembership -> {
            if (groupMembership.getIsAdmin()) {
                admins.add(CompanySummary.fromCompany(groupMembership.getCompany()));
            } else {
                members.add(CompanySummary.fromCompany(groupMembership.getCompany()));
            }
        });

        return new CompaniesGroupSummary(
            group.getId(),
            group.getName(),
            admins,
            members
        );
    }
}

