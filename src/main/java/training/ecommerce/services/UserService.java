package training.ecommerce.services;

import org.springframework.stereotype.Service;

import training.ecommerce.api.request.LoginRequest;
import training.ecommerce.api.request.RegistrationRequest;
import training.ecommerce.exception.UserAlreadyExistsException;
import training.ecommerce.repository.UserRepository;
import training.ecommerce.model.User;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final JwtService jwtService;
    
    public UserService(UserRepository userRepository, EncryptionService encryptionService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public User register(RegistrationRequest request) {
        validateRequest(request);
    
        User user = createUserFromRequest(request);
    
        return userRepository.save(user);
    }

    public String login(LoginRequest login) {
        User user = userRepository.findByUsername(login.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        if (!encryptionService.matches(login.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
    
        return jwtService.generate(user);
    }
    
    private void validateRequest(RegistrationRequest request) {
        userRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
                });
    
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
                });
    
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }
    
    private User createUserFromRequest(RegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(encryptionService.encrypt(request.getPassword()));
        return user;
    }
}