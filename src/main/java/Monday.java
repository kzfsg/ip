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
                if (input.equals("bye")) {
                    ui.showGoodbye();
                    break;
                } else if (input.equals("list")) {
                    ui.showTaskList(inputs);
                } else if (input.startsWith("mark ")) {
                    handleMarkUnmark(inputs, input, true);
                } else if (input.startsWith("unmark ")) {
                    handleMarkUnmark(inputs, input, false);
                } else if (input.startsWith("todo")) {
                    addTodo(inputs, input);
                } else if (input.startsWith("deadline")) {
                    addDeadline(inputs, input);
                } else if (input.startsWith("event")) {
                    addEvent(inputs, input);
                } else if (input.startsWith("delete")) {
                    deleteTask(inputs, input);
                } else {
                    throw new UnknownCommandException();
                }
            } catch(EmptyDescriptionException | InvalidCommandFormatException |
                    UnknownCommandException | InvalidTaskNumberException | InvalidDateTimeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static void deleteTask(ArrayList<Task> inputs, String input) throws InvalidTaskNumberException, InvalidCommandFormatException
    {
        try {
            if (input.equals("delete")) {
                throw new InvalidCommandFormatException("delete <task number>");
            }

            int taskNum = Integer.parseInt(input.substring(7).trim());
            if (taskNum < 1 || taskNum > inputs.size()) {
                throw new InvalidTaskNumberException();
            }

            Task deletedTask = inputs.remove(taskNum - 1);

            // Save to file after deletion
            Storage.saveTasks(inputs);

            ui.showTaskDeletedMessage(deletedTask, inputs.size());

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new InvalidTaskNumberException();
        }
    }


    private static void handleMarkUnmark(ArrayList<Task> inputs, String input, boolean mark)
            throws InvalidTaskNumberException {
        try {
            int taskNum = Integer.parseInt(input.substring(mark ? 5 : 7));
            if (taskNum < 1 || taskNum > inputs.size()) {
                throw new InvalidTaskNumberException();
            }

            Task task = inputs.get(taskNum - 1);
            if (mark) task.markAsDone(); else task.markAsNotDone();

            // Save to file after marking/unmarking
            Storage.saveTasks(inputs);

            ui.showMarkTaskMessage(task, mark);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new InvalidTaskNumberException();
        }
    }

    private static void addDeadline(ArrayList<Task> inputs, String input)
            throws EmptyDescriptionException, InvalidCommandFormatException, InvalidDateTimeException {
        if (input.equals("deadline")) {
            throw new EmptyDescriptionException("deadline");
        }

        String[] parts = input.split(" /by ");
        if (parts.length != 2) {
            throw new InvalidCommandFormatException("deadline <description> /by <date time>");
        }

        String desc = extractAndValidateDescription(parts[0], 9, "deadline");
        String dueDateTime = parts[1].trim();

        if (dueDateTime.isEmpty()) {
            throw new EmptyDescriptionException("deadline due date");
        }

        try {
            inputs.add(new Deadline(desc, dueDateTime));

            // Save to file after adding
            Storage.saveTasks(inputs);

            ui.showTaskAddedMessage(inputs.get(inputs.size() - 1), inputs.size());
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateTimeException(e.getMessage());
        }
    }

    private static void addTodo(ArrayList<Task> inputs, String input)
            throws EmptyDescriptionException {
        if (input.equals("todo")) {
            throw new EmptyDescriptionException("todo");
        }
        String description = extractAndValidateDescription(input, 5, "todo");

        inputs.add(new Todo(description));

        // Save to file after adding
        Storage.saveTasks(inputs);

        ui.showTaskAddedMessage(inputs.get(inputs.size() - 1), inputs.size());
    }

    private static void addEvent(ArrayList<Task> inputs, String input)
            throws EmptyDescriptionException, InvalidCommandFormatException, InvalidDateTimeException {
        if (input.equals("event")) {
            throw new EmptyDescriptionException("event");
        }

        String[] parts = input.split(" /from ");
        if (parts.length != 2) {
            throw new InvalidCommandFormatException("event <description> /from <start> /to <end>");
        }

        String desc = extractAndValidateDescription(parts[0], 6, "event");
        String[] times = parts[1].split(" /to ");

        if (times.length != 2) {
            throw new InvalidCommandFormatException("event <description> /from <start> /to <end>");
        }

        String startTime = times[0].trim();
        String endTime = times[1].trim();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            throw new EmptyDescriptionException("event time");
        }

        try {
            inputs.add(new Event(desc, startTime, endTime));

            // Save to file after adding
            Storage.saveTasks(inputs);

            ui.showTaskAddedMessage(inputs.get(inputs.size() - 1), inputs.size());
        } catch (java.time.format.DateTimeParseException | IllegalArgumentException e) {
            throw new InvalidDateTimeException(e.getMessage());
        }
    }

    // Helper method following DRY principle
    private static String extractAndValidateDescription(String input, int startIndex, String taskType)
            throws EmptyDescriptionException {
        String description = input.substring(startIndex).trim();
        if (description.isEmpty()) {
            throw new EmptyDescriptionException(taskType);
        }
        return description;
    }
}