package training.ecommerce.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import training.ecommerce.api.request.LoginRequest;
import training.ecommerce.api.request.RegistrationRequest;
import training.ecommerce.exception.EmailFailureExpectation;
import training.ecommerce.exception.UserAlreadyExistsException;
import training.ecommerce.exception.UserNotVerifiedException;
import training.ecommerce.model.User;
import training.ecommerce.repository.UserRepository;
import training.ecommerce.repository.VerificationTokenRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VerificationTokenRepository verificationTokenRepository;

    @MockBean
    private EncryptionService encryptionService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private EmailService emailService;

    @Test
    void register_Success() throws EmailFailureExpectation, UserAlreadyExistsException {
        RegistrationRequest request = new RegistrationRequest("user", "email@example.com", "password", "password", "John", "Doe");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        User mockUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        assertDoesNotThrow(() -> userService.register(request));
        verify(emailService, times(1)).sendVerificationEmail(any());
    }

    @Test
    void register_UserAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest("user", "email@example.com", "password", "password", "John", "Doe");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));
    }

    @Test
    void login_Success() throws EmailFailureExpectation {
        LoginRequest loginRequest = new LoginRequest("user", "password");

        User mockUser = new User();
        mockUser.setEmailVerified(true);
        mockUser.setPassword("encryptedPassword");

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(mockUser));
        when(encryptionService.matches(loginRequest.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtService.generate(mockUser)).thenReturn("jwtToken");

        String token = userService.login(loginRequest);

        assertNotNull(token);
        assertEquals("jwtToken", token);
    }

    @Test
    void login_UserNotVerified() {
        LoginRequest loginRequest = new LoginRequest("user", "password");

        User mockUser = new User();
        mockUser.setEmailVerified(false);

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(mockUser));
        when(encryptionService.matches(loginRequest.getPassword(), mockUser.getPassword())).thenReturn(true);

        assertThrows(UserNotVerifiedException.class, () -> userService.login(loginRequest));
    }


    @Test
    void login_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("user", "password");

        User mockUser = new User();
        mockUser.setEmailVerified(true);
        mockUser.setPassword("encryptedPassword");

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(mockUser));
        when(encryptionService.matches(loginRequest.getPassword(), mockUser.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.login(loginRequest));
    }
}