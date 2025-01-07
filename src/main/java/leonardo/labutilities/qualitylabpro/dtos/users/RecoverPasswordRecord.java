package leonardo.labutilities.qualitylabpro.dtos.users;

public record RecoverPasswordRecord(String email, String temporaryPassword, String newPassword) {
}
