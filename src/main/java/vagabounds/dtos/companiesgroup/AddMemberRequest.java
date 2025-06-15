package vagabounds.dtos.companiesgroup;

import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(
    @NotNull Long groupId,
    @NotNull Long newMemberId
) {
}
