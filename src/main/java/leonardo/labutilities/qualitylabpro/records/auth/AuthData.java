package leonardo.labutilities.qualitylabpro.records.auth;

import jakarta.validation.constraints.NotNull;

public record AuthData
        (@NotNull String login,
         @NotNull String password) {

}
