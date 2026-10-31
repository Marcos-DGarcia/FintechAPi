package com.marcos.Fintech.config;

import com.marcos.Fintech.entity.Account;
import com.marcos.Fintech.entity.Transaction;
import com.marcos.Fintech.entity.User;
import com.marcos.Fintech.repositories.AccountRepository;
import com.marcos.Fintech.repositories.TransactionRepository;
import com.marcos.Fintech.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;



@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public InitialDataLoader(AccountRepository accountRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    @Override
    public void run(String... args){
        if (userRepository.count() > 0){
            return;
        }
        // 1- Create users
        User user1 = new User(
                "Marcos",
                "Garcia",
                "30123456",
                "marcos@mail.com",
                "1122334455"
        );
        User user2 = new User(
                "Braian",
                "Gonzalez",
                "30234567",
                "braian@mail.com",
                "2233445566"
        );

        // 2- Save users
        userRepository.save(user1);
        userRepository.save(user2);

        // 3- Create accounts

        Account account1 = new Account(
                "ACC-001",
                new BigDecimal("1000000.00"),
                "ARS",
                true,
                LocalDate.now(),
                user1
        );
        Account account2 = new Account(
                "ACC-002",
                new BigDecimal(500000.00),
                "ARS",
                true,
                LocalDate.now(),
                user2
        );
        // 4- Save accounts
        accountRepository.save(account1);
        accountRepository.save(account2);

        // 5- Create transaction
        Transaction transaction1 = new Transaction(
                new BigDecimal("100000.00"),
                "TRANSFER",
                LocalDateTime.now(),
                "Transfer from account 1 to account 2 ",
                account1,
                account2
        );
        // 6- Save transaction
        transactionRepository.save(transaction1);



    }
}
