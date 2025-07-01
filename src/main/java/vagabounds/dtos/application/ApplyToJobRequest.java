package vagabounds.dtos.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ApplyToJobRequest(
    @NotNull
    Long jobId,

    @NotBlank
    String candidatePresentation,

    @NotNull
    List<String> candidateSkills
) {
}
