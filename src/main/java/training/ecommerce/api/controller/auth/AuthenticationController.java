package training.ecommerce.api.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import training.ecommerce.api.request.LoginRequest;
import training.ecommerce.api.request.RegistrationRequest;
import training.ecommerce.dtos.LoginResponse;
import training.ecommerce.exception.UserAlreadyExistsException;
import training.ecommerce.model.User;
import training.ecommerce.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok("Registration successful");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest login) {
        try {
            String token = userService.login(login);
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/verify")
    public User verify(@AuthenticationPrincipal User user) {
        return user;
    }
    
}