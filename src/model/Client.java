package model;

import model.enums.Role;

import java.util.List;
import java.util.UUID;

public class Client extends Person {
    UUID clientId;
    List<Account> accounts;

    public Client(String firstName, String lastName, String email, String password, UUID clientId, List<Account> accounts) {
        super(firstName, lastName, email, password, Role.CLIENT);
        this.clientId = clientId;
        this.accounts = accounts;
    }

    //Getters
    public UUID getClientId() {return clientId;}
    public List<Account> getAccounts() {return accounts;}

    //Setters
    public void setClientId(UUID clientId) {this.clientId = clientId;}
    public void setAccounts(List<Account> accounts) {this.accounts = accounts;}
}
