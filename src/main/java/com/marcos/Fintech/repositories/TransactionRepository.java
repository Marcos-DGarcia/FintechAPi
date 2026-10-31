package com.marcos.Fintech.repositories;
import com.marcos.Fintech.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
