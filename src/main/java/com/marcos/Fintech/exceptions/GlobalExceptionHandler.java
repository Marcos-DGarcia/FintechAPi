package com.marcos.Fintech.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException err) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException err) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleTransactionNotFound(TransactionNotFoundException err) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
    }
    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmount(InvalidAmountException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(InsufficientFundsException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
    }
    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<String> handleAccountInactive(AccountInactiveException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
    }
    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<String> handleSameAccountTransfer(SameAccountTransferException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
    }
    @ExceptionHandler(AccountHasTransactionsException.class)
    public ResponseEntity<String> handleAccountHasTransactions(
            AccountHasTransactionsException err
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(err.getMessage());
    }
    @ExceptionHandler(AccountHasBalanceException.class)
    public ResponseEntity<String> handleAccountHasBalance(
            AccountHasBalanceException err
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(err.getMessage());
    }
    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<String> handleUserInactive(
            UserInactiveException err
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(err.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(
            MethodArgumentNotValidException err
    ) {

        String message = err.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

}
