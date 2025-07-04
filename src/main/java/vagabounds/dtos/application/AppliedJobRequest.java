package vagabounds.dtos.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppliedJobRequest(
    @NotNull
    Long jobId,
    @NotBlank
    String title,
    @NotNull
    Long candidateId
) { }
