package training.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.transaction.Transactional;
import training.ecommerce.api.request.RegistrationRequest;
import training.ecommerce.exception.PasswordNotMatchException;
import training.ecommerce.exception.UserAlreadyExistsException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;

import static org.awaitility.Awaitility.await;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class UserServiceV2Test {

    @RegisterExtension
    private static final GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void testRegisterUser() {
        RegistrationRequest body = new RegistrationRequest();
        body.setEmail("UserServiceV2Test$testRegisterUser@junit.com");
        body.setPassword("MySuperSecretPassword");
        body.setConfirmPassword("password"); // Intentionally wrong to trigger exception
        body.setFirstName("John");
        body.setLastName("Doe");
        body.setUsername("user1");

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.register(body),
                "User should not be registered because the username is already in use.");

        body.setUsername("UserServiceV2Test$testRegisterUser");
        body.setEmail("user1@gmail.com");

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.register(body),
                "User should not be registered because the email is already in use.");

        body.setEmail("UserServiceV2Test$testRegisterUser@junit.com");

        Assertions.assertThrows(PasswordNotMatchException.class, () -> userService.register(body),
                "User should not be registered because the password does not match.");

        body.setConfirmPassword("MySuperSecretPassword");

        Assertions.assertDoesNotThrow(() -> userService.register(body), "User should be registered successfully.");
    }
}