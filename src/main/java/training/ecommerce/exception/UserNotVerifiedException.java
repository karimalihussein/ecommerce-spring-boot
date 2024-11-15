package training.ecommerce.exception;

/**
 * Exception thrown when a user attempts an action without verifying their account.
 */
public class UserNotVerifiedException extends RuntimeException {

    private final boolean newEmailSent;

    /**
     * Constructs a new UserNotVerifiedException with the specified detail message and email status.
     *
     * @param message      the detail message
     * @param newEmailSent indicates if a new verification email was sent
     */
    public UserNotVerifiedException(String message, boolean newEmailSent) {
        super(message);
        this.newEmailSent = newEmailSent;
    }

    /**
     * Constructs a new UserNotVerifiedException with the specified detail message, cause, and email status.
     *
     * @param message      the detail message
     * @param cause        the cause of the exception
     * @param newEmailSent indicates if a new verification email was sent
     */
    public UserNotVerifiedException(String message, Throwable cause, boolean newEmailSent) {
        super(message, cause);
        this.newEmailSent = newEmailSent;
    }

    /**
     * Constructs a new UserNotVerifiedException with the specified cause and email status.
     *
     * @param cause        the cause of the exception
     * @param newEmailSent indicates if a new verification email was sent
     */
    public UserNotVerifiedException(Throwable cause, boolean newEmailSent) {
        super(cause);
        this.newEmailSent = newEmailSent;
    }

    /**
     * Returns whether a new verification email was sent.
     *
     * @return true if a new email was sent, false otherwise
     */
    public boolean isNewEmailSent() {
        return newEmailSent;
    }
}