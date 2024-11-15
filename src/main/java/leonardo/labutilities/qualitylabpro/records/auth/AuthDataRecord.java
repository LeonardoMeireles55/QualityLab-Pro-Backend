package leonardo.labutilities.qualitylabpro.records.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AuthDataRecord
        (@NotNull String username,
         @NotNull
         @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>\\/?])(?=.*\\d{4,}).+$",
                 message = "Password must contain at least 4 characters and one special character.")
         String password) {
}