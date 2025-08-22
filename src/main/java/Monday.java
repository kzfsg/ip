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
        ArrayList<String> inputs = new ArrayList<>();

        while (true) {
            input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!\n");
                break;
            } else if (input.equals("list")) {
                displayList(inputs);
            } else {
                addTask(inputs, input);
            }
        }

        scanner.close();
    }

    private static void displayList(ArrayList<String> inputs) {
        for (int i = 0; i < inputs.size(); i++) {
            System.out.println(i + ". " + inputs.get(i));
        }
    }

    private static void addTask(ArrayList<String> inputs, String input) {
        System.out.println("added: " + input);
        inputs.add(input);
    }
}