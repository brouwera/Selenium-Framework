package exceptions;

public class FrameworkInitializationException extends RuntimeException {

    public FrameworkInitializationException(String message) {
        super(message);
    }

    public FrameworkInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}