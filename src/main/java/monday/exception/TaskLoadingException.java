package monday.exception;

/**
 * Thrown when tasks cannot be loaded from storage during application startup.
 * This typically occurs when the data file is corrupted, inaccessible, or has permission issues.
 */
public class TaskLoadingException extends Exception {
    public TaskLoadingException(String message, Throwable cause) {
        super("Error loading tasks from storage: " + message + ". Starting with an empty task list.", cause);
    }

    public TaskLoadingException(String message) {
        super("Error loading tasks from storage: " + message + ". Starting with an empty task list.");
    }
}