package com.example.demo.bankAccount;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

private final  BankAccountRepository bankAccountRepository;
private final TransactionRepository transactionRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<BankAccount> getAccounts() {
       return bankAccountRepository.findAll();
    }

    public BankAccount addNewAccount(BankAccount account) {
        Optional <BankAccount> accountById =
                bankAccountRepository.findAccountByEmail(account.getEmail());
        if (accountById.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        bankAccountRepository.save(account);

        return account;
    }

    public void deleteAccount(Long accountId) {
        boolean exists = bankAccountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException(
                    "account with id " + accountId + " does not exists");
        }
        bankAccountRepository.deleteById(accountId);
    }

    @Transactional
    public BankAccount depositMoney(Long accountId, double amount) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("Account with id " + accountId + " does not exist"));

        account.deposit(amount);
        Transaction transaction = new Transaction("Deposit", amount, account);
        transactionRepository.save(transaction);
        return bankAccountRepository.save(account);
    }

    @Transactional
    public BankAccount withdrawMoney(Long accountId, double amount) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("Account with id " + accountId + " does not exist"));

        account.withdraw(amount);
        Transaction transaction = new Transaction("Withdrawal", amount, account);
        transactionRepository.save(transaction);
        return bankAccountRepository.save(account);
    }

    @Transactional
    public BankAccount transferMoney(Long fromAccountId, Long toAccountId, double amount) {
        BankAccount fromAccount = bankAccountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalStateException("Account with id " + fromAccountId + " does not exist"));

        BankAccount toAccount = bankAccountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalStateException("Account with id " + toAccountId + " does not exist"));

        fromAccount.transfer(toAccount, amount);
        Transaction transaction = new Transaction("Transfer", amount, fromAccount);
        transactionRepository.save(transaction);
        return bankAccountRepository.save(fromAccount);
    }

    public BankAccount getAccountById(Long accountId) {
        return bankAccountRepository.findById(accountId)
                .orElse(null);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByBankAccount(account);
    }

    public Optional<BankAccount> authenticateUser(String name, String password) {
        return bankAccountRepository.findAccountByNameAndPassword(name, password);
    }

}
