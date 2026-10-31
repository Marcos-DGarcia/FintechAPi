package com.marcos.Fintech.controllers;

import com.marcos.Fintech.dto.AccountCreateDTO;
import com.marcos.Fintech.dto.AccountResponseDTO;
import com.marcos.Fintech.dto.AccountUpdateDTO;
import com.marcos.Fintech.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(
        name = "Accounts",
        description = "Account management, deposits, withdrawals and account status operations"
)
public class AccountController {

    private final AccountService accountService;

    // Constructor
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    // GET ALL
    // GET /accounts
    @Operation(
            summary = "List all accounts",
            description = "Returns all accounts registered in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of accounts returned successfully"
    )
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> findAll() {

        List<AccountResponseDTO> list = accountService.findAll();

        return ResponseEntity.ok(list);
    }


    // GET BY ID
    // GET /accounts/{id}
    @Operation(
            summary = "Find an account by id",
            description = "Returns the account corresponding to the provided id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> findOne(
            @Parameter(
                    description = "Account id",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        AccountResponseDTO account = accountService.findByIdResponse(id);

        return ResponseEntity.ok(account);
    }


    // GET BY ACCOUNT NUMBER
    // GET /accounts/number/{accountNumber}
    @Operation(
            summary = "Find an account by account number",
            description = "Returns an account using its account number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> findByAccountNumber(

            @Parameter(
                    description = "Account number",
                    example = "ACC-001"
            )
            @PathVariable String accountNumber

    ) {
        AccountResponseDTO account =
                accountService.findByAccountNumber(accountNumber);

        return ResponseEntity.ok(account);
    }


    // CREATE
    // POST /accounts
    @Operation(
            summary = "Create an account",
            description = "Creates a new account associated with an existing active user"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Account created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid account data"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "The user is inactive"
    )
    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(
            @Valid @RequestBody AccountCreateDTO dto
    ) {
        AccountResponseDTO account = accountService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(account);
    }


    // UPDATE
    // PUT /accounts/{id}
    @Operation(
            summary = "Update an account",
            description = "Updates the account number and currency of an existing account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account updated successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid account data"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(

            @Parameter(
                    description = "Account id",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody AccountUpdateDTO dto

    ) {
        AccountResponseDTO account =
                accountService.update(id, dto);

        return ResponseEntity.ok(account);
    }


    // ACTIVATE
    // PUT /accounts/{id}/activate
    @Operation(
            summary = "Activate an account",
            description = "Activates an existing account. The account cannot be activated if its user is inactive"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account activated successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Account cannot be activated because its user is inactive"
    )
    @PutMapping("/{id}/activate")
    public ResponseEntity<AccountResponseDTO> activate(

            @Parameter(
                    description = "Account id",
                    example = "1"
            )
            @PathVariable Long id

    ) {
        AccountResponseDTO account =
                accountService.activate(id);

        return ResponseEntity.ok(account);
    }


    // DEACTIVATE
    // PUT /accounts/{id}/deactivate
    @Operation(
            summary = "Deactivate an account",
            description = "Deactivates an account only when its balance is zero"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account deactivated successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Account cannot be deactivated because it has a non-zero balance"
    )
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<AccountResponseDTO> deactivate(

            @Parameter(
                    description = "Account id",
                    example = "1"
            )
            @PathVariable Long id

    ) {
        AccountResponseDTO account =
                accountService.deactivate(id);

        return ResponseEntity.ok(account);
    }


    // DEPOSIT
    // POST /accounts/{id}/deposit?amount=1000
    @Operation(
            summary = "Deposit money into an account",
            description = "Adds the specified amount to an active account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Deposit completed successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid amount or inactive account"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(

            @Parameter(
                    description = "Account id",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Amount to deposit",
                    example = "1000.00"
            )
            @RequestParam BigDecimal amount

    ) {
        AccountResponseDTO account =
                accountService.deposit(id, amount);

        return ResponseEntity.ok(account);
    }


    // WITHDRAW
    // POST /accounts/{id}/withdraw?amount=500
    @Operation(
            summary = "Withdraw money from an account",
            description = "Withdraws an amount from an active account if sufficient funds are available"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Withdrawal completed successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid amount, inactive account or insufficient funds"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponseDTO> withdraw(

            @Parameter(
                    description = "Account id",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Amount to withdraw",
                    example = "500.00"
            )
            @RequestParam BigDecimal amount

    ) {
        AccountResponseDTO account =
                accountService.withdraw(id, amount);

        return ResponseEntity.ok(account);
    }


    // DELETE
    // DELETE /accounts/{id}
    @Operation(
            summary = "Delete an account",
            description = "Deletes an account if it does not have associated transactions"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Account deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Account cannot be deleted because it has associated transactions"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Account id",
                    example = "1"
            )
            @PathVariable Long id

    ) {
        accountService.delete(id);

        return ResponseEntity.noContent().build();
    }
}