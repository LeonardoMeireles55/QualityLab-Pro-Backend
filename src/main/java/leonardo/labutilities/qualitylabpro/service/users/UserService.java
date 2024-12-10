package leonardo.labutilities.qualitylabpro.service.users;

import leonardo.labutilities.qualitylabpro.components.BCryptEncoderComponent;
import leonardo.labutilities.qualitylabpro.entities.User;
import leonardo.labutilities.qualitylabpro.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.infra.config.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User signUp(String login, String password, String email, UserRoles userRoles) {
        var user = new User(login, BCryptEncoderComponent.encrypt(password), email, userRoles);
        if(userRepository.existsByUsername(login) || userRepository.existsByEmail(email)) {
            throw new CustomGlobalErrorHandling.DataIntegrityViolationException();}
        return userRepository.save(user);
    }

    public void updateUserPassword(String name, String email, String password, String newPassword, UserRoles userRoles) {
        var oldPass = userRepository.getReferenceByUsernameAndEmail(name, email);

        if(!BCryptEncoderComponent.decrypt(password, oldPass.getPassword()) ||
                BCryptEncoderComponent.decrypt(newPassword, oldPass.getPassword())) {
            log.error("PasswordNotMatches. {}, {}", name, email);
            throw new CustomGlobalErrorHandling.PasswordNotMatchesException();
        } else {
            userRepository.setPasswordWhereByUsername(oldPass.getUsername(),
                    BCryptEncoderComponent.encrypt(newPassword));
        }
    }
}