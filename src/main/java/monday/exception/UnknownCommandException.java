package monday.exception;

/**
 * Thrown when the user enters a command that is not recognized by the system.
 * Helps guide users toward valid commands.
 */
public class UnknownCommandException extends Exception {
    public UnknownCommandException() {
        super("I'm sorry, but I don't recognize that command. Please try again.");
    }
}