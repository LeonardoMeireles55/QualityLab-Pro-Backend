package leonardo.labutilities.qualitylabpro.dtos.users;

public record UpdatePasswordRecord(String username, String email, String oldPassword, String newPassword) {
}
