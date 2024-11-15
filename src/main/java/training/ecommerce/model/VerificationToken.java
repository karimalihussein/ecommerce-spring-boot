package training.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Timestamp expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    public VerificationToken() {
        // Default constructor for JPA
    }

    public VerificationToken(String token, User user, Timestamp expiresAt, Timestamp createdAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    /**
     * Checks if the token is expired.
     *
     * @return true if the token is expired, false otherwise.
     */
    public boolean isExpired() {
        return expiresAt.before(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Updates the expiry timestamp for the token.
     *
     * @param newExpiry new expiry timestamp.
     */
    public void extendExpiry(Timestamp newExpiry) {
        this.expiresAt = newExpiry;
    }
}