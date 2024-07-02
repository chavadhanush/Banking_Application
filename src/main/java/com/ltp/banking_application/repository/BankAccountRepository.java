package com.ltp.banking_application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ltp.banking_application.entity.BankAccount;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long>{
    
}
