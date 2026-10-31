package com.marcos.Fintech.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marcos.Fintech.interfaces.Activatable;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account")
public class Account implements Activatable {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Empty constructor
    public Account() {
    }

    // Constructor
    public Account(
            String accountNumber,
            BigDecimal balance,
            String currency,
            boolean active,
            LocalDate creationDate,
            User user
    ) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.active = active;
        this.creationDate = creationDate;
        this.user = user;
    }

    // Getters
    public Long getId() {return id;}
    public String getAccountNumber() {return accountNumber;}
    public BigDecimal getBalance() {return balance;}
    public String getCurrency() {return currency;}
    public boolean isActive() {return active;}
    public LocalDate getCreationDate() {return creationDate;}
    public User getUser() {return user;}

    // Setters
    public void setAccountNumber(String accountNumber) {this.accountNumber = accountNumber;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
    public void setCurrency(String currency) {this.currency = currency;}
    public void setUser(User user) {this.user = user;}

    // Concrete methods
    @Override
    public void activate() {this.active = true;}
    @Override
    public void deactivate() {this.active = false;}

}