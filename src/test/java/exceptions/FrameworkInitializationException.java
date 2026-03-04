package exceptions;

public class FrameworkInitializationException extends RuntimeException {

    // ============================================================
    // Constructors
    // ============================================================

    public FrameworkInitializationException(String message) {
        super(message);
    }

    public FrameworkInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}