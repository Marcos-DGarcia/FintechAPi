package com.marcos.Fintech.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {

    private Long id;
    private BigDecimal amount;
    private String type;
    private LocalDateTime transactionDate;
    private String description;
    private Long originAccountId;
    private Long destinationAccountId;

    public TransactionResponseDTO(
            Long id,
            BigDecimal amount,
            String type,
            LocalDateTime transactionDate,
            String description,
            Long originAccountId,
            Long destinationAccountId
    ) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
        this.description = description;
        this.originAccountId = originAccountId;
        this.destinationAccountId = destinationAccountId;
    }

    // Getters
    public Long getId() {return id;}
    public BigDecimal getAmount() {return amount;}
    public String getType() {return type;}
    public LocalDateTime getTransactionDate() {return transactionDate;}
    public String getDescription() {return description;}
    public Long getOriginAccountId() {return originAccountId;}
    public Long getDestinationAccountId() {return destinationAccountId;}
}