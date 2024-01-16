package com.naikram.bank.account;

import com.naikram.bank.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Random;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long accountId;

    @Column(unique = true)
    private Long accountNumber;

    private String accountName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(columnDefinition = "DECIMAL(12, 2) DEFAULT 0.0") //default is 0.0 with 10 digits and 2 dp
    private Double balance;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Date createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Date updatedAt;

    @ManyToOne(
            cascade = CascadeType.ALL,
            optional = false
    )
    @JoinColumn(
            name="user_id"
    )
    private User user;

    @PrePersist //runs before being saved
    private void setBalance(){
        this.balance = 0.0;
    }

}
