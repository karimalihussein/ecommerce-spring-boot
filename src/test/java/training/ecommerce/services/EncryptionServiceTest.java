package training.ecommerce.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;

@SpringBootTest(classes = {EncryptionService.class})
@TestPropertySource(properties = {"encryption.salt.rounds=10"})
public class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void testPasswordEncryption() {
        String password = "password";
        String encryptedPassword = encryptionService.encrypt(password);
        assertNotNull(encryptedPassword, "The encrypted password should not be null");
        Assertions.assertTrue(encryptionService.matches(password, encryptedPassword), "Hashed password should match original.");
        Assertions.assertFalse(encryptionService.matches(password + "Sike!", encryptedPassword), "Altered password should not be valid.");
    }
}