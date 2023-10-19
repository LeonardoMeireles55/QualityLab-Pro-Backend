package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.analytics.User;
import leonardo.labutilities.qualitylabpro.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User signUp(String login, String password) {
        var user = new User(login, encrypt(password));

        return userRepository.save(user);
    }

    private static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}