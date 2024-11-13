package training.ecommerce.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import jakarta.annotation.PostConstruct;


@Service
public class EncryptionService {
    @Value("${encryption.salt.rounds}")
    private int saltRounds;
    private String salt;

    @PostConstruct
    public void postConstruct() {
        salt = BCrypt.gensalt(saltRounds);
    }

    public String encrypt(String password) {
        return BCrypt.hashpw(password, salt);
    }

    public boolean matches(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
