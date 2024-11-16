package training.ecommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.ecommerce.api.request.LoginRequest;
import training.ecommerce.api.request.RegistrationRequest;
import training.ecommerce.exception.EmailFailureExpectation;
import training.ecommerce.exception.PasswordNotMatchException;
import training.ecommerce.exception.UserAlreadyExistsException;
import training.ecommerce.exception.UserNotVerifiedException;
import training.ecommerce.model.User;
import training.ecommerce.model.VerificationToken;
import training.ecommerce.repository.UserRepository;
import training.ecommerce.repository.VerificationTokenRepository;

import java.sql.Timestamp;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EncryptionService encryptionService;
    private final JwtService jwtService;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            VerificationTokenRepository verificationTokenRepository,
            EncryptionService encryptionService,
            JwtService jwtService,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Transactional
    public void register(RegistrationRequest request) throws EmailFailureExpectation, UserAlreadyExistsException {
        validateRegistrationRequest(request);

        User user = createUserFromRequest(request);
        userRepository.save(user);

        createAndSendVerificationToken(user);
    }

    public String login(LoginRequest loginRequest) throws UserNotVerifiedException, EmailFailureExpectation {
        User user = findUserByUsername(loginRequest.getUsername());
        validatePassword(loginRequest.getPassword(), user.getPassword());
        validateUserEmailVerification(user);

        return jwtService.generate(user);
    }

    @Transactional
    public boolean verify(String token) {
        VerificationToken verificationToken = findValidVerificationToken(token);

        User user = verificationToken.getUser();
        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("User is already verified");
        }

        user.setEmailVerified(true);
        userRepository.save(user);

        verificationTokenRepository.deleteByUser(user);

        return true;
    }

    // Private helper methods

    private void validateRegistrationRequest(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordNotMatchException("Password and confirm password do not match");
        }
    }

    private User createUserFromRequest(RegistrationRequest request) {
        return new User(
                request.getUsername(),
                encryptionService.encrypt(request.getPassword()),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()
        );
    }

    private VerificationToken createAndSendVerificationToken(User user) throws EmailFailureExpectation {
        VerificationToken token = createVerificationToken(user);
        verificationTokenRepository.save(token);
        emailService.sendVerificationEmail(token);
        return token;
    }

    private VerificationToken createVerificationToken(User user) {
        return new VerificationToken(
                jwtService.generateVerificationToken(user),
                user,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis() + 3600000)
        );
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!encryptionService.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    private void validateUserEmailVerification(User user) throws UserNotVerifiedException, EmailFailureExpectation {
        if (!user.isEmailVerified()) {
            boolean shouldResendVerification = user.getVerificationTokens().isEmpty() ||
                    user.getVerificationTokens().getFirst().isExpired();

            if (shouldResendVerification) {
                VerificationToken verificationToken = createAndSendVerificationToken(user);
                verificationTokenRepository.save(verificationToken);
            }

            throw new UserNotVerifiedException("User has not verified their email", true);
        }
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }

    private VerificationToken findValidVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
    }
}