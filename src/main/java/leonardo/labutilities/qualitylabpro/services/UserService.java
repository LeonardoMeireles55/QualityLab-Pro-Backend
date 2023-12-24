package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.User;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User signUp(String login, String password, String email, UserRoles userRoles) {
        var user = new User(login, encrypt(password), email, userRoles);

        return userRepository.save(user);
    }

    public void updUser(String name, String email, String password, String newPassword, UserRoles userRoles) {
        var oldPass = userRepository.getReferenceByUsernameAndEmail(name, email);

        if(!decrypt(password, oldPass.getPassword()) || decrypt(newPassword, oldPass.getPassword())) {
            log.error("PasswordNotMatches. {}, {}", name, email);
            throw new ErrorHandling.PasswordNotMatchesException();
        } else {
            userRepository.setPasswordWhereByUsername(oldPass.getUsername(), encrypt(newPassword));
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