package training.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "line_1", nullable = false, length = 255)
    private String line1;

    @Column(name = "line_2", length = 255)
    private String line2;

    @Column(name = "city", nullable = false, length = 255)
    private String city;

    @Column(name = "country", nullable = false, length = 75)
    private String country;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
