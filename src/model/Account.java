package model;

import model.enums.TypeAccount;

import java.util.List;
import java.util.UUID;

public class Account {
    UUID accountId;
    TypeAccount accountType;
    double balance;
    List<Transaction> transactions;
    Client client;

    public Account(UUID accountId, TypeAccount accountType, double balance, List<Transaction> transactions, Client client) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.balance = balance;
        this.transactions = transactions;
        this.client = client;
    }

    //Getters
    public UUID getAccountId() {return accountId;}
    public TypeAccount getAccountType() {return accountType;}
    public double getBalance() {return balance;}
    public List<Transaction> getTransactions() {return transactions;}
    public Client getClient() {return client;}

    //Setters
    public void setAccountId(UUID accountId) {this.accountId = accountId;}
    public void setAccountType(TypeAccount accountType) {this.accountType = accountType;}
    public void setBalance(double balance) {this.balance = balance;}
    public void setTransactions(List<Transaction> transactions) {this.transactions = transactions;}
    public void setClient(Client client) {this.client = client;}
}
