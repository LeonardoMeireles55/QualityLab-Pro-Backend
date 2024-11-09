package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.components.BCryptEncoderComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entities.User;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;
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
            throw new ErrorHandling.DataIntegrityViolationException();}
        return userRepository.save(user);
    }

    public void updUser(String name, String email, String password, String newPassword, UserRoles userRoles) {
        var oldPass = userRepository.getReferenceByUsernameAndEmail(name, email);

        if(!BCryptEncoderComponent.decrypt(password, oldPass.getPassword()) ||
                BCryptEncoderComponent.decrypt(newPassword, oldPass.getPassword())) {
            log.error("PasswordNotMatches. {}, {}", name, email);
            throw new ErrorHandling.PasswordNotMatchesException();
        } else {
            userRepository.setPasswordWhereByUsername(oldPass.getUsername(),
                    BCryptEncoderComponent.encrypt(newPassword));
        }
    }
}