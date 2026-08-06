package com.itau.challenge.service;

import com.itau.challenge.dto.TransactionDTO;
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
        // TODO: Create a custom exceptions
        if(dto.dateTime() == null || dto.value() == null)
            throw new IllegalArgumentException("The date and value must not be null");
        if(dto.dateTime().isAfter(OffsetDateTime.now()))
            throw new IllegalArgumentException("The date must not be in the future");
        if(dto.value() < 0)
            throw new IllegalArgumentException("The value must not be negative");

        Transaction transaction = new Transaction();
        transaction.setValue(dto.value());
        transaction.setDateTime(dto.dateTime());
//        repository.createTransaction(dto);
    }

}
