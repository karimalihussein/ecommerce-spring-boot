package training.ecommerce.api.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import training.ecommerce.api.request.LoginRequest;
import training.ecommerce.api.request.RegistrationRequest;
import training.ecommerce.dtos.LoginResponse;
import training.ecommerce.exception.EmailFailureExpectation;
import training.ecommerce.exception.UserAlreadyExistsException;
import training.ecommerce.exception.UserNotVerifiedException;
import training.ecommerce.services.UserService;

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
            return ResponseEntity.created(null).body("User registered successfully.");
        } catch (UserAlreadyExistsException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EmailFailureExpectation e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest login) {
        try {
            String token = userService.login(login);
            return ResponseEntity.ok(buildLoginResponse(true, token, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildLoginResponse(false, null, e.getMessage()));
        } catch (UserNotVerifiedException e) {
            String reason = buildVerificationFailureMessage(e.isNewEmailSent());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildLoginResponse(false, null, reason));
        } catch (EmailFailureExpectation e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildLoginResponse(false, null, e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        try {
            userService.verify(token);
            return ResponseEntity.ok("User verified successfully.");
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid or expired token.");
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    // Helper Methods
    private ResponseEntity<String> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }

    private LoginResponse buildLoginResponse(boolean success, String token, String failureMessage) {
        LoginResponse response = new LoginResponse();
        response.setSuccess(success);
        response.setToken(token);
        response.setFailureMessage(failureMessage);
        return response;
    }

    private String buildVerificationFailureMessage(boolean newEmailSent) {
        return newEmailSent ? "USER_NOT_VERIFIED_NEW_EMAIL_SENT" : "USER_NOT_VERIFIED";
    }
}