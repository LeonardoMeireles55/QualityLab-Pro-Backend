package leonardo.labutilities.qualitylabpro.dtos.email;

import jakarta.validation.constraints.Email;

public record RecoveryEmailRecord(@Email String email, String temporaryPassword) {
}
