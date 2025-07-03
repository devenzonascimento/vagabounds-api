package vagabounds.dtos.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApproveCandidateRequest(
    @NotNull
    Long applicationId,

    @NotBlank
    String decisionReason
) {
}
