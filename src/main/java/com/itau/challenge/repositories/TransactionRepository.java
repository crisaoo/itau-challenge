package com.itau.challenge.repositories;

import com.itau.challenge.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByDateTimeBetween(OffsetDateTime start, OffsetDateTime end);
}
