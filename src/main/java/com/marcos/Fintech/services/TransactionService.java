package com.marcos.Fintech.services;

import com.marcos.Fintech.dto.TransactionResponseDTO;
import com.marcos.Fintech.entity.Account;
import com.marcos.Fintech.entity.Transaction;
import com.marcos.Fintech.exceptions.InvalidAmountException;
import com.marcos.Fintech.exceptions.SameAccountTransferException;
import com.marcos.Fintech.exceptions.TransactionNotFoundException;
import com.marcos.Fintech.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;


    // Constructor
    public TransactionService(
            TransactionRepository transactionRepository,
            AccountService accountService
    ) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }


    // FIND ALL
    public List<TransactionResponseDTO> findAll() {

        return transactionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }


    // FIND BY ID
    public TransactionResponseDTO findById(Long id) {

        Transaction transaction = findEntityById(id);

        return toResponseDTO(transaction);
    }


    // TRANSFER
    @Transactional
    public TransactionResponseDTO transfer(
            Long originAccountId,
            Long destinationAccountId,
            BigDecimal amount
    ) {

        if (originAccountId.equals(destinationAccountId)) {
            throw new SameAccountTransferException(
                    "Origin and destination accounts are the same"
            );
        }


        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                    "Amount must be greater than zero"
            );
        }

        Account originAccount =
                accountService.findById(originAccountId);

        Account destinationAccount =
                accountService.findById(destinationAccountId);


        accountService.withdraw(
                originAccountId,
                amount
        );

        accountService.deposit(
                destinationAccountId,
                amount
        );


        Transaction transaction = new Transaction(
                amount,
                "TRANSFER",
                LocalDateTime.now(),
                "Transfer between accounts",
                originAccount,
                destinationAccount
        );

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return toResponseDTO(savedTransaction);
    }


    // INTERNAL METHOD - FIND ENTITY
    private Transaction findEntityById(Long id) {

        return transactionRepository.findById(id)
                .orElseThrow(
                        () -> new TransactionNotFoundException(
                                "Transaction not found"
                        )
                );
    }

    // ENTITY -> RESPONSE DTO
    private TransactionResponseDTO toResponseDTO(
            Transaction transaction
    ) {

        Long originAccountId = null;
        Long destinationAccountId = null;

        if (transaction.getOriginAccount() != null) {
            originAccountId =
                    transaction.getOriginAccount().getId();
        }


        if (transaction.getDestinationAccount() != null) {
            destinationAccountId =
                    transaction.getDestinationAccount().getId();
        }


        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                originAccountId,
                destinationAccountId
        );
    }
}