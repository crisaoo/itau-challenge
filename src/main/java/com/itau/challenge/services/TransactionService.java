package com.itau.challenge.services;

import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.infra.exceptions.BadTransactionException;
import com.itau.challenge.infra.exceptions.UnprocessableEntityException;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository repository;

    @Transactional
    public Transaction createTransaction(TransactionDTO dto){
        log.info("Creating Transaction: {}", dto);
        checkTransactionBody(dto);
        Transaction transaction = repository.save(new Transaction(dto));
        log.info("Transaction created: {}", transaction);
        return transaction;
    }

    @Transactional
    public void deleteAllTransactions() {
        log.info("Deleting all Transactions");
        repository.deleteAll();
        log.info("All transactions deleted");
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateTimeBetween(OffsetDateTime start, OffsetDateTime end){
        log.info("Getting transactions between {} and {}", start, end);
        List<Transaction> transactions = repository.findByDateTimeBetween(start, end);
        log.info("Transactions found: {}", transactions);
        return transactions;
    }

    private void checkTransactionBody(TransactionDTO dto) {
        if(dto.dateTime() == null || dto.value() == null)
            throw new BadTransactionException("Transaction date and value cannot be empty");
        if(dto.dateTime().isAfter(OffsetDateTime.now()))
            throw new UnprocessableEntityException("Transaction date cannot be in the future");
        if(dto.value() < 0)
            throw new UnprocessableEntityException("Transaction value cannot be negative");
    }
}
