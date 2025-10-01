package repository.InMemoryImpl;

import model.Transaction;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryTransactionRepository implements TransactionRepository {
    List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equals(accountId)){
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }

    @Override
    public  List<Transaction> findAll(){
        return transactions;
    }

}
