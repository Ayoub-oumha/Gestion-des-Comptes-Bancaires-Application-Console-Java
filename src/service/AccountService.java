package service;

import model.Account;
import model.Client;
import model.enums.TypeAccount;

import java.util.*;

public interface AccountService {
    Account createAccount(Client client, TypeAccount type, double initialBalance);

    // global statistics methods
    double getTotalSystemBalance();

    double getAverageAccountBalance();

    int getTotalAccountCount();
//
//    void closeAccount(UUID accountId);
//
//    void updateAccount(Account account);
//
//    Optional<Account> findAccountById(UUID accountId);
//
//    List<Account> getAccountsByClient(Client client);
//
//    List<Account> listAccounts();
}
