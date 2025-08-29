package monday.exception;

/**
 * Thrown when the user provides an invalid task number for mark/unmark operations.
 * This includes non-numeric values, negative numbers, or numbers exceeding the task list size.
 */
public class InvalidTaskNumberException extends Exception {
    public InvalidTaskNumberException() {
        super("Invalid task number.");
    }
}