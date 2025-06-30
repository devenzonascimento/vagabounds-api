package vagabounds.validators.CandidateEducationValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vagabounds.dtos.auth.RegisterCandidateRequest;
import vagabounds.models.Candidate;

public class ValidCandidateEducationValidator implements ConstraintValidator<ValidCandidateEducation, RegisterCandidateRequest> {

    @Override
    public boolean isValid(RegisterCandidateRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        var errorMessage = Candidate.validateEducation(
            request.education(),
            request.course(),
            request.semester(),
            request.graduationYear()
        );

        if (!errorMessage.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }

        return true;
    }
}
