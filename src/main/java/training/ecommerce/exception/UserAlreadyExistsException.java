package training.ecommerce.exception;

/**
 * Custom exception to handle cases where a user already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Default constructor for UserAlreadyExistsException.
     */
    public UserAlreadyExistsException() {
        super("User already exists.");
    }

    /**
     * Constructor with a custom message.
     *
     * @param message Custom exception message.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom message and a cause.
     *
     * @param message Custom exception message.
     * @param cause   The cause of the exception.
     */
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     *
     * @param cause The cause of the exception.
     */
    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}