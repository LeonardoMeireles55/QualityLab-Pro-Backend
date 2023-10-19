package leonardo.labutilities.qualitylabpro.records.auth;

import org.springframework.validation.FieldError;

public record ErrorOfValidation(String Field, String Message) {
    public ErrorOfValidation(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}