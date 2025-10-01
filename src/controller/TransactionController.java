package controller;

import controller.HomeController;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidFilterException;
import model.Account;
import model.Client;
import model.Transaction;
import model.enums.TypeTransaction;
import service.ClientService;
import service.TransactionService;
import util.DateUtil;
import util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TransactionController {

    private final TransactionService transactionService;
    private final ClientService clientService;

    public TransactionController(TransactionService transactionService, ClientService clientService) {
        this.transactionService = transactionService;
        this.clientService = clientService;
    }

    // Helper method to get client first found account
    public Account getPrimaryAccount() {
        Client currentClient = (Client) HomeController.getCurrentUser();
        // Assumes client has at least one account - returns the first account
        return currentClient.getAccounts().get(0);
    }

    public void makeWithdrawal() {
        try {
            Account account = getPrimaryAccount();
            System.out.println("\n=== Make Withdrawal ===\n");
            System.out.println("Current Balance: " + String.format("%.2f", account.getBalance()) + " DH");

            double amount = InputUtil.readDouble("Enter withdrawal amount");
            String description = InputUtil.readString("Enter description (optional)");

            transactionService.withdraw(account, amount, description);

            System.out.println("\nWithdrawal successful!");
            System.out.println("Amount withdrawn: " + String.format("%.2f", amount) + " DH");
            System.out.println("New Balance: " + String.format("%.2f", account.getBalance()) + " DH");

        } catch (InsufficientBalanceException e) {
            System.out.println("\n" + e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred: " + e.getMessage());
        }
    }

    public void makeDeposit() {
        try {
            Account account = getPrimaryAccount();
            System.out.println("\n=== Make Deposit ===\n");
            System.out.println("Current Balance: " + String.format("%.2f", account.getBalance()) + " DH");

            double amount = InputUtil.readDouble("Enter Deposit amount");
            String description = InputUtil.readString("Enter description (optional)");

            transactionService.deposit(account, amount, description);

            System.out.println("\nDeposit successful!");
            System.out.println("Amount deposited: " + String.format("%.2f", amount) + " DH");
            System.out.println("New Balance: " + String.format("%.2f", account.getBalance()) + " DH");

        } catch (InsufficientBalanceException e) {
            System.out.println("\n" + e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred: " + e.getMessage());
        }
    }

    public void makeTransfer() {
        try {
            Account sourceAccount = getPrimaryAccount();
            System.out.println("\n=== Make Transfer ===\n");
            System.out.println("Current Balance: " + String.format("%.2f", sourceAccount.getBalance()) + " DH");

            Client selectedClient = selectTransferRecipient();
            if (selectedClient == null) return;

            Account destinationAccount = selectedClient.getAccounts().get(0);

            double amount = InputUtil.readDouble("Enter transfer amount: ");
            String description = InputUtil.readString("Enter description (optional): ");

            transactionService.transfer(sourceAccount, destinationAccount, amount, description);

            System.out.println("\nTransfer successful!");
            System.out.println("Amount transferred: " + String.format("%.2f", amount) + " DH");
            System.out.println("To: " + selectedClient.getFirstName() + " " + selectedClient.getLastName());
            System.out.println("New Balance: " + String.format("%.2f", sourceAccount.getBalance()) + " DH");

        } catch (InsufficientBalanceException e) {
            System.out.println("\n" + e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred: " + e.getMessage());
        }
    }

    private Client selectTransferRecipient() {
        // Get all clients from the system
        List<Client> allClients = new ArrayList<>(clientService.listClients()); // Create a copy
        Client currentClient = (Client) HomeController.getCurrentUser();

        // Remove current client from the list to prevent self-transfer
        allClients.removeIf(client -> client.getClientId().equals(currentClient.getClientId()));

        if (allClients.isEmpty()) {
            System.out.println("No other clients available for transfer.");
            return null;
        }

        System.out.println("\nAvailable clients for transfer:");
        System.out.println("================================");
        // Display numbered list of available clients with their details
        for (int i = 0; i < allClients.size(); i++) {
            Client client = allClients.get(i);
            String fullName = client.getFirstName() + " " + client.getLastName();
            System.out.printf("%d. %s (%s)%n", i + 1, fullName, client.getEmail());
        }

        int clientChoice = InputUtil.readInt("\nSelect recipient (enter number): ");

        // Validate user selection is within valid range
        if (clientChoice < 1 || clientChoice > allClients.size()) {
            System.out.println("Invalid selection.");
            return null;
        }

        // Convert 1-based user input to 0-based array index
        return allClients.get(clientChoice - 1);
    }


    // Displays all transactions for the current client without any filters
    public void viewTransactionHistory() {
        try {
            Client currentClient = (Client) HomeController.getCurrentUser();
            List<Transaction> transactions = transactionService.getTransactions(currentClient);

            displayTransactions(transactions, "Transaction History", null);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Helper method to display filtered/sorted transactions
     * @param transactions The filtered/sorted transaction list to display
     * @param filterDescription Description of the applied filter/sort
     */
    private void displayFilteredTransactions(List<Transaction> transactions, String filterDescription) {
        displayTransactions(transactions, "Filtered/Sorted Transaction History", filterDescription);
    }

    /**
     * Common method for displaying transaction tables with consistent formatting
     * @param transactions List of transactions to display
     * @param title Header title for the table
     * @param filterDescription Optional description of applied filters (null for unfiltered)
     */
    private void displayTransactions(List<Transaction> transactions, String title, String filterDescription) {
        System.out.println("\n=== " + title + " ===");

        // Show filter/sort information if provided
        if (filterDescription != null) {
            System.out.println("Filter/Sort: " + filterDescription);
        }

        if (transactions.isEmpty()) {
            System.out.println("No transactions found" + (filterDescription != null ? " matching the criteria." : "."));
            return;
        }

        System.out.println("\n# | Type       | Amount     | Recipient            | Description           | Date");
        System.out.println("--|------------|------------|----------------------|-----------------------|--------------------");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            // Determine recipient based on transaction type
            String recipient = "-";
            if (t.getTransactionType() == TypeTransaction.TRANSFER) {
                if (t.getDestinationAccount() != null) {
                    Client recipientClient = t.getDestinationAccount().getClient();
                    if (recipientClient != null) {
                        recipient = recipientClient.getFirstName() + " " + recipientClient.getLastName();
                        if (recipient.length() > 20) {
                            recipient = recipient.substring(0, 17) + "...";
                        }
                    }
                }
            }

            System.out.printf("%-2d| %-10s | %10.2f | %-20s | %-21s | %s%n",
                i + 1, // Row number (1-based)
                t.getTransactionType(),
                t.getAmount(),
                recipient,
                t.getDescription().length() > 21 ? t.getDescription().substring(0, 18) + "..." : t.getDescription(),
                DateUtil.formatDateTime(t.getDate())
            );
        }

        // Show total count for filtered results
        if (filterDescription != null) {
            System.out.println("\nTotal transactions displayed: " + transactions.size());
        }
    }

    public double getTotalWithdrawals() {
        Client currentClient = (Client) HomeController.getCurrentUser();
        return transactionService.totalWithdrawals(currentClient);
    }

    public double getTotalDeposits() {
        Client currentClient = (Client) HomeController.getCurrentUser();
        return transactionService.totalDeposits(currentClient);
    }

    public double getTotalTransfers() {
        Client currentClient = (Client) HomeController.getCurrentUser();
        return transactionService.totalTransfers(currentClient);
    }


    public void filterAndSortTransactions() {
        try {
            System.out.println("\nFilter Transactions");
            System.out.println("1. Filter by Type");
            System.out.println("2. Filter by Amount");
            System.out.println("3. Sort by Date");

            int choice = InputUtil.readInt("Choose option: ");

            switch (choice) {
                case 1:
                    filterByType();
                    break;
                case 2:
                    filterByAmount();
                    break;
                case 3:
                    sortByDate();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void filterByType() {
        try {
            System.out.println("Choose Transaction Type:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdrawal");
            System.out.println("3. Transfer");

            int typeChoice = InputUtil.readInt("Enter choice: ");

            TypeTransaction selectedType;
            switch (typeChoice) {
                case 1:
                    selectedType = TypeTransaction.DEPOSIT;
                    break;
                case 2:
                    selectedType = TypeTransaction.WITHDRAWAL;
                    break;
                case 3:
                    selectedType = TypeTransaction.TRANSFER;
                    break;
                default:
                    throw new InvalidFilterException("Invalid transaction type selection.");
            }

            Client currentClient = (Client) HomeController.getCurrentUser();
            // Create Predicate lambda to filter by transaction type
            Predicate<Transaction> typeFilter = transaction -> transaction.getTransactionType() == selectedType;
            List<Transaction> filteredTransactions = transactionService.filterTransactions(currentClient, typeFilter);

            displayFilteredTransactions(filteredTransactions, "Type: " + selectedType);

        } catch (InvalidFilterException e) {
            System.out.println("Filter Error: " + e.getMessage());
        }
    }


    private void filterByAmount() {
        try {
            double min = InputUtil.readDouble("Enter minimum amount: ");
            double max = InputUtil.readDouble("Enter maximum amount: ");

            if (min < 0 || max < 0) {
                throw new InvalidFilterException("Amount values cannot be negative.");
            }

            if (min > max) {
                throw new InvalidFilterException("Minimum amount cannot be greater than maximum amount.");
            }

            Client currentClient = (Client) HomeController.getCurrentUser();
            // Create Predicate lambda to filter by amount range (inclusive)
            Predicate<Transaction> amountFilter = transaction ->
                transaction.getAmount() >= min && transaction.getAmount() <= max;
            List<Transaction> filteredTransactions = transactionService.filterTransactions(currentClient, amountFilter);

            displayFilteredTransactions(filteredTransactions, "Amount: " + String.format("%.2f", min) + " - " + String.format("%.2f", max) + " DH");

        } catch (InvalidFilterException e) {
            System.out.println("Filter Error: " + e.getMessage());
        }
    }


    private void sortByDate() {
        try {
            System.out.println("1. Old to New");
            System.out.println("2. New to Old");

            int dateChoice = InputUtil.readInt("Choose: ");

            boolean ascending;
            String sortOrder;
            switch (dateChoice) {
                case 1:
                    ascending = true;  // Chronological order (oldest first)
                    sortOrder = "Old to New";
                    break;
                case 2:
                    ascending = false; // Reverse chronological order (newest first)
                    sortOrder = "New to Old";
                    break;
                default:
                    throw new InvalidFilterException("Invalid date sort selection.");
            }

            Client currentClient = (Client) HomeController.getCurrentUser();
            // Use service method that applies Comparator internally
            List<Transaction> sortedTransactions = transactionService.sortTransactionsByDate(currentClient, ascending);

            displayFilteredTransactions(sortedTransactions, "Sorted by Date: " + sortOrder);

        } catch (InvalidFilterException e) {
            System.out.println("Sort Error: " + e.getMessage());
        }
    }
}
