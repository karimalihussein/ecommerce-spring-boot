package training.ecommerce.repository;


import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import training.ecommerce.model.User;
import training.ecommerce.model.VerificationToken;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);
}