package com.marcos.Fintech.services;

import com.marcos.Fintech.dto.AccountCreateDTO;
import com.marcos.Fintech.dto.AccountResponseDTO;
import com.marcos.Fintech.dto.AccountUpdateDTO;
import com.marcos.Fintech.entity.Account;
import com.marcos.Fintech.entity.User;
import com.marcos.Fintech.exceptions.AccountHasBalanceException;
import com.marcos.Fintech.exceptions.UserInactiveException;
import com.marcos.Fintech.repositories.AccountRepository;
import com.marcos.Fintech.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountService accountService;
    private User user;


    @BeforeEach
    void setUp() {

        accountRepository = Mockito.mock(AccountRepository.class);
        userRepository = Mockito.mock(UserRepository.class);

        accountService = new AccountService(
                accountRepository,
                userRepository
        );

        user = new User(
                "Marcos",
                "Garcia",
                "30123456",
                "marcos@mail.com",
                "1122334455"
        );
    }


    // CREATE
    @Test
    void shouldCreateAccount() {

        // Given
        AccountCreateDTO dto = new AccountCreateDTO();

        dto.setAccountNumber("ACC-003");
        dto.setBalance(new BigDecimal("5000.00"));
        dto.setCurrency("ARS");
        dto.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // When
        AccountResponseDTO result =
                accountService.create(dto);


        // Then
        assertEquals(
                "ACC-003",
                result.getAccountNumber()
        );

        assertEquals(
                new BigDecimal("5000.00"),
                result.getBalance()
        );

        assertEquals(
                "ARS",
                result.getCurrency()
        );

        assertTrue(result.isActive());

        verify(userRepository).findById(1L);

        verify(accountRepository)
                .save(any(Account.class));
    }


    // FIND ALL
    @Test
    void shouldFindAllAccounts() {

        // Given
        Account account1 = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        Account account2 = new Account(
                "ACC-002",
                new BigDecimal("2000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findAll())
                .thenReturn(List.of(account1, account2));


        // When
        List<AccountResponseDTO> result =
                accountService.findAll();


        // Then
        assertEquals(2, result.size());

        assertEquals(
                "ACC-001",
                result.get(0).getAccountNumber()
        );

        assertEquals(
                "ACC-002",
                result.get(1).getAccountNumber()
        );

        verify(accountRepository).findAll();
    }


    // FIND BY ID
    @Test
    void shouldFindAccountById() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        // When
        AccountResponseDTO result =
                accountService.findByIdResponse(1L);


        // Then
        assertEquals(
                "ACC-001",
                result.getAccountNumber()
        );

        assertEquals(
                new BigDecimal("1000.00"),
                result.getBalance()
        );
    }


    // DEPOSIT
    @Test
    void shouldDepositMoney() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // When
        AccountResponseDTO result =
                accountService.deposit(
                        1L,
                        new BigDecimal("500.00")
                );


        // Then
        assertEquals(
                new BigDecimal("1500.00"),
                result.getBalance()
        );

        verify(accountRepository).save(account);
    }

    // WITHDRAW
    @Test
    void shouldWithdrawMoney() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // When
        AccountResponseDTO result =
                accountService.withdraw(
                        1L,
                        new BigDecimal("500.00")
                );


        // Then
        assertEquals(
                new BigDecimal("500.00"),
                result.getBalance()
        );

        verify(accountRepository).save(account);
    }


    // INSUFFICIENT FUNDS
    @Test
    void shouldRejectWithdrawWithInsufficientFunds() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.withdraw(
                        1L,
                        new BigDecimal("2000.00")
                )
        );


        // Then
        assertEquals(
                "Insufficient funds",
                exception.getMessage()
        );

        verify(
                accountRepository,
                never()
        ).save(any(Account.class));
    }


    // INACTIVE ACCOUNT
    @Test
    void shouldRejectDepositOnInactiveAccount() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                false,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.deposit(
                        1L,
                        new BigDecimal("500.00")
                )
        );


        // Then
        assertEquals(
                "Account not active",
                exception.getMessage()
        );

        verify(
                accountRepository,
                never()
        ).save(any(Account.class));
    }

    // ACCOUNT NOT FOUND
    @Test
    void shouldThrowWhenAccountDoesNotExist() {

        // Given
        when(accountRepository.findById(99L))
                .thenReturn(Optional.empty());


        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.findByIdResponse(99L)
        );


        // Then
        assertEquals(
                "Account not found",
                exception.getMessage()
        );
    }

    // UPDATE
    @Test
    void shouldUpdateAccount() {

        // Given
        Account existingAccount = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        AccountUpdateDTO dto = new AccountUpdateDTO();

        dto.setAccountNumber("ACC-001-UPDATED");
        dto.setCurrency("USD");


        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(existingAccount));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // When
        AccountResponseDTO result =
                accountService.update(
                        1L,
                        dto
                );


        // Then
        assertEquals(
                "ACC-001-UPDATED",
                result.getAccountNumber()
        );

        assertEquals(
                "USD",
                result.getCurrency()
        );

        /*
         * El balance NO debe modificarse mediante update().
         */
        assertEquals(
                new BigDecimal("1000.00"),
                result.getBalance()
        );

        verify(accountRepository)
                .save(existingAccount);
    }

    // ACTIVATE
    @Test
    void shouldActivateAccount() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                false,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // When
        AccountResponseDTO result =
                accountService.activate(1L);


        // Then
        assertTrue(result.isActive());

        verify(accountRepository).save(account);
    }



    // DELETE
    @Test
    void shouldDeleteAccount() {

        // Given
        Account account = new Account(
                "ACC-003",
                new BigDecimal("0.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(3L))
                .thenReturn(Optional.of(account));


        // When
        accountService.delete(3L);


        // Then
        verify(accountRepository).delete(account);
    }

    // DELETE NON EXISTING
    @Test
    void shouldThrowWhenDeletingNonExistingAccount() {

        // Given
        when(accountRepository.findById(99L))
                .thenReturn(Optional.empty());


        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.delete(99L)
        );


        // Then
        assertEquals(
                "Account not found",
                exception.getMessage()
        );

        verify(
                accountRepository,
                never()
        ).delete(any(Account.class));
    }



    // FIND BY ACCOUNT NUMBER
    @Test
    void shouldFindAccountByAccountNumber() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findByAccountNumber("ACC-001"))
                .thenReturn(Optional.of(account));


        // When
        AccountResponseDTO result =
                accountService.findByAccountNumber("ACC-001");


        // Then
        assertEquals(
                "ACC-001",
                result.getAccountNumber()
        );
    }
    @Test
    void shouldRejectDeactivationWhenAccountHasBalance() {

        // Given
        Account account = new Account(
                "ACC-001",
                new BigDecimal("1000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        // When
        AccountHasBalanceException exception = assertThrows(
                AccountHasBalanceException.class,
                () -> accountService.deactivate(1L)
        );

        // Then
        assertEquals(
                "Account cannot be deactivated because its balance is not Zero",
                exception.getMessage()
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }
    @Test
    void shouldDeactivateAccountWithZeroBalance() {

        // Given
        Account account = new Account(
                "ACC-001",
                BigDecimal.ZERO,
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        AccountResponseDTO result =
                accountService.deactivate(1L);

        // Then
        assertFalse(result.isActive());

        verify(accountRepository).save(account);
    }
    @Test
    void shouldRejectAccountActivationWhenUserIsInactive() {

        // Given
        user.deactivate();

        Account account = new Account(
                "ACC-001",
                BigDecimal.ZERO,
                "ARS",
                false,
                LocalDate.now(),
                user
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        // When
        UserInactiveException exception = assertThrows(
                UserInactiveException.class,
                () -> accountService.activate(1L)
        );

        // Then
        assertEquals(
                "Account cannot be activated because its user is inactive",
                exception.getMessage()
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }
    @Test
    void shouldRejectAccountCreationForInactiveUser() {

        // Given
        user.deactivate();

        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountNumber("ACC-003");
        dto.setBalance(BigDecimal.ZERO);
        dto.setCurrency("ARS");
        dto.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // When
        UserInactiveException exception = assertThrows(
                UserInactiveException.class,
                () -> accountService.create(dto)
        );

        // Then
        assertEquals(
                "Cannot create an active account for an inactive user",
                exception.getMessage()
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }
}