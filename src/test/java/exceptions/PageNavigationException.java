package exceptions;

public class PageNavigationException extends RuntimeException {

    public PageNavigationException(String message) {
        super(message);
    }

    public PageNavigationException(String message, Throwable cause) {
        super(message, cause);
    }
}