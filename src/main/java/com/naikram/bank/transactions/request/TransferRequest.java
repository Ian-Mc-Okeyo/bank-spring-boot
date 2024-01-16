package com.naikram.bank.transactions.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private Long receiverAccountNumber;
    private Long senderAccountNumber;
    private Double amount;
}
