package repository.InMemoryImpl;

import model.Account;
import repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {
    List<Account> accounts = new ArrayList<>();

    @Override
    public void save(Account account){
        accounts.add(account);
    }

    @Override
    public Optional<Account> findById(UUID id){
        for (Account account : accounts){
            if (account.getAccountId().equals(id)){
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findByClient(model.Client client){
        List<Account> clientAccounts = new ArrayList<>();
        for (Account account : accounts){
            if (account.getClient().equals(client)){
                clientAccounts.add(account);
            }
        }
        return clientAccounts;
    }

    @Override
    public List<Account> findAll() {
        return accounts;
    }

    @Override
    public void delete(UUID id) {
        accounts.removeIf(account -> account.getAccountId().equals(id));
    }
}
