/**
 * Custom exception classes for the Monday chatbot application.
 * These exceptions handle specific error conditions that can occur
 * during task management operations.
 */

/**
 * Thrown when a task description is empty or contains only whitespace.
 * Used for todos, deadlines, and events that require non-empty descriptions.
 */
class EmptyDescriptionException extends Exception {
    public EmptyDescriptionException(String taskType) {
        super("The description of a " + taskType + " cannot be empty.");
    }
}

/**
 * Thrown when a command does not follow the expected format.
 * Provides specific format instructions to help users correct their input.
 */
class InvalidCommandFormatException extends Exception {
    public InvalidCommandFormatException(String format) {
        super("Format: " + format);
    }
}

/**
 * Thrown when the user enters a command that is not recognized by the system.
 * Helps guide users toward valid commands.
 */
class UnknownCommandException extends Exception {
    public UnknownCommandException() {
        super("I'm sorry, but I don't recognize that command. Please try again.");
    }
}

/**
 * Thrown when the user provides an invalid task number for mark/unmark operations.
 * This includes non-numeric values, negative numbers, or numbers exceeding the task list size.
 */
class InvalidTaskNumberException extends Exception {
    public InvalidTaskNumberException() {
        super("Invalid task number.");
    }
}

/**
 * Thrown when date/time parsing fails due to invalid format.
 * Provides guidance on supported date formats.
 */
class InvalidDateTimeException extends Exception {
    public InvalidDateTimeException(String originalMessage) {
        super(createUserFriendlyMessage(originalMessage));
    }

    private static String createUserFriendlyMessage(String originalMessage) {
        return "Invalid date/time format. Please use one of these formats:\n" +
                "  - yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)\n" +
                "  - d/M/yyyy HHmm (e.g., 2/12/2019 1800)\n" +
                "  - yyyy-MM-dd (e.g., 2019-12-02, defaults to 11:59 PM for deadlines)\n" +
                "Original error: " + originalMessage;
    }
}

/**
 * Thrown when tasks cannot be loaded from storage during application startup.
 * This typically occurs when the data file is corrupted, inaccessible, or has permission issues.
 */
class TaskLoadingException extends Exception {
    public TaskLoadingException(String message, Throwable cause) {
        super("Error loading tasks from storage: " + message + ". Starting with an empty task list.", cause);
    }

    public TaskLoadingException(String message) {
        super("Error loading tasks from storage: " + message + ". Starting with an empty task list.");
    }
}