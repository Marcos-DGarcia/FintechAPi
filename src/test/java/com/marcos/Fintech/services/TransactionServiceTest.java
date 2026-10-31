package com.marcos.Fintech.services;

import com.marcos.Fintech.dto.TransactionResponseDTO;
import com.marcos.Fintech.entity.Account;
import com.marcos.Fintech.entity.Transaction;
import com.marcos.Fintech.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private TransactionService transactionService;


    @BeforeEach
    void setUp() {

        // Fake Repository
        transactionRepository =
                Mockito.mock(TransactionRepository.class);

        // Fake AccountService
        accountService =
                Mockito.mock(AccountService.class);

        // Real TransactionService
        transactionService =
                new TransactionService(
                        transactionRepository,
                        accountService
                );
    }


    // TRANSFER
    @Test
    void shouldTransferMoneyBetweenAccounts() {

        // Given
        Account originAccount = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                null
        );

        Account destinationAccount = new Account(
                "ACC-002",
                new BigDecimal("500.00"),
                "ARS",
                true,
                LocalDate.now(),
                null
        );


        when(accountService.findById(1L))
                .thenReturn(originAccount);

        when(accountService.findById(2L))
                .thenReturn(destinationAccount);


        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(
                        invocation -> invocation.getArgument(0)
                );


        // When
        TransactionResponseDTO result =
                transactionService.transfer(
                        1L,
                        2L,
                        new BigDecimal("200.00")
                );


        // Then
        verify(accountService).withdraw(
                1L,
                new BigDecimal("200.00")
        );

        verify(accountService).deposit(
                2L,
                new BigDecimal("200.00")
        );

        assertEquals(
                "TRANSFER",
                result.getType()
        );

        assertEquals(
                new BigDecimal("200.00"),
                result.getAmount()
        );

        verify(transactionRepository)
                .save(any(Transaction.class));
    }


    // SAME ACCOUNT
    @Test
    void shouldRejectTransferToSameAccount() {

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(
                        1L,
                        1L,
                        new BigDecimal("200.00")
                )
        );


        // Then
        assertEquals(
                "Origin and destination accounts are the same",
                exception.getMessage()
        );

        verify(
                accountService,
                never()
        ).withdraw(
                anyLong(),
                any(BigDecimal.class)
        );

        verify(
                transactionRepository,
                never()
        ).save(any(Transaction.class));
    }


    // INVALID AMOUNT
    @Test
    void shouldRejectTransferWithInvalidAmount() {

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(
                        1L,
                        2L,
                        new BigDecimal("-100.00")
                )
        );


        // Then
        assertEquals(
                "Amount must be greater than zero",
                exception.getMessage()
        );

        verify(
                accountService,
                never()
        ).withdraw(
                anyLong(),
                any(BigDecimal.class)
        );

        verify(
                accountService,
                never()
        ).deposit(
                anyLong(),
                any(BigDecimal.class)
        );
    }

    // FIND BY ID
    @Test
    void shouldFindTransactionById() {

        // Given
        Transaction transaction = new Transaction(
                new BigDecimal("300.00"),
                "TRANSFER",
                LocalDateTime.now(),
                "Test transfer",
                null,
                null
        );

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));


        // When
        TransactionResponseDTO result =
                transactionService.findById(1L);


        // Then
        assertEquals(
                new BigDecimal("300.00"),
                result.getAmount()
        );

        assertEquals(
                "TRANSFER",
                result.getType()
        );

        assertEquals(
                "Test transfer",
                result.getDescription()
        );
    }

    // TRANSACTION NOT FOUND
    @Test
    void shouldThrowWhenTransactionDoesNotExist() {

        // Given
        when(transactionRepository.findById(99L))
                .thenReturn(Optional.empty());


        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.findById(99L)
        );


        // Then
        assertEquals(
                "Transaction not found",
                exception.getMessage()
        );
    }
}
