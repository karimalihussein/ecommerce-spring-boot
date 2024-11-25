package training.ecommerce.services;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import training.ecommerce.api.request.LoginRequest;
import training.ecommerce.api.request.RegistrationRequest;
import training.ecommerce.exception.PasswordNotMatchException;
import training.ecommerce.exception.UserAlreadyExistsException;
import training.ecommerce.exception.UserNotVerifiedException;

@SpringBootTest
public class UserServiceIntegrationTest {

    @RegisterExtension
    private static final GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void testRegisterUser() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("UserServiceV2Test$testRegisterUser@junit.com");
        request.setPassword("MySuperSecretPassword");
        request.setConfirmPassword("password"); // Intentionally wrong to trigger exception
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setUsername("user1");

        // Test for existing username
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.register(request),
                "User should not be registered because the username is already in use.");

        // Test for existing email
        request.setUsername("UserServiceV2Test$testRegisterUser");
        request.setEmail("user1@gmail.com");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.register(request),
                "User should not be registered because the email is already in use.");

        // Test for password mismatch
        request.setEmail("UserServiceV2Test$testRegisterUser@junit.com");
        Assertions.assertThrows(PasswordNotMatchException.class, () -> userService.register(request),
                "User should not be registered because the password does not match.");

        // Successful registration
        request.setConfirmPassword("MySuperSecretPassword");
        Assertions.assertDoesNotThrow(() -> userService.register(request),
                "User should be registered successfully.");
    }

    @Test
    @Transactional
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("usernotfound");
        loginRequest.setPassword("password");

        // Test for invalid username
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.login(loginRequest),
                "User should not be found");

        // Test for unverified user
        loginRequest.setUsername("user2");
        loginRequest.setPassword("securePassword123");
        Assertions.assertThrows(UserNotVerifiedException.class, () -> userService.login(loginRequest),
                "User should be verified");

        // Successful login
        loginRequest.setUsername("user1");
        Assertions.assertDoesNotThrow(() -> userService.login(loginRequest),
                "User should be logged in successfully.");
    }

    @Test
    @Transactional
    public void testVerify() {
        // Test for invalid token
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.verify("invalidToken"),
                "Token should be invalid");
    }
}