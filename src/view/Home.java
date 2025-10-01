package view;

import controller.HomeController;
import util.InputUtil;

public class Home {
    // runing
    public static void start() {
        while (true) {
            System.out.println("\n==== Welcome to CIH App ====\n");
            System.out.println("1. Client");
            System.out.println("2. Banker");
            System.out.println("3. Exit");
            int choice = InputUtil.readInt("Please select an option: ");

            if (choice == 1 || choice == 2) {
                handleLogin(choice);
            } else if (choice == 3) {
                System.out.println("Exiting the application. Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleLogin(int choice) {
        System.out.println("\nEnter email and password\n");

        String email = InputUtil.readString("Enter email: ");
        String password = InputUtil.readString("Enter password: ");

        boolean loginSuccess = HomeController.authenticateUser(email, password, choice);

        if (loginSuccess) {
            return;
        }
    }
}
