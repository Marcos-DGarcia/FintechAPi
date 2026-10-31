package com.marcos.Fintech.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class AccountCreateDTO {

    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    private BigDecimal balance;
    @NotBlank(message = "Currency is required")
    private String currency;
    @NotNull(message = "User id is required")
    private Long userId;

    public AccountCreateDTO() {
    }

    // Getters
    public String getAccountNumber() {return accountNumber;}
    public BigDecimal getBalance() {return balance;}
    public String getCurrency() {return currency;}
    public Long getUserId() {return userId;}

    // Setters
    public void setAccountNumber(String accountNumber) {this.accountNumber = accountNumber;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
    public void setCurrency(String currency) {this.currency = currency;}
    public void setUserId(Long userId) {this.userId = userId;}
}