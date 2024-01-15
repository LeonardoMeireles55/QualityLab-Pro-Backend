package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.component.BCryptEncoderComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.User;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepositoryCustom userRepositoryCustom;

    public User signUp(String login, String password, String email, UserRoles userRoles) {
        var user = new User(login, BCryptEncoderComponent.encrypt(password), email, userRoles);
        if(userRepositoryCustom.existsByUsername(login) || userRepositoryCustom.existsByEmail(email)) {
            throw new ErrorHandling.DataIntegrityViolationException();}
        return userRepositoryCustom.save(user);
    }

    public void updUser(String name, String email, String password, String newPassword, UserRoles userRoles) {
        var oldPass = userRepositoryCustom.getReferenceByUsernameAndEmail(name, email);

        if(!BCryptEncoderComponent.decrypt(password, oldPass.getPassword()) ||
                BCryptEncoderComponent.decrypt(newPassword, oldPass.getPassword())) {
            log.error("PasswordNotMatches. {}, {}", name, email);
            throw new ErrorHandling.PasswordNotMatchesException();
        } else {
            userRepositoryCustom.setPasswordWhereByUsername(oldPass.getUsername(),
                    BCryptEncoderComponent.encrypt(newPassword));
        }
    }
}