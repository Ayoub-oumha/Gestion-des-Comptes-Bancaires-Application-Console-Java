package controller;

import model.Account;
import model.Client;
import model.Transaction;
import model.enums.TypeAccount;
import service.AccountService;
import service.ClientService;
import service.TransactionService;

import java.util.List;

public class BankerController {

    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public BankerController(ClientService clientService, AccountService accountService, TransactionService transactionService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void addNewClientWithAccount(String firstName, String lastName, String email, String password,
                                          TypeAccount accountType, double initialBalance) {
        try {
            // Create new client first
            Client newClient = clientService.createClient(firstName, lastName, email, password);

            // Create new account for the client
            Account newAccount = accountService.createAccount(newClient, accountType, initialBalance);

            System.out.println("\nClient and Account created successfully!");
            System.out.println("\nClient Details:");
            System.out.println("  Name: " + newClient.getFirstName() + " " + newClient.getLastName());
            System.out.println("  Email: " + newClient.getEmail());
            System.out.println("  Password: " + newClient.getPassword());

            System.out.println("\nAccount Details:");
            System.out.println("  Account Type: " + newAccount.getAccountType());
            System.out.println("  Initial Balance: DH" + String.format("%.2f", newAccount.getBalance()));

        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred: " + e.getMessage());
        }
    }

    public void viewAllClients() {
        List<Client> clients = clientService.listClients();

        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            System.out.printf("%-5s %-15s %-15s %-30s %-36s%n", "#", "First Name", "Last Name", "Email", "Client ID");
            System.out.println("─".repeat(105));

            for (int i = 0; i < clients.size(); i++) {
                Client client = clients.get(i);
                System.out.printf("%-5d %-15s %-15s %-30s %-36s%n",
                    (i + 1),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getEmail(),
                    client.getClientId()
                );
            }
            System.out.println("─".repeat(105));
            System.out.println("\nTotal clients: " + clients.size());
        }
    }

    public void removeClient() {
        System.out.println("\n=== Remove Client ===\n");

        List<Client> clients = clientService.listClients();

        viewAllClients();

        System.out.println("\n0. Cancel");

        int choice = util.InputUtil.readInt("\nSelect client to remove (0 to cancel)");

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 1 || choice > clients.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        // Convert choice to array index (choice - 1, index starts at 0)
        Client selectedClient = clients.get(choice - 1);

        try {
            clientService.deleteClient(selectedClient.getClientId());
            System.out.println("\nClient '" + selectedClient.getFirstName() + " " +
                selectedClient.getLastName() + "' has been successfully removed.");
        } catch (Exception e) {
            System.out.println("\nError removing client: " + e.getMessage());
        }
    }


    // display system statistics for the banker
    public void displayStatistics() {
        try {
            System.out.println("\n=== Bank System Statistics ===");
            System.out.println("=" .repeat(50));

            // Client Statistics
            int totalClients = clientService.listClients().size();
            System.out.println("\n•CLIENT STATISTICS:");
            System.out.println("   Total Clients: " + totalClients);

            // Account Statistics
            int totalAccounts = accountService.getTotalAccountCount();
            double totalSystemBalance = accountService.getTotalSystemBalance();
            double averageBalance = accountService.getAverageAccountBalance();

            System.out.println("\n•ACCOUNT STATISTICS:");
            System.out.println("   Total Accounts: " + totalAccounts);
            System.out.println("   Total System Balance: " + String.format("%.2f DH", totalSystemBalance));
            System.out.println("   Average Account Balance: " + String.format("%.2f DH", averageBalance));

            // Transaction Statistics - Counts
            int depositCount = transactionService.getDepositCount();
            int withdrawalCount = transactionService.getWithdrawalCount();
            int transferCount = transactionService.getTransferCount();
            int totalTransactions = transactionService.getTotalTransactionCount();

            System.out.println("\n•TRANSACTION COUNTS:");
            System.out.println("   Number of Deposits: " + depositCount);
            System.out.println("   Number of Withdrawals: " + withdrawalCount);
            System.out.println("   Number of Transfers: " + transferCount);
            System.out.println("   Total Transactions: " + totalTransactions);

            // Transaction Statistics - Amounts
            double totalDeposits = transactionService.getTotalSystemDeposits();
            double totalWithdrawals = transactionService.getTotalSystemWithdrawals();
            double totalTransfers = transactionService.getTotalSystemTransfers();

            System.out.println("\n•TRANSACTION AMOUNTS:");
            System.out.println("   Total Deposits: " + String.format("%.2f DH", totalDeposits));
            System.out.println("   Total Withdrawals: " + String.format("%.2f DH", totalWithdrawals));
            System.out.println("   Total Transfers: " + String.format("%.2f DH", totalTransfers));

            System.out.println("\n" + "=" .repeat(50));

        } catch (Exception e) {
            System.out.println("Error retrieving statistics: " + e.getMessage());
        }
    }


