package com.example.demo.bankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {


    Optional<BankAccount> findAccountByEmail(String email);
    Optional<BankAccount> findAccountById(Long id);

    Optional <BankAccount> findAccountByPassword(String password);

    Optional <BankAccount> findAccountByName(String name);

    Optional<BankAccount> findAccountByNameAndPassword(String name, String password);
}
