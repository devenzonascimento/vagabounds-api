package vagabounds.validators.CandidateEducationValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidCandidateEducationValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidCandidateEducation {
    String message() default "Invalid data for the level of education provided.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
