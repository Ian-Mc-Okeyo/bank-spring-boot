package com.naikram.bank.transactions.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DepositResponse {
    private String message;
    private String status;
}
