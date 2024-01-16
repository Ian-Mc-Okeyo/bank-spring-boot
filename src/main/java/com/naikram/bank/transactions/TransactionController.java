package com.naikram.bank.transactions;

import com.naikram.bank.transactions.request.DepositRequest;
import com.naikram.bank.transactions.request.TransferRequest;
import com.naikram.bank.transactions.request.WithdrawRequest;
import com.naikram.bank.transactions.response.DepositResponse;
import com.naikram.bank.transactions.response.TransactionResponse;
import com.naikram.bank.transactions.response.TransferResponse;
import com.naikram.bank.transactions.response.WithdrawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@RequestBody DepositRequest request) {
        DepositResponse response = transactionService.deposit(request);
        if(response.getStatus().equals("failed")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(@RequestBody WithdrawRequest request){
        WithdrawResponse response = transactionService.withdraw(request);
        if(response.getStatus().equals("failed")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request){
        TransferResponse response = transactionService.transfer(request);
        if(response.getStatus().equals("failed")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get-transaction-history")
    public ResponseEntity<List<Transaction>> getTxnHistory(Authentication authentication){
        String userEmail = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getUserTxns(userEmail));
    }
}
