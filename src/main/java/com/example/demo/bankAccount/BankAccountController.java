package com.example.demo.bankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1")

public class BankAccountController {

    private final BankAccountService accountService;
    @Autowired
    public BankAccountController(BankAccountService accountService) {
        this.accountService =  accountService;
    }

    @GetMapping(path = "/accounts")
    public List<BankAccount> getAccounts() {
      return accountService.getAccounts();
    }

    @PostMapping(path = "/accounts")
    public ResponseEntity<BankAccount> registerNewAccount(@RequestBody BankAccount account) {
        BankAccount newAccount = accountService.addNewAccount(account);
        return ResponseEntity.ok().body(newAccount);
    }

    @DeleteMapping(path = "/accounts/{accountId}")
    public void deleteAccount(@PathVariable("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
    }


    @PutMapping(path = "/accounts/deposit/{accountId}")
    public ResponseEntity<BankAccount> depositMoney(
            @PathVariable("accountId") Long accountId,
            @RequestParam("amount") double amount) {
        BankAccount account = accountService.depositMoney(accountId, amount);
        return ResponseEntity.ok().body(account);
    }

    @PutMapping(path = "/accounts/withdrawal/{accountId}")
    public ResponseEntity<BankAccount> withdrawMoney(
            @PathVariable("accountId") Long accountId,
            @RequestParam("amount") double amount) {
        BankAccount account = accountService.withdrawMoney(accountId, amount);
        return ResponseEntity.ok().body(account);
    }

    @PutMapping(path = "/accounts/transfer/{fromAccountId}/{toAccountId}")
    public ResponseEntity<BankAccount> transferMoney(
            @PathVariable("fromAccountId") Long fromAccountId,
            @PathVariable("toAccountId") Long toAccountId,
            @RequestParam("amount") double amount) {
        BankAccount account = accountService.transferMoney(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok().body(account);
    }

    @GetMapping(path = "/accounts/{accountId}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable("accountId") Long accountId) {
        BankAccount account = accountService.getAccountById(accountId);
        if (account != null) {
            return ResponseEntity.ok().body(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/accounts/transactions/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = accountService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/login")
    public ResponseEntity<BankAccount> login(@RequestBody BankAccount loginRequest) {
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();

        Optional<BankAccount> authenticatedAccount = accountService.authenticateUser(name, password);

        if (authenticatedAccount.isPresent()) {
            BankAccount account = authenticatedAccount.get();
            return ResponseEntity.ok().body(account);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

}
