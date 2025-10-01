package view;

import controller.BankerController;
import model.enums.TypeAccount;
import util.InputUtil;

public class BankerView {

    private final BankerController bankerController;

    public BankerView(BankerController bankerController) {
        this.bankerController = bankerController;
    }

    public void bankerView() {
        System.out.println("\n===== Banker Dashboard =====\n");

        while(true){
            System.out.println("1. Manage Clients");
            System.out.println("2. Manage Transactions");
            System.out.println("3. Statistics");
            System.out.println("4. Logout");

            int choice = InputUtil.readInt("\nPlease select an option: ");

            switch(choice) {
                case 1:
                    manageClients();
                    break;
                case 2:
                    manageTransactions();
                    break;
                case 3:
                    statistics();
                    break;
                case 4:
                    controller.HomeController.logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageClients() {
        while (true){
            System.out.println("\n== Manage Clients ==\n");
            System.out.println("1. View All Clients");
            System.out.println("2. Add New Client");
            System.out.println("3. Remove Client");
            System.out.println("4. Back to Banker Dashboard");

            int choice = InputUtil.readInt("\nPlease select an option: ");

            switch(choice) {
                case 1:
                    System.out.println("\n=== All Clients ===\n");
                    bankerController.viewAllClients();
                    break;
                case 2:
                    addNewClient();
                    break;
                case 3:
                    System.out.println("\n=== Remove Client ===\n");
                    bankerController.removeClient();
                    break;
                case 4:
                    System.out.println("\n");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageTransactions() {
        while (true){
            System.out.println("\n== Manage Transactions ==\n");
            System.out.println("1. View All Transactions");
            System.out.println("2. View a Client Transactions");
            System.out.println("3. View Suspicious Transactions");
            System.out.println("4. Back to Banker Dashboard");

            int choice = InputUtil.readInt("\nPlease select an option: ");

            switch(choice) {
                case 1:
                    bankerController.viewAllTransactions();
                    break;
                case 2:
                    bankerController.viewClientTransactions();
                    break;
                case 3:
                    bankerController.viewSuspiciousTransactions();
                    break;
                case 4:
                    System.out.println("\n");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void statistics(){
        bankerController.displayStatistics();
    }

    // ----------------------------------------------

    private void addNewClient() {
        System.out.println("\n=== Add New Client ===\n");

        // Collect personal information
        String firstName = InputUtil.readString("Enter first name");
        String lastName = InputUtil.readString("Enter last name");
        String email = InputUtil.readString("Enter email");
        String password = InputUtil.readString("Enter password");

        // Collect account information
        System.out.println("\nAccount Information:\n");

        TypeAccount accountType;
        while (true) {
            System.out.println("Account Type:");
            System.out.println("1. CHECKING");
            System.out.println("2. SAVINGS");
            System.out.println("3. TERM_DEPOSIT");

            int accountTypeChoice = InputUtil.readInt("Select account type (1-3)");

            switch (accountTypeChoice) {
                case 1:
                    accountType = TypeAccount.CHECKING;
                    break;
                case 2:
                    accountType = TypeAccount.SAVINGS;
                    break;
                case 3:
                    accountType = TypeAccount.TERM_DEPOSIT;
                    break;
                default:
                    System.out.println("Invalid selection. Please select 1, 2, or 3.\n");
                    continue;
            }
            break; // Exit loop when valid selection is made
        }

        double initialBalance = InputUtil.readDouble("Enter initial balance");

        bankerController.addNewClientWithAccount(firstName, lastName, email, password, accountType, initialBalance);
    }
}
