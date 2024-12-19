package leonardo.labutilities.qualitylabpro.dto.authentication;

import jakarta.validation.constraints.NotNull;

public record AuthDataRecord(
    @NotNull String email,
    @NotNull
    String password
) {}
