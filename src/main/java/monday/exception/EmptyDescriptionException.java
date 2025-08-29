package monday.exception;

/**
 * Thrown when a task description is empty or contains only whitespace.
 * Used for todos, deadlines, and events that require non-empty descriptions.
 */
public class EmptyDescriptionException extends Exception {
    public EmptyDescriptionException(String taskType) {
        super("The description of a " + taskType + " cannot be empty.");
    }
}