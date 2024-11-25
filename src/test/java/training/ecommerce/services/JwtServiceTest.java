package training.ecommerce.services;

import com.auth0.jwt.exceptions.JWTDecodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import training.ecommerce.model.User;
import training.ecommerce.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGenerateToken() {
        // Create a mock user
        User mockUser = new User();
        mockUser.setUsername("testGenerateToken");
        mockUser.setEmail("user2@example.com");

        // Mock the repository to return the mock user when findByUsername is called
        when(userRepository.findByUsername("testGenerateToken")).thenReturn(Optional.of(mockUser));

        // Generate a token
        String token = jwtService.generate(mockUser);

        // Assert that the token contains the correct username
        String usernameFromToken = jwtService.getUsername(token);
        Assertions.assertEquals("testGenerateToken", usernameFromToken, "Token should contain the correct username.");
    }

    @Test
    public void testGenerateVerificationToken() {
        // Create a mock user
        User mockUser = new User();
        mockUser.setUsername("verificationUser");
        mockUser.setEmail("verification@example.com");

        // Generate a verification token
        String verificationToken = jwtService.generateVerificationToken(mockUser);

        // Assert that the token contains the correct email
        String usernameFromVerificationToken = jwtService.getUsername(verificationToken);
        Assertions.assertNull(usernameFromVerificationToken, "Verification token should not contain username.");
    }

    @Test
    public void testGetUsernameFromInvalidToken() {
        String invalidToken = "invalid.token.structure";

        // Assert that an invalid token throws the appropriate exception
        Assertions.assertThrows(com.auth0.jwt.exceptions.JWTDecodeException.class, () -> {
            jwtService.getUsername(invalidToken);
        }, "Expected JWTDecodeException for invalid token structure.");
    }

    @Test
    public void testGetUsernameFromNullToken() {
        // Assert that a null token throws the appropriate exception
        Assertions.assertThrows(JWTDecodeException.class, () -> {
            jwtService.getUsername(null);
        }, "Expected IllegalArgumentException for null token.");
    }
}