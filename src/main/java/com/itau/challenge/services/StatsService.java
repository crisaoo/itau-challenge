package com.itau.challenge.services;

import com.itau.challenge.dtos.StatsDTO;
import com.itau.challenge.models.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final TransactionService transactionService;

    public StatsDTO getStats(int seconds){
        OffsetDateTime end = OffsetDateTime.now();
        OffsetDateTime start = end.minusSeconds(seconds);

        List<Transaction> transactions = transactionService.getTransactionsByDateTimeBetween(start, end);
        DoubleSummaryStatistics stats = transactions.stream()
                .mapToDouble(Transaction::getValue)
                .summaryStatistics();

        return new StatsDTO(stats);
    }
}
