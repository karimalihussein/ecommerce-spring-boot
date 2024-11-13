package training.ecommerce.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.annotation.PostConstruct;
import training.ecommerce.model.User;

@Service
public class JwtService {
    private static final String USERNAME_KEY = "username";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiration}")
    private long expirationInMinutes;
    private Algorithm algorithm;


    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(secret);
    }

    public String generate(User user) {
        return JWT.create()
                .withIssuer(issuer)
                .withClaim(USERNAME_KEY, user.getUsername())
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000))
                .sign(algorithm);
    }

    public String getUsername(String token) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getClaim(USERNAME_KEY)
                .asString();
    }
}
