package com.itau.challenge.services;

import com.itau.challenge.dtos.StatsDTO;
import com.itau.challenge.models.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatsService {
    private final TransactionService transactionService;

    public StatsDTO getStats(int seconds){
        log.info("Getting stats for {} seconds", seconds);
        OffsetDateTime end = OffsetDateTime.now();
        OffsetDateTime start = end.minusSeconds(seconds);

        List<Transaction> transactions = transactionService.getTransactionsByDateTimeBetween(start, end);
        DoubleSummaryStatistics stats = transactions.stream()
                .mapToDouble(Transaction::getValue)
                .summaryStatistics();
        StatsDTO statsDTO = new StatsDTO(stats);
        log.info("Stats for {} seconds is {}", seconds, stats);
        return statsDTO;
    }
}
