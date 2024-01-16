package com.naikram.bank.account.requests;

import com.naikram.bank.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    private String accountName;
    private AccountType accountType;
}
