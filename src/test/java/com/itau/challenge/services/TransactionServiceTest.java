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
        // arrange
        TransactionDTO dto = new TransactionDTO(3000.0, OffsetDateTime.now());
        Transaction transactionSaved = new Transaction(UUID.randomUUID(), dto.value(), dto.dateTime());
        when(repository.save(any(Transaction.class))).thenReturn(transactionSaved);

        // act
        Transaction result = service.createTransaction(dto);

        // asserts
        Assertions.assertEquals(transactionSaved, result);
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw a BadTransactionException when any field is empty")
    void createTransactionCase2() throws BadTransactionException {
        // arrange
        TransactionDTO dto = new TransactionDTO(null, null);

        // act
        BadTransactionException thrown = Assertions.assertThrows(BadTransactionException.class,
                () -> service.createTransaction(dto)
        );

        // assert
        Assertions.assertEquals("Transaction date and value cannot be empty", thrown.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw a IllegalArgumentException when the date is in the future")
    void createTransactionCase3() {
        // arrange
        TransactionDTO dto = new TransactionDTO(3000.0, OffsetDateTime.now().plusSeconds(1));

        // act
        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class,
                () -> service.createTransaction(dto)
        );

        // assert
        Assertions.assertEquals("Transaction date cannot be in the future", thrown.getMessage());
        verify(repository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw a IllegalArgumentException when the value is negative")
    void createTransactionCase4() {
        // arrange
        TransactionDTO dto = new TransactionDTO(-500.0, OffsetDateTime.now());

        // act
        UnprocessableEntityException thrown = Assertions.assertThrows(UnprocessableEntityException.class,
                () -> service.createTransaction(dto)
        );

        // assert
        Assertions.assertEquals("Transaction value cannot be negative", thrown.getMessage());
        verify(repository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should delete all transactions in database")
    void deleteAllTransactions() {
        // act
        service.deleteAllTransactions();

        // assert
        verify(repository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Should get a list of transactions in database")
    void getTransactionsByDateTimeBetween(){
        // arrange
        OffsetDateTime now = OffsetDateTime.now();
        List<Transaction> expectedResult = List.of(
                new Transaction(UUID.randomUUID(), 100.00, now.minusSeconds(10)),
                new Transaction(UUID.randomUUID(), 200.00, now.minusSeconds(20)),
                new Transaction(UUID.randomUUID(), 300.00, now.minusSeconds(30))
        );
        when(repository.findByDateTimeBetween(any(OffsetDateTime.class), any(OffsetDateTime.class))).thenReturn(expectedResult);

        // act
        List<Transaction> result = service.getTransactionsByDateTimeBetween(now.minusSeconds(60), now);

        // assert
        verify(repository, times(1)).findByDateTimeBetween(any(OffsetDateTime.class), any(OffsetDateTime.class));
        Assertions.assertEquals(expectedResult, result);
    }
}
