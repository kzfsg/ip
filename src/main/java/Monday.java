import java.util.Scanner;
import java.util.ArrayList;

public class Monday {
    public static void main(String[] args){
        try {
            showWelcome();
            runChatbot();
        } catch (UnknownCommandException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showWelcome() {
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

    private static void runChatbot() throws UnknownCommandException {
        Scanner scanner = new Scanner(System.in);
        String input;

        // Load existing tasks from file at startup
        ArrayList<Task> inputs = Storage.loadTasks();

        if (inputs.size() > 0) {
            System.out.println("Loaded " + inputs.size() + " task(s) from previous session.\n");
        }

        while (true) {
            try {
                input = scanner.nextLine();
                if (input.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!\n");
                    break;
                } else if (input.equals("list")) {
                    displayList(inputs);
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
                System.out.println(e.getMessage());
            }

        }

        scanner.close();
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

            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + deletedTask);
            System.out.println("Now you have " + inputs.size() + " tasks in the list.");

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new InvalidTaskNumberException();
        }
    }

    private static void displayList(ArrayList<Task> inputs) {
        if (inputs.isEmpty()) {
            System.out.println("Your task list is empty.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < inputs.size(); i++) {
                System.out.println((i + 1) + "." + inputs.get(i));
            }
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

            System.out.println((mark ? "Nice! I've marked this task as done:" :
                    "OK, I've marked this task as not done yet:") + "\n  " + task);
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

            System.out.println("Got it. I've added this task:");
            System.out.println("  " + inputs.get(inputs.size() - 1));
            System.out.println("Now you have " + inputs.size() + " tasks in the list.");
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

        System.out.println("Got it. I've added this task:");
        System.out.println("  [T][ ] " + description);
        System.out.println("Now you have " + inputs.size() + " tasks in the list.");
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

            System.out.println("Got it. I've added this task:");
            System.out.println("  " + inputs.get(inputs.size() - 1));
            System.out.println("Now you have " + inputs.size() + " tasks in the list.");
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