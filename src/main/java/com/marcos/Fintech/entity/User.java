package com.marcos.Fintech.entity;
import com.marcos.Fintech.interfaces.Activatable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Activatable {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts = new ArrayList<>();

    //Empty constructor
    public User(){
    }

    //Constructor
    public User(String firstName, String lastName, String documentNumber, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.email = email;
        this.phone = phone;
        this.registrationDate = LocalDate.now();
        this.active = true;
    }

    //Getters
    public Long getId() {return id;}
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {return lastName;}
    public String getDocumentNumber() {
        return documentNumber;
    }
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public boolean isActive() {
        return active;
    }
    public List<Account> getAccounts() {return accounts;}

    //Setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Concrete methods
    @Override
    public void activate() {
        this.active = true;
    }
    @Override
    public void deactivate() {
        this.active = false;
    }

}
