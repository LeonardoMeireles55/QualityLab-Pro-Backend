package leonardo.labutilities.qualitylabpro.dtos.authentication;

import jakarta.validation.constraints.NotNull;

public record AuthDataRecord(@NotNull String email, @NotNull String password) {
}
