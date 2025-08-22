import java.util.Scanner;

public class Monday {
    public static void main(String[] args) {
        String logo = " __  __                 _             \n"
                + "|  \\/  |               | |            \n"
                + "| \\  / | ___  _ __   __| | __ _ _   _  \n"
                + "| |\\/| |/ _ \\| '_ \\ / _` |/ _` | | | | \n"
                + "| |  | | (_) | | | | (_| | (_| | |_| | \n"
                + "|_|  |_|\\___/|_| |_|\\__,_|\\__,_|\\__, | \n"
                + "                                __/ | \n"
                + "                               |___/  \n";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello I'm\n" + logo);
        System.out.println("What can I do for you?\n");

        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!\n");
                break;
            } else {
                System.out.println(" " + input);
            }
        }
    }
}