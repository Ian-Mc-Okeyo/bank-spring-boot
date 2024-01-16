package com.naikram.bank.transactions;

import com.naikram.bank.transactions.response.TransactionResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.accountNumber = :accountNumber")
    void deposit(@Param("accountNumber") Long accountNumber, @Param("amount") Double amount);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.accountNumber = :accountNumber")
    void withdraw(Long accountNumber, Double amount);

    @Query("SELECT t FROM Transaction t where t.account.user.email = :userEmail")
    List<Transaction> findByAccountUserEmail(@Param("userEmail") String userEmail);
}
