package model;

import model.enums.TypeTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Transaction {
    UUID transactionId;
    TypeTransaction transactionType;
    double amount;
    LocalDateTime date;
    String description;
    Account sourceAccount;
    Account destinationAccount;

    public Transaction(UUID transactionId, TypeTransaction transactionType, double amount, LocalDateTime date, String description, Account sourceAccount, Account destinationAccount) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    //Getters
    public UUID getTransactionId() {return transactionId;}
    public TypeTransaction getTransactionType() {return transactionType;}
    public double getAmount() {return amount;}
    public LocalDateTime getDate() {return date;}
    public String getDescription() {return description;}
    public Account getSourceAccount() {return sourceAccount;}
    public Account getDestinationAccount() {return destinationAccount;}

    //Setters
    public void setTransactionId(UUID transactionId) {this.transactionId = transactionId;}
    public void setTransactionType(TypeTransaction transactionType) {this.transactionType = transactionType;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setDate(LocalDateTime date) {this.date = date;}
    public void setDescription(String description) {this.description = description;}
    public void setSourceAccount(Account sourceAccount) {this.sourceAccount = sourceAccount;}
    public void setDestinationAccount(Account destinationAccount) {this.destinationAccount = destinationAccount;}
}
