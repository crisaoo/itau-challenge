package com.itau.challenge.services;

import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.infra.exceptions.BadTransactionException;
import com.itau.challenge.infra.exceptions.UnprocessableEntityException;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository repository;

    @Transactional
    public Transaction createTransaction(TransactionDTO dto){
        checkTransactionBody(dto);
        return repository.save(new Transaction(dto));
    }

    @Transactional
    public void deleteAllTransactions() {
        repository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateTimeBetween(OffsetDateTime start, OffsetDateTime end){
        return repository.findByDateTimeBetween(start, end);
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
