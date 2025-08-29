package monday.exception;

/**
 * Thrown when a command does not follow the expected format.
 * Provides specific format instructions to help users correct their input.
 */
public class InvalidCommandFormatException extends Exception {
    public InvalidCommandFormatException(String format) {
        super("Format: " + format);
    }
}