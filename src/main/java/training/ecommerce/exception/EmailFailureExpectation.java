package training.ecommerce.exception;

/**
 * Custom exception to handle email sending failures.
 */
public class EmailFailureExpectation extends Exception {

    /**
     * Constructs a new EmailFailureExpectation with the specified detail message.
     *
     * @param message the detail message
     */
    public EmailFailureExpectation(String message) {
        super(message);
    }

    /**
     * Constructs a new EmailFailureExpectation with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public EmailFailureExpectation(String message, Throwable cause) {
        super(message, cause);
    }
}