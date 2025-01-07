package leonardo.labutilities.qualitylabpro.dtos.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginUserRecord(@NotNull @Email String email, @NotNull String password) {
}
