package vagabounds.validators.CandidateEducationValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vagabounds.dtos.auth.RegisterCandidateRequest;
import vagabounds.enums.CandidateEducation;

public class ValidCandidateEducationValidator implements ConstraintValidator<ValidCandidateEducation, RegisterCandidateRequest> {

    @Override
    public boolean isValid(RegisterCandidateRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        var education = request.education();
        var course = request.course();
        var semester = request.semester();
        var graduationYear = request.graduationYear();

        if (education == CandidateEducation.NONE) {
            return true;
        }

        // UNDERGRAD precisa de (course, semester, graduationYear)
        if (education == CandidateEducation.UNDERGRAD) {
            boolean valid = (course != null && !course.isBlank())
                && graduationYear != null
                && semester != null;

            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "UNDERGRAD education requires course, semester and graduationYear"
                ).addConstraintViolation();
            }

            return valid;
        }

        // GRADUATED precisa de (course, graduationYear)
        if (education == CandidateEducation.GRADUATE) {
            boolean valid = (course != null && !course.isBlank())
                && graduationYear != null;

            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "GRADUATE education requires course and graduationYear"
                ).addConstraintViolation();
            }

            return valid;
        }

        return true;
    }
}
