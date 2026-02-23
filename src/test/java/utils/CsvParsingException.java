package utils;

/**
 * Custom exception for CSV parsing errors.
 * Thrown when CSV structure, headers, or data rows are invalid.
 */
public class CsvParsingException extends RuntimeException {

    public CsvParsingException(String message) {
        super(message);
    }

    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}