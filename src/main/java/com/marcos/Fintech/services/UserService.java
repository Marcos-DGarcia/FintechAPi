package com.marcos.Fintech.services;
import com.marcos.Fintech.dto.UserCreateDTO;
import com.marcos.Fintech.dto.UserResponseDTO;
import com.marcos.Fintech.dto.UserUpdateDTO;
import com.marcos.Fintech.entity.Account;
import com.marcos.Fintech.entity.User;
import com.marcos.Fintech.exceptions.AccountHasBalanceException;
import com.marcos.Fintech.exceptions.UserNotFoundException;
import com.marcos.Fintech.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    public UserResponseDTO create(UserCreateDTO dto) {

        User user = new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getDocumentNumber(),
                dto.getEmail(),
                dto.getPhone()
        );
        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    // FIND ALL
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // FIND BY ID
    public UserResponseDTO findById(Long id) {

        User user = findEntityById(id);

        return toResponseDTO(user);
    }

    // FIND BY DOCUMENT NUMBER
    public UserResponseDTO findByDocumentNumber(String documentNumber) {

        User user = userRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return toResponseDTO(user);
    }

    // UPDATE
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {

        User user = findEntityById(id);

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        User updatedUser = userRepository.save(user);

        return toResponseDTO(updatedUser);
    }

    // ACTIVATE
    public UserResponseDTO activate(Long id) {

        User user = findEntityById(id);

        user.activate();

        User updatedUser = userRepository.save(user);

        return toResponseDTO(updatedUser);
    }

    // DEACTIVATE
    @Transactional
    public UserResponseDTO deactivate(Long id) {

        User user = findEntityById(id);

        for (Account account : user.getAccounts()) {
            if (account.getBalance().compareTo(BigDecimal.ZERO) !=0){
                throw  new AccountHasBalanceException("User cannot be deactivated because one or more accounts have a non-zero balance");
            }
        }

        for (Account account : user.getAccounts()) {
            account.deactivate();
        }

        user.deactivate();

        User updatedUser = userRepository.save(user);

        return toResponseDTO(updatedUser);
    }

    // INTERNAL METHOD - FIND ENTITY
    private User findEntityById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // ENTITY -> RESPONSE DTO
    private UserResponseDTO toResponseDTO(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getDocumentNumber(),
                user.getEmail(),
                user.getPhone(),
                user.isActive(),
                user.getRegistrationDate()
        );
    }
}