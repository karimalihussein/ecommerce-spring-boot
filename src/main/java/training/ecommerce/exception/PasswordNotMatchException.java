package training.ecommerce.exception;

/**
 * Exception thrown when a provided password does not match the expected value.
 */
public class PasswordNotMatchException extends RuntimeException {

    /**
     * Constructs a new PasswordNotMatchException with the specified detail message.
     *
     * @param message the detail message
     */
    public PasswordNotMatchException(String message) {
        super(message);
    }

    /**
     * Constructs a new PasswordNotMatchException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public PasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new PasswordNotMatchException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public PasswordNotMatchException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new PasswordNotMatchException with no detail message or cause.
     */
    public PasswordNotMatchException() {
        super("Passwords do not match");
    }
}