package com.marcos.Fintech.exceptions;

public class SameAccountTransferException extends RuntimeException {

    public SameAccountTransferException(String message) {
        super(message);
    }

}
