package leonardo.labutilities.qualitylabpro.analytics.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum UserRoles {
    USER("user"),
    ADMIN("admin");
    private final String role;
    UserRoles(String role) {
        this.role = role;
    }
}
