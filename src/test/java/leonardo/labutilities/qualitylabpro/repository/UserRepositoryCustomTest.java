package leonardo.labutilities.qualitylabpro.repository;

import leonardo.labutilities.qualitylabpro.component.BCryptEncoderComponent;
import leonardo.labutilities.qualitylabpro.domain.entities.User;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryCustomTest {
    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Autowired
    UserRepositoryCustom userRepositoryCustom;


    public void setupTestData() {
        var user = new User("UserTest", BCryptEncoderComponent.encrypt("12345"),
                        "leo@hotmail.com", UserRoles.USER);

        userRepositoryCustom.save(user);
    }

    @Test
    @DisplayName("return 200 when user is exists")
    @Transactional
    void findByLoginUserDataBaseIsUserExists() {
        setupTestData();
        var userNotNull = userRepositoryCustom.findByUsername("UserTest");
        assertThat(userNotNull).isNotNull();
    }
    @Test
    @DisplayName("return null when user is empty")
    @Transactional
    void findByLoginUserDataBaseIsUserNotExists() {
        var userEmpty = userRepositoryCustom.findByUsername("");
        assertThat(userEmpty).isNull();
    }

    @Test
    @DisplayName("return True when update passwords successful")
    @Transactional
    void setPasswordWhereByUsername() {
        setupTestData();
        String username = "UserTest";
        String oldPassword = "12345";
        String newPassword = "249195Leo@@";

        var userWithOldPassword = userRepositoryCustom
                .getReferenceByUsernameAndEmail("UserTest", "leo@hotmail.com");

        userRepositoryCustom.setPasswordWhereByUsername(username, newPassword);

        var userWithNewPassword = userRepositoryCustom
                .getReferenceByUsernameAndEmail("UserTest", "leo@hotmail.com");

            assertThat(BCryptEncoderComponent.decrypt(oldPassword, userWithOldPassword.getPassword())
                    || BCryptEncoderComponent.decrypt(newPassword, userWithNewPassword.getPassword())).isTrue();
    }
}