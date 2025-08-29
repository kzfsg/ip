package monday.ui;

import java.util.Scanner;
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
     * Displays the goodbye message when the user exits the application.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!\n");
    }

    /**
     * Reads the next line of user input from the scanner.
     *
     * @return The user's input as a String
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the list of tasks to the user.
     * Shows a message if the list is empty, otherwise displays all tasks with numbering.
     *
     * @param taskList The TaskList to display
     */
    public void showTaskList(TaskList taskList) {
        System.out.println(taskList.toString());
    }

    /**
     * Displays a message indicating how many tasks were loaded from previous session.
     *
     * @param taskCount The number of tasks that were loaded
     */
    public void showLoadedTasksMessage(int taskCount) {
        if (taskCount > 0) {
            System.out.println("Loaded " + taskCount + " task(s) from previous session.\n");
        }
    }

    /**
     * Displays a confirmation message when a task is marked as done or undone.
     *
     * @param task The task that was marked/unmarked
     * @param isMarked True if task was marked as done, false if unmarked
     */
    public void showMarkTaskMessage(Task task, boolean isMarked) {
        System.out.println((isMarked ? "Nice! I've marked this task as done:" :
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
     * Displays an error message to the user.
     *
     * @param message The error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Closes the scanner to free up system resources.
     * Should be called when the application is shutting down.
     */
    public void close() {
        scanner.close();
    }
}