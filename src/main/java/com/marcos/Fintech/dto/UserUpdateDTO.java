package com.marcos.Fintech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserUpdateDTO {

    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Document number is required")
    private String documentNumber;
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;
    @NotBlank(message = "Phone is required")
    private String phone;

    public UserUpdateDTO() {
    }

    // Getters
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getDocumentNumber() {return documentNumber;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}

    // Setters
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setDocumentNumber(String documentNumber) {this.documentNumber = documentNumber;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}
}