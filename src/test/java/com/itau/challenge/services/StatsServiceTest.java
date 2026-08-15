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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private StatsService service;

    @Test
    @DisplayName("Should calculate the stats correctly")
    void getStatsCase1() {
        OffsetDateTime now = OffsetDateTime.now();

        List<Transaction> transactions = List.of(
                new Transaction(UUID.randomUUID(), 100.00, now.minusSeconds(10)),
                new Transaction(UUID.randomUUID(), 200.00, now.minusSeconds(20)),
                new Transaction(UUID.randomUUID(), 300.00, now.minusSeconds(30))
        );

        when(transactionService.getTransactionsByDateTimeBetween(any(), any())).thenReturn(transactions);

        StatsDTO result = service.getStats(60);

        Assertions.assertEquals(3, result.count());
        Assertions.assertEquals(600.00, result.sum());
        Assertions.assertEquals(200.00, result.avg());
        Assertions.assertEquals(100.00, result.min());
        Assertions.assertEquals(300.00, result.max());
    }

    @Test
    @DisplayName("Should return 0 for all stats fields when there aren't transactions")
    void getStatsCase2() {
        when(transactionService.getTransactionsByDateTimeBetween(any(), any())).thenReturn(List.of());

        StatsDTO result = service.getStats(30);

        Assertions.assertEquals(0, result.count());
        Assertions.assertEquals(0.00, result.sum());
        Assertions.assertEquals(0.00, result.avg());
        Assertions.assertEquals(0.00, result.min());
        Assertions.assertEquals(0.00, result.max());
    }
}
