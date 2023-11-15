package leonardo.labutilities.qualitylabpro.records.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.main.enums.UserRoles;

public record AuthDataDTO
        (@NotNull String login,
         @NotNull
         @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>\\/?])(?=.*\\d{4,}).+$",
                 message = "Password must contain at least 4 characters and one special character.")
         String password, String email, UserRoles roles) {
}
