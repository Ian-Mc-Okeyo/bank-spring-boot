package com.naikram.bank.transactions.response;

import com.naikram.bank.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private String type;
    private double amount;
    private Date transTime;
    private Account account;
}
