package io.github.amayaframework.routing;

/**
 * Exception thrown when request parameters cannot be parsed or validated correctly.
 * <p>
 * Typical cases include type conversion errors, missing required parameters,
 * or values that do not match the expected format.
 */
public class IllegalParamException extends RuntimeException {

    /**
     * Creates a new exception with the specified detail message and cause.
     *
     * @param message the detail message describing the error
     * @param cause   the underlying cause of the failure, may be {@code null}
     */
    public IllegalParamException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new exception with the specified detail message.
     *
     * @param message the detail message describing the error
     */
    public IllegalParamException(String message) {
        super(message);
    }
}
