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