package com.marcos.Fintech.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountUpdateDTO {

    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotBlank(message = "Currency is required")
    private String currency;

    public AccountUpdateDTO() {
    }

    // Getters
    public String getAccountNumber() {return accountNumber;}
    public String getCurrency() {return currency;}

    // Setters
    public void setAccountNumber(String accountNumber) {this.accountNumber = accountNumber;}
    public void setCurrency(String currency) {this.currency = currency;}
}