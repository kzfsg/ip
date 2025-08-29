import java.util.ArrayList;

public class Monday {
    private static Ui ui = new Ui();
    public static void main(String[] args){
        try {
            ui.showWelcome();
            runChatbot();
        } catch (UnknownCommandException e) {
            ui.showError(e.getMessage());
        } finally {
            ui.close();
        }
    }


    private static void runChatbot() throws UnknownCommandException {
        String input;

        // Load existing tasks from file at startup
        ArrayList<Task> inputs = Storage.loadTasks();
        ui.showLoadedTasksMessage(inputs.size());

        while (true) {
            try {
                input = ui.readCommand();
                Parser.Command command = Parser.parse(input);
                
                switch (command.getType()) {
                    case BYE:
                        ui.showGoodbye();
                        return;
                    case LIST:
                        ui.showTaskList(inputs);
                        break;
                    case MARK:
                        handleMarkUnmark(inputs, command, true);
                        break;
                    case UNMARK:
                        handleMarkUnmark(inputs, command, false);
                        break;
                    case TODO:
                        addTodo(inputs, command);
                        break;
                    case DEADLINE:
                        addDeadline(inputs, command);
                        break;
                    case EVENT:
                        addEvent(inputs, command);
                        break;
                    case DELETE:
                        deleteTask(inputs, command);
                        break;
                    case UNKNOWN:
                    default:
                        throw new UnknownCommandException();
                }
            } catch(EmptyDescriptionException | InvalidCommandFormatException |
                    UnknownCommandException | InvalidTaskNumberException | InvalidDateTimeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static void deleteTask(ArrayList<Task> inputs, Parser.Command command) 
            throws InvalidTaskNumberException {
        int taskNum = Integer.parseInt(command.getParameter());
        if (taskNum < 1 || taskNum > inputs.size()) {
            throw new InvalidTaskNumberException();
        }

        Task deletedTask = inputs.remove(taskNum - 1);

        // Save to file after deletion
        Storage.saveTasks(inputs);

        ui.showTaskDeletedMessage(deletedTask, inputs.size());
    }


    private static void handleMarkUnmark(ArrayList<Task> inputs, Parser.Command command, boolean mark)
            throws InvalidTaskNumberException {
        int taskNum = Integer.parseInt(command.getParameter());
        if (taskNum < 1 || taskNum > inputs.size()) {
            throw new InvalidTaskNumberException();
        }

        Task task = inputs.get(taskNum - 1);
        if (mark) task.markAsDone(); else task.markAsNotDone();

        // Save to file after marking/unmarking
        Storage.saveTasks(inputs);

        ui.showMarkTaskMessage(task, mark);
    }

    private static void addDeadline(ArrayList<Task> inputs, Parser.Command command)
            throws InvalidDateTimeException {
        try {
            inputs.add(new Deadline(command.getDescription(), command.getParameter()));

            // Save to file after adding
            Storage.saveTasks(inputs);

            ui.showTaskAddedMessage(inputs.get(inputs.size() - 1), inputs.size());
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateTimeException(e.getMessage());
        }
    }

    private static void addTodo(ArrayList<Task> inputs, Parser.Command command) {
        inputs.add(new Todo(command.getDescription()));

        // Save to file after adding
        Storage.saveTasks(inputs);

        ui.showTaskAddedMessage(inputs.get(inputs.size() - 1), inputs.size());
    }

    private static void addEvent(ArrayList<Task> inputs, Parser.Command command)
            throws InvalidDateTimeException {
        try {
            String[] times = command.getParameters();
            inputs.add(new Event(command.getDescription(), times[0], times[1]));

            // Save to file after adding
            Storage.saveTasks(inputs);

            ui.showTaskAddedMessage(inputs.get(inputs.size() - 1), inputs.size());
        } catch (java.time.format.DateTimeParseException | IllegalArgumentException e) {
            throw new InvalidDateTimeException(e.getMessage());
        }
    }

}