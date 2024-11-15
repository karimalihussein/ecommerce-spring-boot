package training.ecommerce.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle all uncaught exceptions.
     *
     * @param ex The exception that occurred.
     * @return A generic error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Log the exception details
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);

        // Return a generic error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

    /**
     * Handle specific custom exceptions.
     *
     * @param ex The custom exception.
     * @return A custom error response.
     */
    @ExceptionHandler(EmailFailureExpectation.class)
    public ResponseEntity<String> handleEmailFailure(EmailFailureExpectation ex) {
        // Log the exception details
        logger.error("Email failure: {}", ex.getMessage(), ex);

        // Return a specific error response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handle UserNotVerifiedException.
     *
     * @param ex The exception for unverified users.
     * @return A specific error response.
     */
    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<String> handleUserNotVerified(UserNotVerifiedException ex) {
        logger.warn("User not verified: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}