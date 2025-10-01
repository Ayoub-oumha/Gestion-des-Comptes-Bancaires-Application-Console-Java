package repository;

import model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    // save new transaction
    void save(Transaction transaction);

    // find transactions by account id
    List<Transaction> findByAccountId(UUID accountId);

    // find all transactions
    List<Transaction> findAll();
}
