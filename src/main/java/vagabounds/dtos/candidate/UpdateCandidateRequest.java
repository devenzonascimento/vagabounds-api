package vagabounds.dtos.candidate;

import jakarta.validation.constraints.NotNull;
import vagabounds.enums.CandidateEducation;

public record UpdateCandidateRequest(
    @NotNull
    Long candidateId,
    String name,
    String address,
    CandidateEducation education,
    String course,
    Integer semester,
    Integer graduationYear,
    String resumeURL
) {
}
