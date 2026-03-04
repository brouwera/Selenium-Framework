package exceptions;

public class PageNavigationException extends RuntimeException {

    // ============================================================
    // Constructors
    // ============================================================

    public PageNavigationException(String message) {
        super(message);
    }

    public PageNavigationException(String message, Throwable cause) {
        super(message, cause);
    }
}