/**
 * Handles parsing and interpretation of user commands.
 * Follows Single Responsibility Principle - only handles command parsing operations.
 */
public class Parser {

    /**
     * Represents the different types of commands that can be parsed.
     */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN
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
         * Gets the command type.
         *
         * @return The CommandType enum value
         */
        public CommandType getType() {
            return type;
        }

        /**
         * Gets the full original command string.
         *
         * @return The original command string
         */
        public String getFullCommand() {
            return fullCommand;
        }

        /**
         * Gets the description extracted from the command.
         *
         * @return The description string, or null if not applicable
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the description for this command.
         *
         * @param description The description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Gets a single parameter from the command.
         *
         * @return The parameter string, or null if not applicable
         */
        public String getParameter() {
            return parameter;
        }

        /**
         * Sets a single parameter for this command.
         *
         * @param parameter The parameter to set
         */
        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        /**
         * Gets multiple parameters from the command.
         *
         * @return Array of parameter strings, or null if not applicable
         */
        public String[] getParameters() {
            return parameters;
        }

        /**
         * Sets multiple parameters for this command.
         *
         * @param parameters The array of parameters to set
         */
        public void setParameters(String[] parameters) {
            this.parameters = parameters;
        }
    }

    /**
     * Parses the user input string and returns a Command object with extracted information.
     *
     * @param input The raw user input string
     * @return A Command object containing the parsed command type and parameters
     * @throws EmptyDescriptionException If a command requiring description has empty description
     * @throws InvalidCommandFormatException If the command format is invalid
     * @throws InvalidTaskNumberException If task number is invalid for mark/unmark/delete commands
     */
    public static Command parse(String input) throws EmptyDescriptionException, 
            InvalidCommandFormatException, InvalidTaskNumberException {
        
        String trimmedInput = input.trim();
        Command command;

        if (trimmedInput.equals("bye")) {
            command = new Command(CommandType.BYE, input);
        } else if (trimmedInput.equals("list")) {
            command = new Command(CommandType.LIST, input);
        } else if (trimmedInput.startsWith("mark ")) {
            command = parseMarkUnmarkCommand(input, true);
        } else if (trimmedInput.startsWith("unmark ")) {
            command = parseMarkUnmarkCommand(input, false);
        } else if (trimmedInput.startsWith("todo")) {
            command = parseTodoCommand(input);
        } else if (trimmedInput.startsWith("deadline")) {
            command = parseDeadlineCommand(input);
        } else if (trimmedInput.startsWith("event")) {
            command = parseEventCommand(input);
        } else if (trimmedInput.startsWith("delete")) {
            command = parseDeleteCommand(input);
        } else {
            command = new Command(CommandType.UNKNOWN, input);
        }

        return command;
    }

    /**
     * Parses mark and unmark commands to extract task number.
     *
     * @param input The full command string
     * @param isMark True if this is a mark command, false for unmark
     * @return A Command object with the task number as parameter
     * @throws InvalidTaskNumberException If the task number is invalid
     */
    private static Command parseMarkUnmarkCommand(String input, boolean isMark) 
            throws InvalidTaskNumberException {
        Command command = new Command(isMark ? CommandType.MARK : CommandType.UNMARK, input);
        
        try {
            String taskNumStr = input.substring(isMark ? 5 : 7).trim();
            int taskNum = Integer.parseInt(taskNumStr);
            command.setParameter(String.valueOf(taskNum));
            return command;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidTaskNumberException();
        }
    }

    /**
     * Parses todo command to extract description.
     *
     * @param input The full command string
     * @return A Command object with the todo description
     * @throws EmptyDescriptionException If the todo description is empty
     */
    private static Command parseTodoCommand(String input) throws EmptyDescriptionException {
        if (input.equals("todo")) {
            throw new EmptyDescriptionException("todo");
        }

        Command command = new Command(CommandType.TODO, input);
        String description = extractAndValidateDescription(input, 5, "todo");
        command.setDescription(description);
        return command;
    }

    /**
     * Parses deadline command to extract description and due date/time.
     *
     * @param input The full command string
     * @return A Command object with description and due date as parameters
     * @throws EmptyDescriptionException If description or due date is empty
     * @throws InvalidCommandFormatException If the command format is incorrect
     */
    private static Command parseDeadlineCommand(String input) 
            throws EmptyDescriptionException, InvalidCommandFormatException {
        if (input.equals("deadline")) {
            throw new EmptyDescriptionException("deadline");
        }

        String[] parts = input.split(" /by ");
        if (parts.length != 2) {
            throw new InvalidCommandFormatException("deadline <description> /by <date time>");
        }

        Command command = new Command(CommandType.DEADLINE, input);
        String description = extractAndValidateDescription(parts[0], 9, "deadline");
        String dueDateTime = parts[1].trim();

        if (dueDateTime.isEmpty()) {
            throw new EmptyDescriptionException("deadline due date");
        }

        command.setDescription(description);
        command.setParameter(dueDateTime);
        return command;
    }

    /**
     * Parses event command to extract description, start time, and end time.
     *
     * @param input The full command string
     * @return A Command object with description and time parameters
     * @throws EmptyDescriptionException If description or times are empty
     * @throws InvalidCommandFormatException If the command format is incorrect
     */
    private static Command parseEventCommand(String input) 
            throws EmptyDescriptionException, InvalidCommandFormatException {
        if (input.equals("event")) {
            throw new EmptyDescriptionException("event");
        }

        String[] parts = input.split(" /from ");
        if (parts.length != 2) {
            throw new InvalidCommandFormatException("event <description> /from <start> /to <end>");
        }

        String description = extractAndValidateDescription(parts[0], 6, "event");
        String[] times = parts[1].split(" /to ");

        if (times.length != 2) {
            throw new InvalidCommandFormatException("event <description> /from <start> /to <end>");
        }

        String startTime = times[0].trim();
        String endTime = times[1].trim();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            throw new EmptyDescriptionException("event time");
        }

        Command command = new Command(CommandType.EVENT, input);
        command.setDescription(description);
        command.setParameters(new String[]{startTime, endTime});
        return command;
    }

    /**
     * Parses delete command to extract task number.
     *
     * @param input The full command string
     * @return A Command object with the task number as parameter
     * @throws InvalidTaskNumberException If the task number is invalid
     * @throws InvalidCommandFormatException If the command format is incorrect
     */
    private static Command parseDeleteCommand(String input) 
            throws InvalidTaskNumberException, InvalidCommandFormatException {
        if (input.equals("delete")) {
            throw new InvalidCommandFormatException("delete <task number>");
        }

        Command command = new Command(CommandType.DELETE, input);
        
        try {
            String taskNumStr = input.substring(7).trim();
            int taskNum = Integer.parseInt(taskNumStr);
            command.setParameter(String.valueOf(taskNum));
            return command;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidTaskNumberException();
        }
    }

    /**
     * Extracts and validates description from a command string.
     * Helper method following DRY principle.
     *
     * @param input The full command string
     * @param startIndex The index where description starts
     * @param taskType The type of task for error messaging
     * @return The extracted and validated description
     * @throws EmptyDescriptionException If the description is empty
     */
    private static String extractAndValidateDescription(String input, int startIndex, String taskType)
            throws EmptyDescriptionException {
        String description = input.substring(startIndex).trim();
        if (description.isEmpty()) {
            throw new EmptyDescriptionException(taskType);
        }
        return description;
    }
}