    // Display all transactions in the system
    public void viewAllTransactions() {
        try {
            List<Transaction> allTransactions = transactionService.getAllTransactions();

            System.out.println("\n=== All System Transactions ===");

            if (allTransactions.isEmpty()) {
                System.out.println("No transactions found in the system.");
                return;
            }

            // Sort transactions by date (newest first)
            allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

            System.out.println("\n# | Type       | Amount     | Client               | Recipient            | Description           | Date");
            System.out.println("--|------------|------------|----------------------|----------------------|-----------------------|--------------------");

            for (int i = 0; i < allTransactions.size(); i++) {
                Transaction t = allTransactions.get(i);

                // Get client name from source account
                String clientName = "-";
                if (t.getSourceAccount() != null && t.getSourceAccount().getClient() != null) {
                    Client client = t.getSourceAccount().getClient();
                    clientName = client.getFirstName() + " " + client.getLastName();
                    if (clientName.length() > 20) {
                        clientName = clientName.substring(0, 17) + "...";
                    }
                }

                // Get recipient name for transfers
                String recipient = "-";
                if (t.getTransactionType().toString().equals("TRANSFER")) {
                    if (t.getDestinationAccount() != null && t.getDestinationAccount().getClient() != null) {
                        Client recipientClient = t.getDestinationAccount().getClient();
                        recipient = recipientClient.getFirstName() + " " + recipientClient.getLastName();
                        if (recipient.length() > 20) {
                            recipient = recipient.substring(0, 17) + "...";
                        }
                    }
                }

                System.out.printf("%-2d| %-10s | %10.2f | %-20s | %-20s | %-21s | %s%n",
                    i + 1,
                    t.getTransactionType(),
                    t.getAmount(),
                    clientName,
                    recipient,
                    t.getDescription().length() > 21 ? t.getDescription().substring(0, 18) + "..." : t.getDescription(),
                    util.DateUtil.formatDateTime(t.getDate())
                );
            }

            System.out.println("\nTotal transactions: " + allTransactions.size());

        } catch (Exception e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
    }

    // Display transactions for a specific client selected by the banker
    public void viewClientTransactions() {
        try {
            List<Client> clients = clientService.listClients();

            if (clients.isEmpty()) {
                System.out.println("No clients found in the system.");
                return;
            }

            System.out.println("\n=== Select Client to View Transactions ===");
            System.out.println("\nAvailable Clients:");
            System.out.printf("%-5s %-15s %-15s %-30s%n", "#", "First Name", "Last Name", "Email");
            System.out.println("─".repeat(70));

            for (int i = 0; i < clients.size(); i++) {
                Client client = clients.get(i);
                System.out.printf("%-5d %-15s %-15s %-30s%n",
                    i + 1,
                    client.getFirstName(),
                    client.getLastName(),
                    client.getEmail()
                );
            }

            System.out.println("\n0. Cancel");

            int choice = util.InputUtil.readInt("\nSelect client (0 to cancel): ");

            if (choice == 0) {
                System.out.println("Operation cancelled.");
                return;
            }

            if (choice < 1 || choice > clients.size()) {
                System.out.println("Invalid selection. Please try again.");
                return;
            }

            // Get selected client
            Client selectedClient = clients.get(choice - 1);
            List<Transaction> clientTransactions = transactionService.getTransactions(selectedClient);

            System.out.println("\n=== Transactions for " + selectedClient.getFirstName() + " " +
                selectedClient.getLastName() + " ===");

            if (clientTransactions.isEmpty()) {
                System.out.println("No transactions found for this client.");
                return;
            }

            System.out.println("\n# | Type       | Amount     | Recipient            | Description           | Date");
            System.out.println("--|------------|------------|----------------------|-----------------------|--------------------");

            for (int i = 0; i < clientTransactions.size(); i++) {
                Transaction t = clientTransactions.get(i);

                // Get recipient for transfers
                String recipient = "-";
                if (t.getTransactionType().toString().equals("TRANSFER")) {
                    if (t.getDestinationAccount() != null && t.getDestinationAccount().getClient() != null) {
                        Client recipientClient = t.getDestinationAccount().getClient();
                        recipient = recipientClient.getFirstName() + " " + recipientClient.getLastName();
                        if (recipient.length() > 20) {
                            recipient = recipient.substring(0, 17) + "...";
                        }
                    }
                }

                System.out.printf("%-2d| %-10s | %10.2f | %-20s | %-21s | %s%n",
                    i + 1,
                    t.getTransactionType(),
                    t.getAmount(),
                    recipient,
                    t.getDescription().length() > 21 ? t.getDescription().substring(0, 18) + "..." : t.getDescription(),
                    util.DateUtil.formatDateTime(t.getDate())
                );
            }

            System.out.println("\nTotal transactions for this client: " + clientTransactions.size());

            // Show client account balance
            if (!selectedClient.getAccounts().isEmpty()) {
                double balance = selectedClient.getAccounts().get(0).getBalance();
                System.out.println("Current account balance: " + String.format("%.2f DH", balance));
            }

        } catch (Exception e) {
            System.out.println("Error retrieving client transactions: " + e.getMessage());
        }
    }

    // Display suspicious transactions with categorization
    public void viewSuspiciousTransactions() {
        try {
            List<Transaction> suspiciousTransactions = transactionService.getSuspiciousTransactions();
            List<Transaction> largeAmountTransactions = transactionService.getLargeAmountTransactions(10000.0);
            List<Transaction> repeatedTransactions = transactionService.getRepeatedTransactions(3);

            System.out.println("\n=== Suspicious Transactions Report ===");

            if (suspiciousTransactions.isEmpty()) {
                System.out.println("\nNo suspicious transactions found.");
                return;
            }

            System.out.println("\n# | Type       | Amount     | Client               | Recipient            | Description           | Date                | Reason");
            System.out.println("--|------------|------------|----------------------|----------------------|-----------------------|---------------------|------------------");

            for (int i = 0; i < suspiciousTransactions.size(); i++) {
                Transaction t = suspiciousTransactions.get(i);

                // Get client name from source account
                String clientName = "-";
                if (t.getSourceAccount() != null && t.getSourceAccount().getClient() != null) {
                    Client client = t.getSourceAccount().getClient();
                    clientName = client.getFirstName() + " " + client.getLastName();
                    if (clientName.length() > 20) {
                        clientName = clientName.substring(0, 17) + "...";
                    }
                }

                // Get recipient name for transfers
                String recipient = "-";
                if (t.getTransactionType().toString().equals("TRANSFER")) {
                    if (t.getDestinationAccount() != null && t.getDestinationAccount().getClient() != null) {
                        Client recipientClient = t.getDestinationAccount().getClient();
                        recipient = recipientClient.getFirstName() + " " + recipientClient.getLastName();
                        if (recipient.length() > 20) {
                            recipient = recipient.substring(0, 17) + "...";
                        }
                    }
                }

                // Determine reason for being suspicious
                String reason = "";
                if (largeAmountTransactions.contains(t)) {
                    reason = "Large Amount";
                }
                if (repeatedTransactions.contains(t)) {
                    if (!reason.isEmpty()) {
                        reason += " + Repeated";
                    } else {
                        reason = "Repeated Pattern";
                    }
                }

                System.out.printf("%-2d| %-10s | %10.2f | %-20s | %-20s | %-21s | %-19s | %s%n",
                    i + 1,
                    t.getTransactionType(),
                    t.getAmount(),
                    clientName,
                    recipient,
                    t.getDescription().length() > 21 ? t.getDescription().substring(0, 18) + "..." : t.getDescription(),
                    util.DateUtil.formatDateTime(t.getDate()),
                    reason
                );
            }

            System.out.println("\nSuspicious Transaction Summary:");
            System.out.println("  • Large Amount (>10,000 DH): " + largeAmountTransactions.size() + " transactions");
            System.out.println("  • Repeated Patterns (>3 times): " + repeatedTransactions.size() + " transactions");
            System.out.println("  • Total Suspicious: " + suspiciousTransactions.size() + " transactions");

        } catch (Exception e) {
            System.out.println("Error retrieving suspicious transactions: " + e.getMessage());
        }
    }
}
