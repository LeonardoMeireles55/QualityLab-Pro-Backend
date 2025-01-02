package leonardo.labutilities.qualitylabpro.configs.exception;


import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomGlobalErrorHandling {

    // Custom Exception Classes
    public static class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class NoContentException extends RuntimeException {}

    public static class DataIntegrityViolationException extends RuntimeException {}

    public static class PasswordNotMatchesException extends RuntimeException {}

    // Validation Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.error("BAD_REQUEST: Validation failed for fields - {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
        ConstraintViolationException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex
            .getConstraintViolations()
            .forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
            );
        log.error("BAD_REQUEST: Constraint violations encountered - {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    // Specific Business Logic Exceptions
    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> handleNoContentException() {
        log.error("NO_CONTENT: No content available.");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("NOT_FOUND: Resource not found - {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDataIntegrityViolation() {
        log.error("CONFLICT: Data integrity violation.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            "Data integrity violation - the value already exists."
        );
    }

    @ExceptionHandler(PasswordNotMatchesException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handlePasswordNotMatches() {
        log.error("UNAUTHORIZED: Password does not match.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            "Passwords do not match or are invalid."
        );
    }

    // Authentication and Authorization Exceptions
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleBadCredentials() {
        log.error("UNAUTHORIZED: Invalid credentials.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthenticationException() {
        log.error("UNAUTHORIZED: Authentication failed.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDenied() {
        log.error("FORBIDDEN: Access denied.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleInternalAuthenticationServiceException() {
        log.error("INTERNAL_SERVER_ERROR: Authentication service failure.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            "Authentication service error. Please try again later."
        );
    }

    // Other Exceptions
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePropertyReferenceException(PropertyReferenceException ex) {
        log.error("BAD_REQUEST: Invalid property - {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Invalid property reference: " + ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex
    ) {
        log.error("BAD_REQUEST: Malformed request body.");
        return ResponseEntity.badRequest().body("Request body is invalid or malformed.");
    }

    // Generic Exception Handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("INTERNAL_SERVER_ERROR: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            "An unexpected error occurred."
        );
    }
}
