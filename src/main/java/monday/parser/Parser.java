package monday.parser;

import java.util.ArrayList;

import monday.exception.EmptyDescriptionException;
import monday.exception.InvalidCommandFormatException;
import monday.exception.InvalidDateTimeException;
import monday.exception.InvalidTaskNumberException;
import monday.exception.UnknownCommandException;
import monday.task.Task;
import monday.task.TaskList;
import monday.task.Todo;
import monday.task.Deadline;
import monday.task.Event;
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
                command.type = CommandType.BYE;
                break;

            case "list":
                command.type = CommandType.LIST;
                break;

            case "mark":
                command.type = CommandType.MARK;
                if (words.length < 2) {
                    throw new InvalidCommandFormatException("Invalid format for the 'mark' command. Please specify a task number.");
                }
                command.setParameter(words[1].trim());
                break;

            case "unmark":
                command.type = CommandType.UNMARK;
                if (words.length < 2) {
                    throw new InvalidCommandFormatException("Invalid format for the 'unmark' command. Please specify a task number.");
                }
                command.setParameter(words[1].trim());
                break;

            case "todo":
                command.type = CommandType.TODO;
                if (words.length < 2 || words[1].trim().isEmpty()) {
                    throw new EmptyDescriptionException("todo");
                }
                command.setDescription(words[1].trim());
                break;

            case "deadline":
                command.type = CommandType.DEADLINE;
                if (words.length < 2) {
                    throw new EmptyDescriptionException("deadline");
                }
                String[] deadlineParts = words[1].split(" /by ", 2);
                if (deadlineParts.length < 2) {
                    throw new InvalidCommandFormatException("Invalid format for the 'deadline' command. Description and due date are required. Format: deadline <description> /by <yyyy-MM-dd HHmm>");
                }
                command.setDescription(deadlineParts[0].trim());
                command.setParameter(deadlineParts[1].trim());
                break;

            case "event":
                command.type = CommandType.EVENT;
                if (words.length < 2) {
                    throw new EmptyDescriptionException("event");
                }
                String[] eventParts = words[1].split(" /from ", 2);
                if (eventParts.length < 2) {
                    throw new InvalidCommandFormatException("Invalid format for the 'event' command. Description, start and end times are required. Format: event <description> /from <start> /to <end>");
                }
                String[] fromToParts = eventParts[1].split(" /to ", 2);
                if (fromToParts.length < 2) {
                    throw new InvalidCommandFormatException("Invalid format for the 'event' command. Description, start and end times are required. Format: event <description> /from <start> /to <end>");
                }
                command.setDescription(eventParts[0].trim());
                command.setParameters(new String[]{fromToParts[0].trim(), fromToParts[1].trim()});
                break;

            case "delete":
                command.type = CommandType.DELETE;
                if (words.length < 2) {
                    throw new InvalidCommandFormatException("Invalid format for the 'delete' command. Please specify a task number.");
                }
                command.setParameter(words[1].trim());
                break;

            case "find":
                command.type = CommandType.FIND;
                if (words.length < 2 || words[1].trim().isEmpty()) {
                    throw new InvalidCommandFormatException("Invalid format for the 'find' command. Please specify a keyword to search for.");
                }
                command.setParameter(words[1].trim());
                break;

            case "help":
                command.type = CommandType.HELP;
                break;

            default:
                throw new UnknownCommandException();
        }

        return command;
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
    public static void execute(Command command, TaskList taskList, Ui ui) throws UnknownCommandException,
            InvalidCommandFormatException, InvalidTaskNumberException, InvalidDateTimeException, EmptyDescriptionException {

        switch (command.getType()) {
            case BYE:
                // Handled in main loop
                break;

            case LIST:
                ui.showTaskList(taskList);
                break;

            case MARK:
                int markTaskNum = Integer.parseInt(command.getParameter());
                Task markedTask = taskList.markTaskAsDone(markTaskNum);
                ui.showMarkUnmarkMessage(markedTask, true);
                break;

            case UNMARK:
                int unmarkTaskNum = Integer.parseInt(command.getParameter());
                Task unmarkedTask = taskList.markTaskAsNotDone(unmarkTaskNum);
                ui.showMarkUnmarkMessage(unmarkedTask, false);
                break;

            case TODO:
                taskList.addTask(new Todo(command.getDescription()));
                ui.showTaskAddedMessage(taskList.getLastTask(), taskList.size());
                break;

            case DEADLINE:
                try {
                    taskList.addTask(new Deadline(command.getDescription(), command.getParameter()));
                    ui.showTaskAddedMessage(taskList.getLastTask(), taskList.size());
                } catch (java.time.format.DateTimeParseException e) {
                    throw new InvalidDateTimeException(e.getMessage());
                }
                break;

            case EVENT:
                try {
                    String[] times = command.getParameters();
                    taskList.addTask(new Event(command.getDescription(), times[0], times[1]));
                    ui.showTaskAddedMessage(taskList.getLastTask(), taskList.size());
                } catch (java.time.format.DateTimeParseException | IllegalArgumentException e) {
                    throw new InvalidDateTimeException(e.getMessage());
                }
                break;

            case DELETE:
                int deleteTaskNum = Integer.parseInt(command.getParameter());
                Task deletedTask = taskList.deleteTask(deleteTaskNum);
                ui.showTaskDeletedMessage(deletedTask, taskList.size());
                break;

            case FIND:
                String keyword = command.getParameter();
                ArrayList<Task> matchingTasks = taskList.findTasks(keyword);
                ui.showMatchingTasks(matchingTasks);
                break;

            case HELP:
                ui.showHelp();
                break;

            case UNKNOWN:
            default:
                throw new UnknownCommandException();
        }
    }
}
