package view;

import controller.HomeController;
import controller.ClientController;
import controller.TransactionController;
import model.Client;
import model.Account;
import util.InputUtil;

public class ClientView {
    
    private final ClientController clientController;
    private final TransactionController transactionController;

    public ClientView(ClientController clientController, TransactionController transactionController) {
        this.clientController = clientController;
        this.transactionController = transactionController;
    }
    
    public void clientView() {
        while(true){
            Client currentClient = (Client) HomeController.getCurrentUser();

            System.out.println("\n===== Client Dashboard =====\n");

            if (currentClient != null) {
                System.out.println("Welcome Mr. " + currentClient.getFirstName() + "\n");

                try {
                    Account primaryAccount = clientController.getPrimaryAccount();
                    System.out.println("•" + primaryAccount.getAccountType() + " Account: " + primaryAccount.getAccountId());
                    System.out.println("•Balance: " + String.format("%.2f", primaryAccount.getBalance()) + " DH");
                    System.out.println("•Total Withdrawals: " + String.format("%.2f", transactionController.getTotalWithdrawals()) + " DH");
                    System.out.println("•Total Deposits: " + String.format("%.2f", transactionController.getTotalDeposits()) + " DH");
                    System.out.println("•Total Transfers: " + String.format("%.2f", transactionController.getTotalTransfers()) + " DH");
                } catch (IllegalStateException e) {
                    System.out.println("•No account information available");
                }
                System.out.println();
            }

            System.out.println("1. Make Transaction");
            System.out.println("2. View Transaction History");
            System.out.println("3. Filter Transactions");
            System.out.println("4. Logout");

            int choice = InputUtil.readInt("Please select an option: ");

            switch(choice) {
                case 1:
                    showTransactionMenu();
                    break;
                case 2:
                    transactionController.viewTransactionHistory();
                    break;
                case 3:
                    transactionController.filterAndSortTransactions();
                    break;
                case 4:
                    controller.HomeController.logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showTransactionMenu() {
        while (true) {
            System.out.println("\n=== Transaction Menu ===\n");
            System.out.println("1. Withdraw");
            System.out.println("2. Deposit");
            System.out.println("3. Transfer");
            System.out.println("4. Back to Main Menu");

            int choice = InputUtil.readInt("Please select transaction type: ");
            System.out.println("\n");

            switch (choice) {
                case 1:
                    transactionController.makeWithdrawal();
                    break;
                case 2:
                    transactionController.makeDeposit();
                    break;
                case 3:
                    transactionController.makeTransfer();
                    break;
                case 4:
                    return; // Go back to main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
