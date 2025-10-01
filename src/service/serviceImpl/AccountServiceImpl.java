package service.serviceImpl;

import model.Account;
import model.Client;
import model.enums.TypeAccount;
import repository.AccountRepository;
import service.AccountService;

import java.util.ArrayList;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Client client, TypeAccount type, double initialBalance) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }

        if (type == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }

        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        Account newAccount = new Account(
            UUID.randomUUID(),
            type,
            initialBalance,
            new ArrayList<>(),
            client
        );

        accountRepository.save(newAccount);

        client.getAccounts().add(newAccount);

        return newAccount;
    }

    @Override
    public double getTotalSystemBalance() {
        return accountRepository.findAll().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Override
    public double getAverageAccountBalance() {
        int accountCount = getTotalAccountCount();
        if (accountCount == 0) {
            return 0.0;
        }
        return getTotalSystemBalance() / accountCount;
    }

    @Override
    public int getTotalAccountCount() {
        return accountRepository.findAll().size();
    }
}
