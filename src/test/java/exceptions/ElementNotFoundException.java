package exceptions;

public class ElementNotFoundException extends RuntimeException {

    // ============================================================
    // Constructors
    // ============================================================

    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}