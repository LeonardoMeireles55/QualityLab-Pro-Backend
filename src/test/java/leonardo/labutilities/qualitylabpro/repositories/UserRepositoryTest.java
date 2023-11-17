package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.main.User;
import leonardo.labutilities.qualitylabpro.main.enums.UserRoles;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    private User signUp() {
        var user = new User("UserTest", encrypt("12345"), "safdasdas", UserRoles.USER);

        return userRepository.save(user);
    }
    @Test
    @DisplayName("return 200 when user is exists")
    void findByLoginUserDataBaseIsUserExists() {
        signUp();
        var userNotNull = userRepository.findByLogin("UserTest");
        assertThat(userNotNull).isNotNull();
    }
    @Test
    @DisplayName("return null when user is empty")
    void findByLoginUserDataBaseIsUserNotExists() {
        var userEmpty = userRepository.findByLogin("");
        assertThat(userEmpty).isNull();
    }
}