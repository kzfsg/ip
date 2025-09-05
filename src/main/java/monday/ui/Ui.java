package monday.ui;

import java.util.Scanner;
import java.util.ArrayList;

import monday.task.Task;
import monday.task.TaskList;

/**
 * Handles all user interface interactions for the Monday task manager.
 * Follows Single Responsibility Principle - only handles UI operations.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a new Ui instance and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message with ASCII art logo when the application starts.
     */
    public void showWelcome() {
        String logo = " __  __                 _             \n"
                + "|  \\/  |               | |            \n"
                + "| \\  / | ___  _ __   __| | __ _ _   _  \n"
                + "| |\\/| |/ _ \\| '_ \\ / _` |/ _` | | | | \n"
                + "| |  | | (_) | | | | (_| | (_| | |_| | \n"
                + "|_|  |_|\\___/|_| |_|\\__,_|\\__,_|\\__, | \n"
                + "                                __/ | \n"
                + "                               |___/  \n";

        System.out.println("Hello I'm\n" + logo);
        System.out.println("What can I do for you?\n");
    }

    /**
     * Reads a command from the user.
     *
     * @return The user's command as a string
     */
    public String readCommand() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    /**
     * Displays a goodbye message when the application exits.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays the full task list to the user.
     *
     * @param taskList The TaskList object to display
     */
    public void showTaskList(TaskList taskList) {
        System.out.println(taskList.toString());
    }

    /**
     * Displays a message confirming that tasks have been loaded from storage.
     *
     * @param numberOfTasks The number of tasks loaded
     */
    public void showLoadedTasksMessage(int numberOfTasks) {
        System.out.println("Successfully loaded " + numberOfTasks + " tasks from storage.");
    }

    /**
     * Displays a confirmation message when a task's status is changed.
     *
     * @param task The task that was marked or unmarked
     * @param isMarked True if the task was marked, false if it was unmarked
     */
    public void showMarkUnmarkMessage(Task task, boolean isMarked) {
        System.out.println((isMarked ?
                "Nice! I've marked this task as done:" :
                "OK, I've marked this task as not done yet:") + "\n  " + task);
    }

    /**
     * Displays a confirmation message when a new task is added.
     *
     * @param task The task that was added
     * @param totalTasks The total number of tasks after addition
     */
    public void showTaskAddedMessage(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param deletedTask The task that was removed
     * @param remainingTasks The number of tasks remaining after deletion
     */
    public void showTaskDeletedMessage(Task deletedTask, int remainingTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + remainingTasks + " tasks in the list.");
    }

    /**
     * Displays a list of matching tasks found during search.
     *
     * @param matchingTasks The list of tasks that match the search criteria
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + "." + matchingTasks.get(i));
            }
        }
    }

    /**
     * Displays help information showing all available commands and their usage.
     */
    public void showHelp() {
        System.out.println("Here are the available commands:\n");
        System.out.println("1. list - Display all tasks");
        System.out.println("   Usage: list");
        System.out.println();
        System.out.println("2. todo - Add a simple task");
        System.out.println("   Usage: todo <description>");
        System.out.println("   Example: todo read book");
        System.out.println();
        System.out.println("3. deadline - Add a task with a due date");
        System.out.println("   Usage: deadline <description> /by <yyyy-MM-dd HHmm>");
        System.out.println("   Example: deadline return book /by 2024-12-31 1800");
        System.out.println();
        System.out.println("4. event - Add an event with start and end times");
        System.out.println("   Usage: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        System.out.println("   Example: event project meeting /from 2024-12-01 1400 /to 2024-12-01 1600");
        System.out.println();
        System.out.println("5. mark - Mark a task as completed");
        System.out.println("   Usage: mark <task_number>");
        System.out.println("   Example: mark 1");
        System.out.println();
        System.out.println("6. unmark - Mark a task as not completed");
        System.out.println("   Usage: unmark <task_number>");
        System.out.println("   Example: unmark 1");
        System.out.println();
        System.out.println("7. delete - Remove a task from the list");
        System.out.println("   Usage: delete <task_number>");
        System.out.println("   Example: delete 1");
        System.out.println();
        System.out.println("8. find - Search for tasks containing a keyword");
        System.out.println("   Usage: find <keyword>");
        System.out.println("   Example: find book");
        System.out.println();
        System.out.println("9. help - Show this help message");
        System.out.println("   Usage: help");
        System.out.println();
        System.out.println("10. bye - Exit the application");
        System.out.println("    Usage: bye");
        System.out.println();
        System.out.println("Note: Task numbers are 1-based (start from 1)");
        System.out.println("Date format: yyyy-MM-dd HHmm (e.g., 2024-12-31 1800 for Dec 31, 2024 at 6:00 PM)");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Closes the scanner to release system resources.
     */
    public void close() {
        scanner.close();
    }
}
