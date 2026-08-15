package com.itau.challenge.services;

import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.infra.exceptions.BadTransactionException;
import com.itau.challenge.infra.exceptions.UnprocessableEntityException;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.repositories.TransactionRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;


    @Test
    @DisplayName("Should create a transaction successfully")
    void createTransactionCase1() {
        TransactionDTO dto = new TransactionDTO(3000.0, OffsetDateTime.now());
        service.createTransaction(dto);

        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw a BadTransactionException when any field is empty")
    void createTransactionCase2() throws BadTransactionException {
        BadTransactionException thrown = Assertions.assertThrows(BadTransactionException.class, () -> {
            TransactionDTO dto = new TransactionDTO(null, null);
            service.createTransaction(dto);
        });

        Assertions.assertEquals("Transaction date and value cannot be empty", thrown.getMessage());
        verify(repository, times(0)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw a IllegalArgumentException when the date is in the future")
    void createTransactionCase3() {
        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
           TransactionDTO dto = new TransactionDTO(3000.0, OffsetDateTime.now().plusSeconds(1));
           service.createTransaction(dto);
        });

        Assertions.assertEquals("Transaction date cannot be in the future", thrown.getMessage());
        verify(repository, times(0)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw a IllegalArgumentException when the value is negative")
    void createTransactionCase4() {
        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            TransactionDTO dto = new TransactionDTO(-500.0, OffsetDateTime.now());
            service.createTransaction(dto);
        });

        Assertions.assertEquals("Transaction value cannot be negative", thrown.getMessage());
        verify(repository, times(0)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should delete all transactions in database")
    void deleteAllTransactions() {
        service.deleteAllTransactions();
        verify(repository, times(1)).deleteAll();
    }
}
