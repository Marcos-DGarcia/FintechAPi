package com.marcos.Fintech.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountResponseDTO {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private boolean active;
    private LocalDate creationDate;
    private Long userId;

    public AccountResponseDTO(
            Long id,
            String accountNumber,
            BigDecimal balance,
            String currency,
            boolean active,
            LocalDate creationDate,
            Long userId
    ) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.active = active;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    // Getters
    public Long getId() {return id;}
    public String getAccountNumber() {return accountNumber;}
    public BigDecimal getBalance() {return balance;}
    public String getCurrency() {return currency;}
    public boolean isActive() {return active;}
    public LocalDate getCreationDate() {return creationDate;}
    public Long getUserId() {return userId;}

}