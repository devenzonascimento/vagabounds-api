package vagabounds.dtos.job;

import jakarta.validation.constraints.NotNull;

public record RejectCandidateRequest(
        @NotNull Long jobId,
        @NotNull Long candidateId
) {
}
