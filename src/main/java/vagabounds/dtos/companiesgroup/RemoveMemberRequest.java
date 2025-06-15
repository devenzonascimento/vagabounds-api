package vagabounds.dtos.companiesgroup;

import jakarta.validation.constraints.NotNull;

public record RemoveMemberRequest(
    @NotNull Long groupId,
    @NotNull Long memberId
) {
}
