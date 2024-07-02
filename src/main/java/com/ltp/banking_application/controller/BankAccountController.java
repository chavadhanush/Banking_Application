package com.ltp.banking_application.controller;

import java.math.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ltp.banking_application.entity.BankAccount;
import com.ltp.banking_application.exception.AccountNotFoundException;
import com.ltp.banking_application.exception.InsufficientBalanceException;
import com.ltp.banking_application.service.BankAccountService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Validated
public class BankAccountController {
    
    private final BankAccountService bankAccountService;
    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);

    @GetMapping("/balance/{id}")
    public ResponseEntity<BankAccount> checkBalance(@PathVariable Long id) {
        Optional<BankAccount> account = bankAccountService.checkBalance(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/withdraw/{id}")
    public ResponseEntity<String> withdraw(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        if (request == null || !request.containsKey("amount") || request.get("amount") == null || request.get("amount").compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Invalid withdrawal amount");
        }
        BigDecimal amount = request.get("amount");
        try {
            BankAccount account = bankAccountService.withdraw(id, amount);
            return ResponseEntity.ok("Withdrawal successful. New balance: " + account.getBalance());
        } catch (AccountNotFoundException e) {
            logger.error("Account not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InsufficientBalanceException e) {
            logger.error("Insufficient funds: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<String> deposit(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        if (request == null || !request.containsKey("amount") || request.get("amount") == null || request.get("amount").compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Invalid deposit amount");
        }
        BigDecimal amount = request.get("amount");
        try {
            BankAccount account = bankAccountService.deposit(id, amount);
            return ResponseEntity.ok("Deposit successful. New balance: " + account.getBalance());
        } catch (AccountNotFoundException e) {
            logger.error("Account not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@Valid @RequestBody BankAccount bankAccount, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        
        BankAccount createdAccount = bankAccountService.createUser(bankAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            BankAccount account = bankAccountService.findAccountById(id);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException e) {
            logger.error("Account not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Iterable<BankAccount>> findAllBankAccounts() {
        Iterable<BankAccount> accounts = bankAccountService.getAllBankAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        bankAccountService.deleteAccountById(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).body("Account with " + id + "deleted Succesfully!");
    }
}
