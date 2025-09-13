package monday.parser;

import java.util.ArrayList;

import monday.exception.EmptyDescriptionException;
import monday.exception.InvalidCommandFormatException;
import monday.exception.InvalidDateTimeException;
import monday.exception.InvalidTaskNumberException;
import monday.exception.UnknownCommandException;
import monday.task.Deadline;
import monday.task.Event;
import monday.task.Task;
import monday.task.TaskList;
import monday.task.Todo;
import monday.ui.Ui;

/**
 * Handles parsing and interpretation of user commands.
 * Follows Single Responsibility Principle - only handles command parsing operations.
 */
public class Parser {

    /**
     * Represents the different types of commands that can be parsed.
     */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, HELP, UNKNOWN
    }

    /**
     * Represents a parsed command with its type and relevant parameters.
     */
    public static class Command {
        private CommandType type;
        private String fullCommand;
        private String description;
        private String parameter;
        private String[] parameters;
        private Task.Priority priority;

        /**
         * Constructs a Command object.
         *
         * @param type The type of command
         * @param fullCommand The original full command string
         */
        public Command(CommandType type, String fullCommand) {
            this.type = type;
            this.fullCommand = fullCommand;
        }

        /**
         * Returns the command type.
         *
         * @return The command type
         */
        public CommandType getType() {
            return type;
        }

        /**
         * Returns the full original command string.
         *
         * @return The full command string
         */
        public String getFullCommand() {
            return fullCommand;
        }

        /**
         * Returns the task description from the command.
         *
         * @return The task description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the task description for the command.
         *
         * @param description The task description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Returns the main parameter for the command.
         *
         * @return The main parameter
         */
        public String getParameter() {
            return parameter;
        }

        /**
         * Sets the main parameter for the command.
         *
         * @param parameter The main parameter
         */
        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        /**
         * Returns an array of parameters for the command.
         *
         * @return The array of parameters
         */
        public String[] getParameters() {
            return parameters;
        }

        /**
         * Sets the array of parameters for the command.
         *
         * @param parameters The array of parameters
         */
        public void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        /**
         * Returns the priority for the command.
         *
         * @return The priority level
         */
        public Task.Priority getPriority() {
            return priority;
        }

        /**
         * Sets the priority for the command.
         *
         * @param priority The priority level
         */
        public void setPriority(Task.Priority priority) {
            this.priority = priority;
        }
    }

    /**
     * Parses priority from a command string by looking for /priority flags.
     *
     * @param commandPart The command part to parse priority from
     * @return An array where [0] is the command without priority, [1] is the priority string
     */
    private static String[] extractPriority(String commandPart) {
        String[] parts = commandPart.split(" /priority ", 2);
        if (parts.length == 2) {
            return new String[]{parts[0].trim(), parts[1].trim()};
        }
        return new String[]{commandPart, null};
    }

    /**
     * Converts a priority string to a Priority enum value.
     *
     * @param priorityStr The priority string (high, medium, low, 1, 2, 3)
     * @return The corresponding Priority enum value
     * @throws InvalidCommandFormatException If the priority string is invalid
     */
    private static Task.Priority parsePriority(String priorityStr) throws InvalidCommandFormatException {
        if (priorityStr == null || priorityStr.trim().isEmpty()) {
            return Task.Priority.MEDIUM; // Default priority
        }

        priorityStr = priorityStr.toLowerCase().trim();
        switch (priorityStr) {
        case "high":
        case "1":
            return Task.Priority.HIGH;
        case "medium":
        case "2":
            return Task.Priority.MEDIUM;
        case "low":
        case "3":
            return Task.Priority.LOW;
        default:
            throw new InvalidCommandFormatException(
                    "Invalid priority: " + priorityStr + ". Valid options: high/1, medium/2, low/3");
        }
    }

    /**
     * Parses a 'bye' command.
     *
     * @param command The command object to configure
     */
    private static void parseByeCommand(Command command) {
        command.type = CommandType.BYE;
    }

    /**
     * Parses a 'list' command.
     *
     * @param command The command object to configure
     */
    private static void parseListCommand(Command command) {
        command.type = CommandType.LIST;
    }

    /**
     * Parses a 'mark' command.
     *
     * @param command The command object to configure
     * @param words The command words split from input
     * @throws InvalidCommandFormatException If format is invalid
     */
    private static void parseMarkCommand(Command command, String[] words) throws InvalidCommandFormatException {
        command.type = CommandType.MARK;
        if (words.length < 2) {
            throw new InvalidCommandFormatException(
                    "Invalid format for the 'mark' command. Please specify a task number.");
        }
        command.setParameter(words[1].trim());
    }

    /**
     * Parses an 'unmark' command.
     *
     * @param command The command object to configure
     * @param words The command words split from input
     * @throws InvalidCommandFormatException If format is invalid
     */
    private static void parseUnmarkCommand(Command command, String[] words) throws InvalidCommandFormatException {
        command.type = CommandType.UNMARK;
        if (words.length < 2) {
            throw new InvalidCommandFormatException(
                    "Invalid format for the 'unmark' command. Please specify a task number.");
        }
        command.setParameter(words[1].trim());
    }

    /**
     * Parses a 'todo' command.
     *
     * @param command The command object to configure
     * @param words The command words split from input
     * @throws EmptyDescriptionException If description is empty
     * @throws InvalidCommandFormatException If priority format is invalid
     */
    private static void parseTodoCommand(Command command, String[] words) throws EmptyDescriptionException, InvalidCommandFormatException {
        command.type = CommandType.TODO;
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        String[] priorityParts = extractPriority(words[1]);
        command.setDescription(priorityParts[0]);
        command.setPriority(parsePriority(priorityParts[1]));
    }

    /**
     * Parses a 'deadline' command.
     *
     * @param command The command object to configure
     * @param words The command words split from input
     * @throws EmptyDescriptionException If description is empty
     * @throws InvalidCommandFormatException If format is invalid
     */
    private static void parseDeadlineCommand(Command command, String[] words) throws EmptyDescriptionException, InvalidCommandFormatException {
        command.type = CommandType.DEADLINE;
        if (words.length < 2) {
            throw new EmptyDescriptionException("deadline");
        }
        String[] priorityParts = extractPriority(words[1]);
        String[] deadlineParts = priorityParts[0].split(" /by ", 2);
        if (deadlineParts.length < 2) {
            throw new InvalidCommandFormatException(
                    "Invalid format for the 'deadline' command. Description and due date are required. " +
                    "Format: deadline <description> /by <yyyy-MM-dd HHmm> [/priority <high|medium|low>]");
        }
        command.setDescription(deadlineParts[0].trim());
        command.setParameter(deadlineParts[1].trim());
        command.setPriority(parsePriority(priorityParts[1]));
    }

    /**
     * Parses an 'event' command.
     *
     * @param command The command object to configure
     * @param words The command words split from input
     * @throws EmptyDescriptionException If description is empty
     * @throws InvalidCommandFormatException If format is invalid
     */
    private static void parseEventCommand(Command command, String[] words) throws EmptyDescriptionException, InvalidCommandFormatException {
        command.type = CommandType.EVENT;
        if (words.length < 2) {
            throw new EmptyDescriptionException("event");
        }
        String[] priorityParts = extractPriority(words[1]);
        String[] eventParts = priorityParts[0].split(" /from ", 2);
        if (eventParts.length < 2) {
            throw new InvalidCommandFormatException(
                    "Invalid format for the 'event' command. Description, start and end times are required. " +
                    "Format: event <description> /from <start> /to <end> [/priority <high|medium|low>]");
        }
        String[] fromToParts = eventParts[1].split(" /to ", 2);
        if (fromToParts.length < 2) {
            throw new InvalidCommandFormatException(
                    "Invalid format for the 'event' command. Description, start and end times are required. " +
                    "Format: event <description> /from <start> /to <end> [/priority <high|medium|low>]");
        }
        command.setDescription(eventParts[0].trim());
        command.setParameters(new String[]{fromToParts[0].trim(), fromToParts[1].trim()});
        command.setPriority(parsePriority(priorityParts[1]));
    }

    /**
     * Parses a 'delete' command.
     *
     * @param command The command object to configure
     * @param words The command words split from input
     * @throws InvalidCommandFormatException If format is invalid
     */
    private static void parseDeleteCommand(Command command, String[] words) throws InvalidCommandFormatException {
        command.type = CommandType.DELETE;
        if (words.length < 2) {
            throw new InvalidCommandFormatException(
                    "Invalid format for the 'delete' command. Please specify a task number.");
        }
        command.setParameter(words[1].trim());
    }

    /**
     * Parses a 'find' command.
     *
     * @param command The command object to configure
     * @param words The command words split from input
     * @throws InvalidCommandFormatException If format is invalid
     */
    private static void parseFindCommand(Command command, String[] words) throws InvalidCommandFormatException {
        command.type = CommandType.FIND;
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new InvalidCommandFormatException(
                    "Invalid format for the 'find' command. Please specify a keyword to search for.");
        }
        command.setParameter(words[1].trim());
    }

    /**
     * Parses a 'help' command.
     *
     * @param command The command object to configure
     */
    private static void parseHelpCommand(Command command) {
        command.type = CommandType.HELP;
    }

    /**
     * Parses the user input string into a Command object.
     *
     * @param fullCommand The full user input string
     * @return A Command object representing the parsed command
     * @throws EmptyDescriptionException If a command requires a description but none is provided
     * @throws InvalidCommandFormatException If a command's format is incorrect
     * @throws UnknownCommandException If the command is not recognized
     */
    public static Command parse(String fullCommand) throws EmptyDescriptionException,
            InvalidCommandFormatException, UnknownCommandException {

        fullCommand = fullCommand.trim();
        String[] words = fullCommand.split(" ", 2);
        String commandWord = words[0].toLowerCase();

        Command command = new Command(CommandType.UNKNOWN, fullCommand);

        switch (commandWord) {
            case "bye":
                parseByeCommand(command);
                break;
            case "list":
                parseListCommand(command);
                break;
            case "mark":
                parseMarkCommand(command, words);
                break;
            case "unmark":
                parseUnmarkCommand(command, words);
                break;
            case "todo":
                parseTodoCommand(command, words);
                break;
            case "deadline":
                parseDeadlineCommand(command, words);
                break;
            case "event":
                parseEventCommand(command, words);
                break;
            case "delete":
                parseDeleteCommand(command, words);
                break;
            case "find":
                parseFindCommand(command, words);
                break;
            case "help":
                parseHelpCommand(command);
                break;
            default:
                throw new UnknownCommandException();
        }

        return command;
    }

    /**
     * Executes a 'bye' command.
     */
    private static void executeBye() {
        // Handled in main loop
    }

    /**
     * Executes a 'list' command.
     *
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     */
    private static void executeList(TaskList taskList, Ui ui) {
        ui.showTaskList(taskList);
    }

    /**
     * Executes a 'mark' command.
     *
     * @param command The command with parameter
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     * @throws InvalidTaskNumberException If task number is invalid
     */
    private static void executeMark(Command command, TaskList taskList, Ui ui) throws InvalidTaskNumberException {
        int markTaskNum = Integer.parseInt(command.getParameter());
        Task markedTask = taskList.markTaskAsDone(markTaskNum);
        ui.showMarkUnmarkMessage(markedTask, true);
    }

    /**
     * Executes an 'unmark' command.
     *
     * @param command The command with parameter
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     * @throws InvalidTaskNumberException If task number is invalid
     */
    private static void executeUnmark(Command command, TaskList taskList, Ui ui) throws InvalidTaskNumberException {
        int unmarkTaskNum = Integer.parseInt(command.getParameter());
        Task unmarkedTask = taskList.markTaskAsNotDone(unmarkTaskNum);
        ui.showMarkUnmarkMessage(unmarkedTask, false);
    }

    /**
     * Executes a 'todo' command.
     *
     * @param command The command with description
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     */
    private static void executeTodo(Command command, TaskList taskList, Ui ui) {
        Task.Priority priority = command.getPriority();
        if (priority != null) {
            taskList.addTask(new Todo(command.getDescription(), priority));
        } else {
            taskList.addTask(new Todo(command.getDescription()));
        }
        ui.showTaskAddedMessage(taskList.getLastTask(), taskList.size());
    }

    /**
     * Executes a 'deadline' command.
     *
     * @param command The command with description and parameter
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     * @throws InvalidDateTimeException If date/time format is invalid
     */
    private static void executeDeadline(Command command, TaskList taskList, Ui ui) throws InvalidDateTimeException {
        try {
            Task.Priority priority = command.getPriority();
            if (priority != null) {
                taskList.addTask(new Deadline(command.getDescription(), command.getParameter(), priority));
            } else {
                taskList.addTask(new Deadline(command.getDescription(), command.getParameter()));
            }
            ui.showTaskAddedMessage(taskList.getLastTask(), taskList.size());
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateTimeException(e.getMessage());
        }
    }

    /**
     * Executes an 'event' command.
     *
     * @param command The command with description and parameters
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     * @throws InvalidDateTimeException If date/time format is invalid
     */
    private static void executeEvent(Command command, TaskList taskList, Ui ui) throws InvalidDateTimeException {
        try {
            String[] times = command.getParameters();
            Task.Priority priority = command.getPriority();
            if (priority != null) {
                taskList.addTask(new Event(command.getDescription(), times[0], times[1], priority));
            } else {
                taskList.addTask(new Event(command.getDescription(), times[0], times[1]));
            }
            ui.showTaskAddedMessage(taskList.getLastTask(), taskList.size());
        } catch (java.time.format.DateTimeParseException | IllegalArgumentException e) {
            throw new InvalidDateTimeException(e.getMessage());
        }
    }

    /**
     * Executes a 'delete' command.
     *
     * @param command The command with parameter
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     * @throws InvalidTaskNumberException If task number is invalid
     */
    private static void executeDelete(Command command, TaskList taskList, Ui ui) throws InvalidTaskNumberException {
        int deleteTaskNum = Integer.parseInt(command.getParameter());
        Task deletedTask = taskList.deleteTask(deleteTaskNum);
        ui.showTaskDeletedMessage(deletedTask, taskList.size());
    }

    /**
     * Executes a 'find' command.
     *
     * @param command The command with parameter
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     */
    private static void executeFind(Command command, TaskList taskList, Ui ui) {
        String keyword = command.getParameter();
        ArrayList<Task> matchingTasks = taskList.findTasks(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    /**
     * Executes a 'help' command.
     *
     * @param ui The Ui instance to display messages
     */
    private static void executeHelp(Ui ui) {
        ui.showHelp();
    }

    /**
     * Executes the given command by delegating to the appropriate methods in TaskList and Ui.
     *
     * @param command The command to execute
     * @param taskList The TaskList instance to operate on
     * @param ui The Ui instance to display messages
     * @throws UnknownCommandException If the command type is not recognized
     * @throws InvalidCommandFormatException If the command format is incorrect
     * @throws InvalidTaskNumberException If the task number is invalid
     * @throws InvalidDateTimeException If the date/time format is invalid
     * @throws EmptyDescriptionException If a description is missing
     */
    public static void execute(Command command, TaskList taskList, Ui ui)
            throws UnknownCommandException, InvalidCommandFormatException, InvalidTaskNumberException,
            InvalidDateTimeException, EmptyDescriptionException {

        switch (command.getType()) {
            case BYE:
                executeBye();
                break;
            case LIST:
                executeList(taskList, ui);
                break;
            case MARK:
                executeMark(command, taskList, ui);
                break;
            case UNMARK:
                executeUnmark(command, taskList, ui);
                break;
            case TODO:
                executeTodo(command, taskList, ui);
                break;
            case DEADLINE:
                executeDeadline(command, taskList, ui);
                break;
            case EVENT:
                executeEvent(command, taskList, ui);
                break;
            case DELETE:
                executeDelete(command, taskList, ui);
                break;
            case FIND:
                executeFind(command, taskList, ui);
                break;
            case HELP:
                executeHelp(ui);
                break;
            case UNKNOWN:
            default:
                throw new UnknownCommandException();
        }
    }
}
