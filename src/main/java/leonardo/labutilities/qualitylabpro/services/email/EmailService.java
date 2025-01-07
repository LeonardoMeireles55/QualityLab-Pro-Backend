package leonardo.labutilities.qualitylabpro.services.email;

import leonardo.labutilities.qualitylabpro.dtos.email.EmailRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(EmailRecord email) {
        var message = new SimpleMailMessage();
        message.setFrom("noreply@newEmail.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        javaMailSender.send(message);
    }

}
