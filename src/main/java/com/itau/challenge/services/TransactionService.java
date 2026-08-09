package com.itau.challenge.services;

import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.exceptions.BadTransactionException;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository repository;

    @Transactional
    public Transaction createTransaction(TransactionDTO dto){
        checkTransactionBody(dto);

        Transaction transaction = new Transaction();
        transaction.setValue(dto.value());
        transaction.setDateTime(dto.dateTime());
        return repository.save(transaction);
    }

    private void checkTransactionBody(TransactionDTO dto) {
        if(dto.dateTime() == null || dto.value() == null)
            throw new BadTransactionException("Transaction date and value cannot be empty");
        if(dto.dateTime().isAfter(OffsetDateTime.now()))
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        if(dto.value() < 0)
            throw new IllegalArgumentException("Transaction value cannot be negative");
    }

}
