package repository;

import model.Account;
import model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    // save new account
    void save(Account account);

    // find account by id
    Optional<Account> findById(UUID id);

    // find accounts by client
    List<Account> findByClient(Client client);

    // find all accounts
    List<Account> findAll();

    // update account
    void delete(UUID id);
}
