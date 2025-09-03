package io.github.amayaframework.routing;

public class IllegalParamException extends RuntimeException {

    public IllegalParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParamException(String message) {
        super(message);
    }
}
