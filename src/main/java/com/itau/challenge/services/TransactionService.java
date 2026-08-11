package com.itau.challenge.services;

import com.itau.challenge.dtos.StatisticsDTO;
import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.exceptions.BadTransactionException;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
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
    public StatisticsDTO getStats(int seconds){
        OffsetDateTime end = OffsetDateTime.now();
        OffsetDateTime start = end.minusSeconds(seconds);

        List<Transaction> transactions = repository.findByDateTimeBetween(start, end);
        DoubleSummaryStatistics stats = transactions.stream().mapToDouble(Transaction::getValue).summaryStatistics();

        return new StatisticsDTO(
            (int) stats.getCount(),
            stats.getSum(),
            stats.getAverage(),
            Double.isInfinite(stats.getMin())? 0.0 : stats.getMin(),
            Double.isInfinite(stats.getMax())? 0.0 : stats.getMax());
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
