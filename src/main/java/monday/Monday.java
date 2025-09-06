package monday;

import monday.exception.EmptyDescriptionException;
import monday.exception.InvalidCommandFormatException;
import monday.exception.InvalidDateTimeException;
import monday.exception.InvalidTaskNumberException;
import monday.exception.TaskLoadingException;
import monday.exception.UnknownCommandException;
import monday.parser.Parser;
import monday.parser.Parser.CommandType;
import monday.storage.Storage;
import monday.task.TaskList;
import monday.ui.Ui;

/**
 * Main application class that orchestrates the Monday task manager.
 * Serves as a thin layer that coordinates between Storage, TaskList, Ui, and Parser.
 */
public class Monday {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Monday application instance with the specified file path.
     * Initializes all components and loads existing tasks from storage.
     *
     * @param filePath The path to the file where tasks are stored
     */
    public Monday(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.load(), storage);
            ui.showLoadedTasksMessage(tasks.size());
        } catch (TaskLoadingException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList(storage);
        }
    }

    /**
     * Runs the main application loop.
     * Handles user input, command parsing, and execution until the user exits.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand();
                Parser.Command command = Parser.parse(input);

                if (command.getType() == CommandType.BYE) {
                    ui.showGoodbye();
                    break;
                }

                Parser.execute(command, tasks, ui);

            } catch (EmptyDescriptionException | InvalidCommandFormatException |
                     UnknownCommandException | InvalidTaskNumberException | InvalidDateTimeException e) {
                ui.showError(e.getMessage());
            }
        }


        ui.close();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return "Monday heard: " + input;
    }

    /**
     * Entry point for the Monday task manager application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        new Monday("./data/monday.txt").run();
    }
}
