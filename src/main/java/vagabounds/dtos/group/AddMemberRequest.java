package vagabounds.dtos.group;

import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(
    @NotNull Long groupId,
    @NotNull Long newMemberId
) {
}
