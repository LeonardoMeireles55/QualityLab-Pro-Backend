package leonardo.labutilities.qualitylabpro.services.users;

import leonardo.labutilities.qualitylabpro.dtos.email.EmailRecord;
import leonardo.labutilities.qualitylabpro.dtos.email.RecoveryEmailRecord;
import leonardo.labutilities.qualitylabpro.entities.User;
import leonardo.labutilities.qualitylabpro.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.repositories.UserRepository;
import leonardo.labutilities.qualitylabpro.services.email.EmailService;
import leonardo.labutilities.qualitylabpro.utils.components.BCryptEncoderComponent;
import leonardo.labutilities.qualitylabpro.utils.components.PasswordRecoveryTokenManager;
import leonardo.labutilities.qualitylabpro.utils.exception.CustomGlobalErrorHandling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final PasswordRecoveryTokenManager passwordRecoveryTokenManager;
	private final EmailService emailService;

	private void sendRecoveryEmail(RecoveryEmailRecord recoveryEmailRecord) {
		String subject = "Password Recovery";
		String message = String.format(
				"Dear user,\n\nUse the following temporary password to recover your account: %s\n\nBest regards,\nYour Team",
				recoveryEmailRecord.temporaryPassword());
		log.info("Sending recovery email to: {}", recoveryEmailRecord.email());
		emailService.sendEmail(new EmailRecord(recoveryEmailRecord.email(), subject, message));
	}

	public void recoverPassword(String username, String email) {

		var user = userRepository.existsByUsernameAndEmail(username, email);

		if (!user) {
			throw new CustomGlobalErrorHandling.ResourceNotFoundException(
					"User not or invalid arguments");
		}

		String temporaryPassword = passwordRecoveryTokenManager.generateTemporaryPassword();
		passwordRecoveryTokenManager.generateAndStoreToken(email, temporaryPassword);

		sendRecoveryEmail(new RecoveryEmailRecord(email, temporaryPassword));
	}

	public void changePassword(String email, String temporaryPassword, String newPassword) {
		if (!passwordRecoveryTokenManager.isRecoveryTokenValid(temporaryPassword, email)) {
			throw new CustomGlobalErrorHandling.ResourceNotFoundException("Invalid recovery token");
		}
		userRepository.setPasswordWhereByUsername(email,
				BCryptEncoderComponent.encrypt(newPassword));
	};

	public User signUp(String login, String password, String email) {

		var user = new User(login, BCryptEncoderComponent.encrypt(password), email, UserRoles.USER);

		if (userRepository.existsByEmail(email)) {
			throw new CustomGlobalErrorHandling.DataIntegrityViolationException();
		}
		return userRepository.save(user);
	}

	public void updateUserPassword(String name, String email, String password, String newPassword) {
		var oldPass = userRepository.getReferenceByUsernameAndEmail(name, email);
		if (!BCryptEncoderComponent.decrypt(password, oldPass.getPassword())
				|| BCryptEncoderComponent.decrypt(newPassword, oldPass.getPassword())) {
			log.error("PasswordNotMatches. {}, {}", name, email);
			throw new CustomGlobalErrorHandling.PasswordNotMatchesException();
		} else {
			userRepository.setPasswordWhereByUsername(oldPass.getUsername(),
					BCryptEncoderComponent.encrypt(newPassword));
		}
	}
}
