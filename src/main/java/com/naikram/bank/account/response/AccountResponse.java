package com.naikram.bank.account.response;

import com.naikram.bank.account.AccountType;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class AccountResponse {
    private String accountName;
    private Double balance;
    private Long accountNumber;
    private Date createdAt;
    private AccountType accountType;
}
