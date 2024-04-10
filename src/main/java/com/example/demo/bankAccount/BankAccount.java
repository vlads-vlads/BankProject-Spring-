package com.example.demo.bankAccount;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Entity
@Table
public class BankAccount {
    private static final Logger logger = LogManager.getLogger(BankAccount.class);
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private long id;
    private String name;
    private String email;
    private double balance;

    private String password;

    public BankAccount() {
    }
    public BankAccount(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public BankAccount(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            logger.info("Deposit successful. New balance: {}", balance);
        } else {
            logger.error("Invalid amount for deposit.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= getBalance()) {
            balance -= amount;
            logger.info("Withdrawal successful. New balance: {}", balance);
        } else {
            logger.error("Invalid amount for withdrawal or insufficient balance.");
        }
    }

    public void transfer(BankAccount recipient, double amount) {
        if (this.getId() != recipient.getId() && amount > 0 && amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            logger.info("Transfer successful. Amount transferred: {}", amount);
        } else {
            logger.error("Invalid amount for transfer or insufficient balance.");
        }
    }
}
