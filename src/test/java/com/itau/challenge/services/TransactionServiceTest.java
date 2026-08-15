package com.itau.challenge.services;

import com.itau.challenge.dtos.StatisticsDTO;
import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.infra.exceptions.BadTransactionException;
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
import java.util.List;
import java.util.UUID;

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
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
           TransactionDTO dto = new TransactionDTO(3000.0, OffsetDateTime.now().plusSeconds(1));
           service.createTransaction(dto);
        });

        Assertions.assertEquals("Transaction date cannot be in the future", thrown.getMessage());
        verify(repository, times(0)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw a IllegalArgumentException when the value is negative")
    void createTransactionCase4() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
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

    @Test
    @DisplayName("Should calculate the stats correctly")
    void getStatsCase1() {
        OffsetDateTime now = OffsetDateTime.now();

        List<Transaction> transactions = List.of(
            new Transaction(UUID.randomUUID(), 100.00, now.minusSeconds(10)),
            new Transaction(UUID.randomUUID(), 200.00, now.minusSeconds(20)),
            new Transaction(UUID.randomUUID(), 300.00, now.minusSeconds(30))
        );

        when(repository.findByDateTimeBetween(any(), any())).thenReturn(transactions);

        StatisticsDTO result = service.getStats(60);

        Assertions.assertEquals(3, result.count());
        Assertions.assertEquals(600.00, result.sum());
        Assertions.assertEquals(200.00, result.avg());
        Assertions.assertEquals(100.00, result.min());
        Assertions.assertEquals(300.00, result.max());
    }

    @Test
    @DisplayName("Should return 0 for all stats fields when there aren't transactions")
    void getStatsCase2() {
        when(repository.findByDateTimeBetween(any(), any())).thenReturn(List.of());

        StatisticsDTO result = service.getStats(30);

        Assertions.assertEquals(0, result.count());
        Assertions.assertEquals(0.00, result.sum());
        Assertions.assertEquals(0.00, result.avg());
        Assertions.assertEquals(0.00, result.min());
        Assertions.assertEquals(0.00, result.max());
    }

}
