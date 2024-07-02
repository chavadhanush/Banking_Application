package com.ltp.banking_application.service;

import java.math.BigDecimal;
import java.util.*;

import com.ltp.banking_application.entity.BankAccount;

public interface BankAccountService {
    BankAccount createUser(BankAccount bankAccount);
    Optional<BankAccount> checkBalance(Long id);
    BankAccount deposit(Long id, BigDecimal amount);
    BankAccount withdraw(Long id, BigDecimal amount);
    BankAccount findAccountById(Long id);
    Iterable<BankAccount> getAllBankAccounts();
    void deleteAccountById(Long id);
}
