package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.main.User;
import leonardo.labutilities.qualitylabpro.main.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User signUp(String login, String password, String email, UserRoles userRoles) {
        var user = new User(login, encrypt(password), email, userRoles);

        return userRepository.save(user);
    }

    private static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}