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

    public void updUser(String name, String email, String password, String newPassword, UserRoles userRoles) {
        var updUser = userRepository.getReferenceByUsernameAndEmail(name, email);

        if(!decrypt(password, updUser.getPassword())) {
            throw  new RuntimeException("Passwords not matches");
        } else {
            var user = new User(updUser.getUsername(), encrypt(newPassword), email, UserRoles.USER);

            userRepository.setPasswordWhereByUsername(updUser.getUsername(), user.getPassword());
        }
    }

    private static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    private static Boolean decrypt(String pass, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(pass, password);
    }

}