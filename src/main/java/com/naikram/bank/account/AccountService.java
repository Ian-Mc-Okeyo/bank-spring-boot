package com.naikram.bank.account;

import com.naikram.bank.account.requests.CreateAccountRequest;
import com.naikram.bank.account.response.AccountResponse;
import com.naikram.bank.exceptions.WrongLoginCredentialsException;
import com.naikram.bank.user.User;
import com.naikram.bank.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    public Account findByAccountNumber(Long accountNumber){
        return accountRepository.findByAccountNumber(accountNumber);
    }

    protected long generateAccountNumber(){
        //generate the random 10 digit number
        Random random = new Random();
        long lowerBound = 1_000_000_000L; // Smallest 10-digit number (10^8)
        long upperBound = 9_999_999_999L; // Largest 10-digit number (10^9 - 1)
        long random10DigitNumber = 0;

        while (true){
            random10DigitNumber = lowerBound + (long)(random.nextDouble() * (upperBound - lowerBound + 1));
            Account account = accountRepository.findByAccountNumber(random10DigitNumber);
            if(account == null){
                break;
            }
        }

        return random10DigitNumber;

    }

    public AccountResponse createAccount(CreateAccountRequest request, String userEmail) {
        User user = (User) userRepository.findByEmail(userEmail).orElseThrow(
                ()->new WrongLoginCredentialsException("User not found")
        );
        Account newAccount = Account.builder()
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .accountNumber(generateAccountNumber())
                .user(user)
                .build();
        Account savedAccount = accountRepository.save(newAccount);
        LOGGER.info("New account created for " + user.getFirstName());

        return AccountResponse.builder()
                .accountName(savedAccount.getAccountName())
                .balance(savedAccount.getBalance())
                .accountNumber(savedAccount.getAccountNumber())
                .createdAt(savedAccount.getCreatedAt())
                .accountType(savedAccount.getAccountType())
                .build();
    }

    public List<AccountResponse> findUserAccounts(String userEmail) {
        return accountRepository.findByUserEmail(userEmail);
    }

    public String setNullBalance() {
        accountRepository.setNullBalance();
        return "Update successful";
    }
}
