package leonardo.labutilities.qualitylabpro.records.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;

public record UserRecord
                (@NotNull String username,
                @NotNull
                @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>\\/?])(?=.*\\d{4,}).+$",
                        message = "Password must contain at least 4 characters and one special character.")
                String password, String email, UserRoles roles) {
}
