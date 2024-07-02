package com.ltp.banking_application.entity;

import java.math.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_account")
public class BankAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Account holder name cannot be blank")
    @Column(name = "account_holder_name")
    private String accountHolderName;

    @PositiveOrZero(message = "Please provide a positive amount!")
    @Column(name = "balance")
    private BigDecimal balance;

}
