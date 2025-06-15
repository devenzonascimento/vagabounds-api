package vagabounds.dtos.group;

import jakarta.validation.constraints.NotNull;

public record RemoveMemberRequest(
    @NotNull Long groupId,
    @NotNull Long memberId
) {
}
