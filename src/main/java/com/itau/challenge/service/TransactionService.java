package com.itau.challenge.service;

import com.itau.challenge.dto.TransactionDTO;
import com.itau.challenge.exception.BadTransactionException;
import com.itau.challenge.model.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Service
public class TransactionService {
//    private final TransactionRepository repository;

    @Transactional
    public void createTransaction(TransactionDTO dto){
        checkTransactionBody(dto);

        Transaction transaction = new Transaction();
        transaction.setValue(dto.value());
        transaction.setDateTime(dto.dateTime());
//        return repository.createTransaction(dto);
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
