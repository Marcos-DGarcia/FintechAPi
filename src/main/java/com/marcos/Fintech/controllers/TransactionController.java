package com.marcos.Fintech.controllers;

import com.marcos.Fintech.dto.TransactionResponseDTO;
import com.marcos.Fintech.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(
        name = "Transactions",
        description = "Operations related to transfers and transactions"
)
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService
    ) {
        this.transactionService = transactionService;
    }


    // GET ALL
    // GET /transactions
    @Operation(
            summary = "List all transactions",
            description = "Returns all transactions registered in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of transactions returned successfully"
    )
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> findAll() {

        List<TransactionResponseDTO> list =
                transactionService.findAll();

        return ResponseEntity.ok(list);
    }


    // GET BY ID
    // GET /transactions/{id}
    @Operation(
            summary = "Find a transaction by id",
            description = "Returns the transaction corresponding to the provided id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transaction found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Transaction not found"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(

            @Parameter(
                    description = "Transaction id",
                    example = "1"
            )
            @PathVariable Long id

    ) {
        TransactionResponseDTO transaction =
                transactionService.findById(id);

        return ResponseEntity.ok(transaction);
    }


    // TRANSFER
    // POST /transactions/transfer
    @Operation(
            summary = "Transfer money between accounts",
            description = "Transfers an amount from an origin account to a destination account and creates a transaction record"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Transfer completed successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid amount, insufficient funds, inactive account or same origin and destination account"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Origin or destination account not found"
    )
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(

            @Parameter(
                    description = "Origin account id",
                    example = "1"
            )
            @RequestParam Long originAccountId,

            @Parameter(
                    description = "Destination account id",
                    example = "2"
            )
            @RequestParam Long destinationAccountId,

            @Parameter(
                    description = "Amount to transfer",
                    example = "500.00"
            )
            @RequestParam BigDecimal amount

    ) {

        TransactionResponseDTO transfer =
                transactionService.transfer(
                        originAccountId,
                        destinationAccountId,
                        amount
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transfer);
    }
}