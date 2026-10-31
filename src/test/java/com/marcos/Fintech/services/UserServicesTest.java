package com.marcos.Fintech.services;
import com.marcos.Fintech.dto.UserCreateDTO;
import com.marcos.Fintech.dto.UserResponseDTO;
import com.marcos.Fintech.dto.UserUpdateDTO;
import com.marcos.Fintech.entity.Account;
import com.marcos.Fintech.entity.User;
import com.marcos.Fintech.exceptions.AccountHasBalanceException;
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

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {

        // Repository false
        userRepository = Mockito.mock(UserRepository.class);
        // Service real
        userService = new UserService(userRepository);
    }

    // CREATE
    @Test
    void shouldCreateUser() {

        // Given
        UserCreateDTO dto = new UserCreateDTO();

        dto.setFirstName("Laura");
        dto.setLastName("Gomez");
        dto.setDocumentNumber("33444555");
        dto.setEmail("laura@mail.com");
        dto.setPhone("1155554444");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UserResponseDTO result = userService.create(dto);

        // Then
        assertEquals("Laura", result.getFirstName());
        assertEquals("Gomez", result.getLastName());
        assertEquals("33444555", result.getDocumentNumber());
        assertEquals("laura@mail.com", result.getEmail());

        verify(userRepository).save(any(User.class));
    }

    // FIND ALL
    @Test
    void shouldFindAllUsers() {

        // Given
        User user1 = new User(
                "Marcos",
                "Garcia",
                "30123456",
                "marcos@mail.com",
                "1122334455"
        );

        User user2 = new User(
                "Laura",
                "Gomez",
                "33444555",
                "laura@mail.com",
                "1155554444"
        );
        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        // When

        List<UserResponseDTO> result = userService.findAll();

        // Then
        assertEquals(2, result.size());

        assertEquals(
                "Marcos",
                result.get(0).getFirstName()
        );

        assertEquals(
                "Laura",
                result.get(1).getFirstName()
        );

        verify(userRepository).findAll();
    }



    // FIND BY ID
    @Test
    void shouldFindUserById() {

        // Given
        User user = new User(
                "Laura",
                "Gomez",
                "33444555",
                "laura@mail.com",
                "1155554444"
        );

        when(userRepository.findById(3L))
                .thenReturn(Optional.of(user));


        // When
        UserResponseDTO result = userService.findById(3L);


        // Then
        assertEquals(
                "Laura",
                result.getFirstName()
        );

        assertEquals(
                "33444555",
                result.getDocumentNumber()
        );
    }

    // USER NOT FOUND
    @Test
    void shouldThrowWhenUserDoesNotExist() {

        // Given
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.findById(99L)
        );
        // Then
        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }

    // ACTIVATE
    @Test
    void shouldActivateUser() {
        // Given
        User user = new User(
                "Laura",
                "Gomez",
                "33444555",
                "laura@mail.com",
                "1155554444"
        );

        user.deactivate();

        when(userRepository.findById(3L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UserResponseDTO result = userService.activate(3L);

        // Then
        assertTrue(result.isActive());

        verify(userRepository).save(user);
    }


    // DEACTIVATE
    @Test
    void shouldDeactivateUser() {

        // Given
        User user = new User(
                "Laura",
                "Gomez",
                "33444555",
                "laura@mail.com",
                "1155554444"
        );

        when(userRepository.findById(3L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UserResponseDTO result = userService.deactivate(3L);

        // Then
        assertFalse(result.isActive());

        verify(userRepository).save(user);
    }



    // UPDATE
    @Test
    void shouldUpdateUser() {

        // Given
        User existingUser = new User(
                "Laura",
                "Gomez",
                "33444555",
                "laura@mail.com",
                "1155554444"
        );

        UserUpdateDTO dto = new UserUpdateDTO();

        dto.setFirstName("Laura");
        dto.setLastName("Gomez");
        dto.setDocumentNumber("33444555");
        dto.setEmail("laura.gomez@mail.com");
        dto.setPhone("1199999999");

        when(userRepository.findById(3L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UserResponseDTO result = userService.update(
                3L,
                dto
        );

        // Then
        assertEquals(
                "laura.gomez@mail.com",
                result.getEmail()
        );

        assertEquals(
                "1199999999",
                result.getPhone()
        );

        verify(userRepository).save(existingUser);
    }



    // FIND BY DOCUMENT NUMBER
    @Test
    void shouldFindUserByDocumentNumber() {

        // Given
        User user = new User(
                "Marcos",
                "Garcia",
                "30123456",
                "marcos@mail.com",
                "1122334455"
        );

        when(userRepository.findByDocumentNumber("30123456"))
                .thenReturn(Optional.of(user));

        // When
        UserResponseDTO result =
                userService.findByDocumentNumber("30123456");

        // Then
        assertEquals(
                "Marcos",
                result.getFirstName()
        );

        assertEquals(
                "30123456",
                result.getDocumentNumber()
        );
    }
    @Test
    void shouldRejectUserDeactivationWhenAccountHasBalance() {

        // Given
        User user = new User(
                "Laura",
                "Gomez",
                "33444555",
                "laura@mail.com",
                "1155554444"
        );

        Account account = new Account(
                "ACC-001",
                new BigDecimal("500.00"),
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        user.getAccounts().add(account);

        when(userRepository.findById(3L))
                .thenReturn(Optional.of(user));

        // When
        AccountHasBalanceException exception = assertThrows(
                AccountHasBalanceException.class,
                () -> userService.deactivate(3L)
        );

        // Then
        assertEquals(
                "User cannot be deactivated because one or more accounts have a non-zero balance",
                exception.getMessage()
        );

        verify(userRepository, never())
                .save(any(User.class));
    }
    @Test
    void shouldDeactivateUserAndAccountsWhenBalancesAreZero() {

        // Given
        User user = new User(
                "Laura",
                "Gomez",
                "33444555",
                "laura@mail.com",
                "1155554444"
        );

        Account account = new Account(
                "ACC-001",
                BigDecimal.ZERO,
                "ARS",
                true,
                LocalDate.now(),
                user
        );

        user.getAccounts().add(account);

        when(userRepository.findById(3L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UserResponseDTO result =
                userService.deactivate(3L);

        // Then
        assertFalse(result.isActive());
        assertFalse(account.isActive());

        verify(userRepository).save(user);
    }
}