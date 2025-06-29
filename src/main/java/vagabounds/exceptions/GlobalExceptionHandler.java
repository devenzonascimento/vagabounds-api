package vagabounds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
        MethodArgumentNotValidException exception
    ) {
        List<Map<String, String>> errors = new ArrayList<>();

        // Field errors (como @NotBlank, @Email, etc.)
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> errorMap = new HashMap<>();

            errorMap.put("field", error.getField());
            errorMap.put("message", error.getDefaultMessage());

            errors.add(errorMap);
        });

        // Global errors (como os levantados pelo ValidCandidateEducationValidator)
        exception.getBindingResult().getGlobalErrors().forEach(error -> {
            Map<String, String> errorMap = new HashMap<>();

            errorMap.put("message", error.getDefaultMessage());

            errors.add(errorMap);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeExceptions(
        RuntimeException exception
    ) {
        Map<String, String> errorMessage = new HashMap<>();

        errorMessage.put("errorMessage", exception.getMessage());

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Unauthorized.");
    }
}
