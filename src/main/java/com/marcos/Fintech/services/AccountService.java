package com.marcos.Fintech.services;

import com.marcos.Fintech.dto.AccountCreateDTO;
import com.marcos.Fintech.dto.AccountResponseDTO;
import com.marcos.Fintech.dto.AccountUpdateDTO;
import com.marcos.Fintech.entity.Account;
import com.marcos.Fintech.entity.User;
import com.marcos.Fintech.exceptions.*;
import com.marcos.Fintech.repositories.AccountRepository;
import com.marcos.Fintech.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    // Constructor
    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository
    ) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }


    // CREATE
    public AccountResponseDTO create(AccountCreateDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!user.isActive()){
            throw new UserInactiveException("Cannot create an active account for an inactive user");
        }

        Account account = new Account(
                dto.getAccountNumber(),
                dto.getBalance(),
                dto.getCurrency(),
                true,
                LocalDate.now(),
                user
        );

        Account savedAccount = accountRepository.save(account);

        return toResponseDTO(savedAccount);
    }


    // FIND ALL
    public List<AccountResponseDTO> findAll() {

        return accountRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }


    // FIND BY ID - RESPONSE FOR CONTROLLER
    public AccountResponseDTO findByIdResponse(Long id) {

        Account account = findById(id);

        return toResponseDTO(account);
    }


    // FIND BY ACCOUNT NUMBER
    public AccountResponseDTO findByAccountNumber(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(
                        () -> new AccountNotFoundException("Account not found")
                );

        return toResponseDTO(account);
    }


    // UPDATE
    public AccountResponseDTO update(
            Long id,
            AccountUpdateDTO dto
    ) {

        Account account = findById(id);

        account.setAccountNumber(dto.getAccountNumber());
        account.setCurrency(dto.getCurrency());

        Account updatedAccount = accountRepository.save(account);

        return toResponseDTO(updatedAccount);
    }


    // DELETE
    public void delete(Long id) {

        Account account = findById(id);
        try {
            accountRepository.delete(account);
            accountRepository.flush();
        }catch (DataIntegrityViolationException err) {
            throw new AccountHasTransactionsException("Account cannot be deleted because it has transactions. Deactivate the account instead.");
        }
    }


    // ACTIVATE
    public AccountResponseDTO activate(Long id) {

        Account account = findById(id);

        if(!account.getUser().isActive()){
            throw new UserInactiveException( "Account cannot be activated because its user is inactive");
        }

        account.activate();

        Account updatedAccount = accountRepository.save(account);

        return toResponseDTO(updatedAccount);
    }


    // DEACTIVATE
    public AccountResponseDTO deactivate(Long id) {

        Account account = findById(id);

        if(account.getBalance().compareTo(BigDecimal.ZERO) !=0){
         throw new AccountHasBalanceException("Account cannot be deactivated because its balance is not Zero");
        }

        account.deactivate();

        Account updatedAccount = accountRepository.save(account);

        return toResponseDTO(updatedAccount);
    }


    // DEPOSIT
    public AccountResponseDTO deposit(
            Long id,
            BigDecimal amount
    ) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                    "Amount must be greater than zero"
            );
        }

        Account account = findById(id);

        if (!account.isActive()) {
            throw new AccountInactiveException(
                    "Account not active"
            );
        }

        BigDecimal newBalance =
                account.getBalance().add(amount);

        account.setBalance(newBalance);

        Account updatedAccount =
                accountRepository.save(account);

        return toResponseDTO(updatedAccount);
    }

    // WITHDRAW
    public AccountResponseDTO withdraw(
            Long id,
            BigDecimal amount
    ) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                    "Amount must be greater than zero"
            );
        }

        Account account = findById(id);

        if (!account.isActive()) {
            throw new AccountInactiveException(
                    "Account not active"
            );
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds"
            );
        }

        BigDecimal newBalance =
                account.getBalance().subtract(amount);

        account.setBalance(newBalance);

        Account updatedAccount =
                accountRepository.save(account);

        return toResponseDTO(updatedAccount);
    }

    // INTERNAL METHOD - FIND ENTITY
    Account findById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(
                        () -> new AccountNotFoundException("Account not found")
                );
    }

    // ENTITY -> RESPONSE DTO
    private AccountResponseDTO toResponseDTO(Account account) {

        return new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.isActive(),
                account.getCreationDate(),
                account.getUser().getId()
        );
    }
}