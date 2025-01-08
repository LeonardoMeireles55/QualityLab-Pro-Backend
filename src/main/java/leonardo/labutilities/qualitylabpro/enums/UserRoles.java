package leonardo.labutilities.qualitylabpro.enums;

public enum UserRoles {
	USER("USER"), ADMIN("ADMIN");

	private final String role;

	UserRoles(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}
