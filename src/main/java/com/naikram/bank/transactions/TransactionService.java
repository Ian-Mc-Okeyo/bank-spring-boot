package com.naikram.bank.transactions;

import com.naikram.bank.account.Account;
import com.naikram.bank.account.AccountRepository;
import com.naikram.bank.exceptions.AccountNotFoundException;
import com.naikram.bank.exceptions.InsufficientFundsException;
import com.naikram.bank.transactions.request.DepositRequest;
import com.naikram.bank.transactions.request.TransferRequest;
import com.naikram.bank.transactions.request.WithdrawRequest;
import com.naikram.bank.transactions.response.DepositResponse;
import com.naikram.bank.transactions.response.TransactionResponse;
import com.naikram.bank.transactions.response.TransferResponse;
import com.naikram.bank.transactions.response.WithdrawResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    public DepositResponse deposit(DepositRequest request){
        if(request.getAmount() < 1 ){
            return DepositResponse.builder()
                    .message("Invalid amount")
                    .status("failed")
                    .build();
        }

        //check account Number
        Account userAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if(userAccount == null){
            throw new AccountNotFoundException("Account not found");
        }

        //deposit
        transactionRepository.deposit(request.getAccountNumber(), request.getAmount());

        //save txn
        Transaction newTransaction = Transaction.builder()
                .amount(request.getAmount())
                .account(userAccount)
                .type("Deposit")
                .build();
        transactionRepository.save(newTransaction);
        return DepositResponse.builder()
                .message("Deposit Success")
                .status("success")
                .build();
    }

    public WithdrawResponse withdraw(WithdrawRequest request) {
        if(request.getAmount() < 1 ){
            return WithdrawResponse.builder()
                    .message("Invalid amount")
                    .status("failed")
                    .build();
        }

        //check account Number
        Account userAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if(userAccount == null){
            throw new AccountNotFoundException("Account not found");
        }

        //check if user has enough balance
        if(userAccount.getBalance() < request.getAmount()){
            throw new InsufficientFundsException("Insufficient Funds");
        }

        //deposit
        transactionRepository.withdraw(request.getAccountNumber(), request.getAmount());

        //save txn
        Transaction newTransaction = Transaction.builder()
                .amount(request.getAmount())
                .account(userAccount)
                .type("Withdrawal")
                .build();
        transactionRepository.save(newTransaction);
        return WithdrawResponse.builder()
                .message("Deposit Success")
                .status("success")
                .build();
    }

    public TransferResponse transfer(TransferRequest request) {
        //check amount
        if(request.getAmount() < 1 ){
            return TransferResponse.builder()
                    .message("Invalid amount")
                    .status("failed")
                    .build();
        }

        //check if sender account exists
        Account senderAccount = accountRepository.findByAccountNumber(request.getSenderAccountNumber());
        if(senderAccount == null){
            throw new AccountNotFoundException("Sender Account not found");
        }

        //check if receiver account exists
        Account receiverAccount = accountRepository.findByAccountNumber(request.getReceiverAccountNumber());
        if(receiverAccount == null){
            throw new AccountNotFoundException("Receiver Account not found");
        }

        //check if sender has enough money
        if(senderAccount.getBalance() < request.getAmount()){
            throw new InsufficientFundsException("Insufficient Funds");
        }

        //transact
        transactionRepository.withdraw(request.getSenderAccountNumber(), request.getAmount());
        transactionRepository.deposit(request.getReceiverAccountNumber(), request.getAmount());

        //save sender txn
        Transaction receiverTransaction = Transaction.builder()
                .amount(request.getAmount())
                .thirdPartyAccountNumber(senderAccount.getAccountNumber())
                .account(receiverAccount)
                .type("Receive")
                .build();
        transactionRepository.save(receiverTransaction);

        //save receiver txn
        Transaction senderTransaction = Transaction.builder()
                .amount(request.getAmount())
                .thirdPartyAccountNumber(receiverAccount.getAccountNumber())
                .account(receiverAccount)
                .type("Send")
                .build();
        transactionRepository.save(senderTransaction);

        return TransferResponse.builder()
                .message("Successful transfer")
                .status("success")
                .build();
    }

    public List<Transaction> getUserTxns(String userEmail){
        return transactionRepository.findByAccountUserEmail(userEmail);
    }
}
