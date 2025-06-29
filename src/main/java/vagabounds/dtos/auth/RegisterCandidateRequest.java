package vagabounds.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vagabounds.enums.CandidateEducation;
import vagabounds.validators.CandidateEducationValidator.ValidCandidateEducation;

@ValidCandidateEducation
public record RegisterCandidateRequest(
    @Email
    @NotBlank
    String email,

    @Size(min = 8)
    @NotBlank
    String password,

    @NotBlank
    String name,

    @NotBlank
    String address,

    @NotNull
    CandidateEducation education,

    // Campos opcionais
    String course,
    Integer graduationYear,
    Integer semester,
    String resumeURL
) {
}
