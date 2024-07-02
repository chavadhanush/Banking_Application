package com.ltp.banking_application.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ltp.banking_application.entity.BankAccount;
import com.ltp.banking_application.exception.AccountNotFoundException;
import com.ltp.banking_application.exception.InsufficientBalanceException;
import com.ltp.banking_application.repository.BankAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<BankAccount> checkBalance(Long id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    @Transactional
    public BankAccount deposit(Long id, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account doesn't exist"));

        BigDecimal total = account.getBalance().add(amount);
        account.setBalance(total);

        return bankAccountRepository.save(account);
    }

    @Override
    @Transactional
    public BankAccount withdraw(Long accountId, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        return bankAccountRepository.save(account);
    }

    @Override
    public BankAccount findAccountById(Long id) {
        return bankAccountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " does not exist"));
    }

    @Override
    public Iterable<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    @Transactional
    public BankAccount createUser(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public void deleteAccountById(Long id) {
        BankAccount account = bankAccountRepository.findById(id)
        .orElseThrow(() -> new EmptyResultDataAccessException("Account with ID " + id + " does not exist", 1));
        bankAccountRepository.delete(account);
    }
}
