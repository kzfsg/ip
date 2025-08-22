import java.util.Scanner;
import java.util.ArrayList;

public class Monday {
    public static void main(String[] args) {
        showWelcome();
        runChatbot();
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

    private static void runChatbot() {
        Scanner scanner = new Scanner(System.in);
        String input;
        ArrayList<Task> inputs = new ArrayList<>();

        while (true) {
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
            } else if (input.startsWith("todo ")) {
                addTodo(inputs, input);
            } else if (input.startsWith("deadline ")) {
                addDeadline(inputs, input);
            } else if (input.startsWith("event ")) {
                addEvent(inputs, input);
            } else {
                inputs.add(new Task(input));
                System.out.println("added: " + input);
            }
        }

        scanner.close();
    }

    private static void displayList(ArrayList<Task> inputs) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < inputs.size(); i++) {
            System.out.println((i + 1) + "." + inputs.get(i));
        }
    }

    private static void handleMarkUnmark(ArrayList<Task> inputs, String input, boolean mark) {
        try {
            int taskNum = Integer.parseInt(input.substring(mark ? 5 : 7));
            Task task = inputs.get(taskNum - 1);
            if (mark) task.markAsDone(); else task.markAsNotDone();
            System.out.println((mark ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:") + "\n  " + task);
        } catch (Exception e) {
            System.out.println("Invalid task number.");
        }
    }

    private static void addTask(ArrayList<Task> inputs, String input) {
        System.out.println("added: " + input);
        inputs.add(new Task(input));
    }

    private static void addDeadline(ArrayList<Task> inputs, String input) {
        try {
            String[] parts = input.split(" /by ");
            String desc = parts[0].substring(9);
            String dueDate = parts[1];
            inputs.add(new Deadline(desc, dueDate));
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + inputs.get(inputs.size() - 1));
            System.out.println("Now you have " + inputs.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("Format: deadline <description> /by <date>");
        }
    }

    private static void addTodo(ArrayList<Task> inputs, String input) {
        String description = input.substring(5);
        inputs.add(new Todo(description));
        System.out.println("Got it. I've added this task:");
        System.out.println("  [T][ ] " + description);
        System.out.println("Now you have " + inputs.size() + " tasks in the list.");
    }

    private static void addEvent(ArrayList<Task> inputs, String input) {
        try {
            String[] parts = input.split(" /from ");
            String desc = parts[0].substring(6);
            String[] times = parts[1].split(" /to ");
            String startTime = times[0];
            String endTime = times[1];
            inputs.add(new Event(desc, startTime, endTime));
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + inputs.get(inputs.size() - 1));
            System.out.println("Now you have " + inputs.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("Format: event <description> /from <start> /to <end>");
        }
    }

}

