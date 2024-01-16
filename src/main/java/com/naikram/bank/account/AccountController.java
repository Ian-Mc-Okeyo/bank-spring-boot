package com.naikram.bank.account;

import com.naikram.bank.account.requests.CreateAccountRequest;
import com.naikram.bank.account.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody CreateAccountRequest request,
            Authentication authentication
            ){
        //access the userEmail from the authenticated userPrincipal
        String userEmail = authentication.getName();
        LOGGER.info("Username is: " + userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request, userEmail));
    }
    @GetMapping("/get/user-accounts")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            Authentication authentication
    ){
        LOGGER.info("In getting the user accounts");
        String userEmail = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(accountService.findUserAccounts(userEmail));
    }

    @GetMapping("/dummy")
    public String dummy(){
        return "Dummy";
    }

    @GetMapping("/set-null-balance")
    public ResponseEntity<String> setBalanceToNull(){
        LOGGER.info("In setting the balance to 0");
        return ResponseEntity.status(HttpStatus.OK).body(accountService.setNullBalance());
    }
}
