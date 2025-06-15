package vagabounds.dtos.companiesgroup;

import jakarta.validation.constraints.NotNull;

public record AddMemberToGroupRequest(
    @NotNull Long groupId,
    @NotNull Long newMemberId
) {
}
