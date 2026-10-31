package com.marcos.Fintech.exceptions;

import javax.security.auth.login.AccountException;

public class AccountHasBalanceException extends RuntimeException {
    public AccountHasBalanceException(String message) {
        super(message);
    }
}
