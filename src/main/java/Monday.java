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
                try {
                    int taskNum = Integer.parseInt(input.substring(5));
                    markTask(inputs, taskNum - 1);
                    System.out.println("Nice! I've marked this task as done:\n  " + inputs.get(taskNum - 1));
                } catch (Exception e) {
                    System.out.println("Invalid task number.");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskNum = Integer.parseInt(input.substring(7));
                    unmarkTask(inputs, taskNum - 1);
                    System.out.println("OK, I've marked this task as not done yet:\n  " + inputs.get(taskNum - 1));
                } catch (Exception e) {
                    System.out.println("Invalid task number.");
                }
            }
            else {
                addTask(inputs, input);
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

    private static void markTask(ArrayList<Task> inputs, int index) {
        inputs.get(index).markAsDone();
    }

    private static void unmarkTask(ArrayList<Task> inputs, int index) {
        inputs.get(index).markAsNotDone();
    }

    private static void addTask(ArrayList<Task> inputs, String input) {
        System.out.println("added: " + input);
        inputs.add(new Task(input));
    }
}

