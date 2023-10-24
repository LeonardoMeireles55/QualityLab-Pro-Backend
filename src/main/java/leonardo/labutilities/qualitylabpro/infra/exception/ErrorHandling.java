package leonardo.labutilities.qualitylabpro.infra.exception;

import leonardo.labutilities.qualitylabpro.records.auth.ErrorOfValidation;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErrorHandling {
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<String> error201() {
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorOfValidation>> error400(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ErrorOfValidation::new).toList());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> Error400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> ErrorBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> ErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auth failed");
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> AcessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acess denied");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> error500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " +ex.getLocalizedMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String ErrorMessage = "An error occurred while processing your request. The provided value already exists in the database.\n";

        return new ResponseEntity<>(ErrorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        String errorMessage = "An error occurred while authenticating the user. Please try again later.";

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private record ErrorValidation(String campo, String message) {
        public ErrorValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
    public static class NoContentException extends RuntimeException {
        public NoContentException(String message) {
            super(message);
        }
    }
}
