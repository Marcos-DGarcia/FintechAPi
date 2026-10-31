package com.marcos.Fintech.exceptions;

public class AccountHasTransactionsException extends RuntimeException {
    public AccountHasTransactionsException(String message) {
        super(message);
    }
}
