package service.serviceImpl;

import exceptions.InsufficientBalanceException;
import model.Account;
import model.Client;
import model.Transaction;
import model.enums.TypeTransaction;
import repository.AccountRepository;
import repository.TransactionRepository;
import service.TransactionService;
import util.ValidatorUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void withdraw(Account account, double amount, String description) {
        // Validate account
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        // Validate amount
        if (!ValidatorUtil.isValidWithdrawAmount(amount)) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive");
            } else {
                throw new IllegalArgumentException("Withdrawal amount exceeds limit of 10,000 DH");
            }
        }

        // Check if sufficient balance
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " +
                String.format("%.2f", account.getBalance()) + " DH, Requested: " +
                String.format("%.2f", amount) + " DH");
        }

        // Update account balance
        account.setBalance(account.getBalance() - amount);

        // Create withdrawal transaction
        Transaction withdrawal = new Transaction(
            UUID.randomUUID(),
            TypeTransaction.WITHDRAWAL,
            amount,
            LocalDateTime.now(),
            description != null && !description.trim().isEmpty() ? description : "Withdraw",
            account,
            null // No destination account for withdrawal
        );

        // Save transaction to repository
        transactionRepository.save(withdrawal);

        // Add transaction to account's transaction list
        account.getTransactions().add(withdrawal);
    }

    @Override
    public void deposit(Account account, double amount, String description) {
        // Validate account
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        // Validate amount
        if (!ValidatorUtil.isValidDepositAmount(amount)) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            } else {
                throw new IllegalArgumentException("Deposit amount exceeds limit of 20,000 DH");
            }
        }

        // Update account balance
        account.setBalance(account.getBalance() + amount);

        // Create Deposit transaction
        Transaction deposit = new Transaction(
                UUID.randomUUID(),
                TypeTransaction.DEPOSIT,
                amount,
                LocalDateTime.now(),
                description != null && !description.trim().isEmpty() ? description : "Deposit",
                account,
                null // No destination account for deposit
        );

        // Save transaction to repository
        transactionRepository.save(deposit);

        // Add transaction to account's transaction list
        account.getTransactions().add(deposit);
    }

    @Override
    public void transfer(Account source, Account destination, double amount, String description) {
        // Validate destination account
        if (destination == null) {
            throw new IllegalArgumentException("Destination account cannot be null");
        }

        // Validate amount
        if (!ValidatorUtil.isValidTransferAmount(amount)) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive");
            } else {
                throw new IllegalArgumentException("Transfer amount exceeds limit of 30,000 DH");
            }
        }

        // Check if sufficient balance in source account
        if (source.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " +
                String.format("%.2f", source.getBalance()) + " DH, Requested: " +
                String.format("%.2f", amount) + " DH");
        }

        // Update account balances
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        // Create transfer transaction
        Transaction transfer = new Transaction(
            UUID.randomUUID(),
            TypeTransaction.TRANSFER,
            amount,
            LocalDateTime.now(),
            description != null && !description.trim().isEmpty() ? description : "Transfer",
            source,
            destination
        );

        // Save transaction to repository
        transactionRepository.save(transfer);

        // Add transaction to both accounts' transaction lists
        source.getTransactions().add(transfer);
        destination.getTransactions().add(transfer);
    }

    @Override
    public List<Transaction> getTransactions(Client client) {
        // Get client's primary account
        Account account = client.getAccounts().get(0);

        // Get transactions and sort by date
        List<Transaction> transactions = account.getTransactions();
        transactions.sort((t1, t2) -> t1.getDate().compareTo(t2.getDate()));

        return transactions;
    }

    @Override
    public double totalWithdrawals(Client client) {
        Account account = client.getAccounts().get(0);
        return account.getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.WITHDRAWAL)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double totalDeposits(Client client) {
        Account account = client.getAccounts().get(0);
        return account.getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.DEPOSIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double totalTransfers(Client client) {
        Account account = client.getAccounts().get(0);
        return account.getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.TRANSFER)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public List<Transaction> filterTransactions(Client client, Predicate<Transaction> filter) {
        Account account = client.getAccounts().get(0);
        return account.getTransactions().stream()
                .filter(filter)
                .toList();
    }

    @Override
    public List<Transaction> sortTransactionsByDate(Client client, boolean ascending) {
        Account account = client.getAccounts().get(0);
        return account.getTransactions().stream()
                .sorted(ascending ?
                    (t1, t2) -> t1.getDate().compareTo(t2.getDate()) :
                    (t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .toList();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public double getTotalSystemDeposits() {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.DEPOSIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getTotalSystemWithdrawals() {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.WITHDRAWAL)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getTotalSystemTransfers() {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.TRANSFER)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public int getTotalTransactionCount() {
        return transactionRepository.findAll().size();
    }

    @Override
    public int getDepositCount() {
        return (int) transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.DEPOSIT)
                .count();
    }

    @Override
    public int getWithdrawalCount() {
        return (int) transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.WITHDRAWAL)
                .count();
    }

    @Override
    public int getTransferCount() {
        return (int) transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionType() == TypeTransaction.TRANSFER)
                .count();
    }

    @Override
    public List<Transaction> getSuspiciousTransactions() {
        List<Transaction> suspicious = new java.util.ArrayList<>();

        // Add large amount transactions (>10000 DH)
        suspicious.addAll(getLargeAmountTransactions(10000.0));

        // Add repeated transactions (same type + amount >3 times per client)
        suspicious.addAll(getRepeatedTransactions(3));

        // Remove duplicates and sort by date (newest first)
        return suspicious.stream()
                .distinct()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Transaction> getLargeAmountTransactions(double threshold) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAmount() > threshold)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Transaction> getRepeatedTransactions(int minOccurrences) {
        List<Transaction> allTransactions = transactionRepository.findAll();
        List<Transaction> repeatedTransactions = new java.util.ArrayList<>();

        // Group transactions by client + transaction type + amount
        java.util.Map<String, List<Transaction>> groupedTransactions = allTransactions.stream()
                .filter(t -> t.getSourceAccount() != null && t.getSourceAccount().getClient() != null)
                .collect(java.util.stream.Collectors.groupingBy(t ->
                    t.getSourceAccount().getClient().getClientId() + "|" +
                    t.getTransactionType() + "|" +
                    String.format("%.2f", t.getAmount())
                ));

        // Find groups with more than minOccurrences transactions
        for (List<Transaction> group : groupedTransactions.values()) {
            if (group.size() > minOccurrences) {
                repeatedTransactions.addAll(group);
            }
        }

        return repeatedTransactions;
    }
}
