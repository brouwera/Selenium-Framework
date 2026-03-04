package exceptions;

public class InvalidTestDataException extends RuntimeException {

    // ============================================================
    // Constructors
    // ============================================================

    public InvalidTestDataException(String message) {
        super(message);
    }

    public InvalidTestDataException(String message, Throwable cause) {
        super(message, cause);
    }
}