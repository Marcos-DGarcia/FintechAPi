package com.marcos.Fintech.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String type;

    @Column(name ="transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    private String description;

    @ManyToOne
    @JoinColumn(name ="origin_account_id")
    Account originAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    Account destinationAccount;

    //Empty constructor
    public Transaction() {}

    //Constructor
    public Transaction( BigDecimal amount, String type, LocalDateTime transactionDate, String description, Account originAccount, Account destinationAccount) {
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
        this.description = description;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
    }

    //Getters
    public Long getId() {return id;}
    public BigDecimal getAmount() {return amount;}
    public String getType() {return type;}
    public LocalDateTime getTransactionDate() {return transactionDate;}
    public String getDescription() {return description;}
    public Account getOriginAccount() {return originAccount;}
    public Account getDestinationAccount() {return destinationAccount;}

    //Setters
    public void setAmount(BigDecimal amount) {this.amount = amount;}
    public void setType(String type) {this.type = type;}
    public void setTransactionDate(LocalDateTime transactionDate) {this.transactionDate = transactionDate;}
    public void setDescription(String description) {this.description = description;}
    public void setOriginAccount(Account originAccount) {this.originAccount = originAccount;}
    public void setDestinationAccount(Account destinationAccount) {this.destinationAccount = destinationAccount;}















}
