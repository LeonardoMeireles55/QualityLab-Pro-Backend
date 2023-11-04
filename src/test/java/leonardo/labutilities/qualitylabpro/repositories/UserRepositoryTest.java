package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.analytics.User;
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
    @Test
    @DisplayName("return null when user is empty")
    void findByLoginUserDataBaseIsEmpty() {
        var createUser = signUp();
        var userEmpty = userRepository.findByLogin("");
        assertThat(userEmpty).isNull();
    }
    private static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    private User signUp() {
        var user = new User("garotoTeste", encrypt("12345"));

        return userRepository.save(user);
    }
}