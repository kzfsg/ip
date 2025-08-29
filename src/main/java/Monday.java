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
                
                if (input.equals("bye")) {
                    ui.showGoodbye();
                    break;
                }
                
                Parser.Command command = Parser.parse(input);
                Parser.execute(command, tasks, ui);
                
            } catch (EmptyDescriptionException | InvalidCommandFormatException |
                     UnknownCommandException | InvalidTaskNumberException | InvalidDateTimeException e) {
                ui.showError(e.getMessage());
            }
        }
        
        ui.close();
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