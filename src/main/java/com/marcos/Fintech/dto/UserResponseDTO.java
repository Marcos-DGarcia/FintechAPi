package com.marcos.Fintech.dto;

import java.time.LocalDate;

public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String email;
    private String phone;
    private boolean active;
    private LocalDate registrationDate;

    public UserResponseDTO(
            Long id,
            String firstName,
            String lastName,
            String documentNumber,
            String email,
            String phone,
            boolean active,
            LocalDate registrationDate
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.email = email;
        this.phone = phone;
        this.active = active;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
}