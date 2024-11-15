package training.ecommerce.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import training.ecommerce.exception.EmailFailureExpectation;
import training.ecommerce.model.VerificationToken;

@Service
public class EmailService {

    @Value("${email.from}")
    private String from;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a verification email with the provided token.
     *
     * @param verificationToken the token containing user email and verification details
     * @throws EmailFailureExpectation if sending the email fails
     */
    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureExpectation {
        SimpleMailMessage message = buildVerificationMessage(verificationToken);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailFailureExpectation("Failed to send verification email to " + verificationToken.getUser().getEmail(), e);
        }
    }

    /**
     * Creates and configures a verification email message.
     *
     * @param verificationToken the token containing user email and verification details
     * @return a SimpleMailMessage configured with the necessary details
     */
    private SimpleMailMessage buildVerificationMessage(VerificationToken verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("Verify Your Account");
        message.setText(buildVerificationEmailBody(verificationToken));
        return message;
    }

    /**
     * Builds the body of the verification email.
     *
     * @param verificationToken the token containing verification details
     * @return the email body as a String
     */
    private String buildVerificationEmailBody(VerificationToken verificationToken) {
        return String.format(
            "To verify your account, please click the following link:\n\n%s/verify?token=%s\n\nIf you did not request this, please ignore this email.",
            frontendUrl, 
            verificationToken.getToken()
        );
    }
}