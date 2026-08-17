package com.itau.challenge.services;

import com.itau.challenge.dtos.StatsDTO;
import com.itau.challenge.models.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private StatsService service;

    @Test
    @DisplayName("Should calculate the stats correctly")
    void getStatsCase1() {
        // arrange
        OffsetDateTime now = OffsetDateTime.now();
        List<Transaction> transactions = List.of(
                new Transaction(UUID.randomUUID(), 100.00, now.minusSeconds(10)),
                new Transaction(UUID.randomUUID(), 200.00, now.minusSeconds(20)),
                new Transaction(UUID.randomUUID(), 300.00, now.minusSeconds(30))
        );
        StatsDTO expectedResult = new StatsDTO(3, 600.00, 200.00, 100.00, 300.00);
        when(transactionService.getTransactionsByDateTimeBetween(any(), any())).thenReturn(transactions);

        // act
        StatsDTO result = service.getStats(60);

        // assert
        verify(transactionService, times(1)).getTransactionsByDateTimeBetween(any(), any());
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should return 0 for all stats fields when there aren't transactions")
    void getStatsCase2() {
        // arrange
        StatsDTO expectedResult = new StatsDTO(0, 0.0, 0.0, 0.0, 0.0);
        when(transactionService.getTransactionsByDateTimeBetween(any(), any())).thenReturn(List.of());

        // act
        StatsDTO result = service.getStats(30);

        // assert
        verify(transactionService, times(1)).getTransactionsByDateTimeBetween(any(), any());
        Assertions.assertEquals(expectedResult, result);
    }
}
