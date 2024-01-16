package com.naikram.bank.account;

import com.naikram.bank.account.response.AccountResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByAccountNumber(Long accountNumber);

    List<AccountResponse> findByUserEmail(String userEmail);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE account SET balance=0.0 WHERE balance IS NULL",
            nativeQuery = true
    )
    void setNullBalance();
}
