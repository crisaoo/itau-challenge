package com.itau.challenge.repositories;

import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.models.Transaction;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest{
    @Autowired
    EntityManager entityManager;
    @Autowired
    TransactionRepository repository;

    @Test
    @DisplayName("Should get 3 first transactions from 5 below between two dates from DB")
    void findByDateTimeBetweenCase1() {
        OffsetDateTime start = OffsetDateTime.now();
        OffsetDateTime end = OffsetDateTime.now().plusSeconds(60);

        TransactionDTO dto1 = new TransactionDTO(3000.0, start.plusSeconds(10));
        TransactionDTO dto2 = new TransactionDTO(5322.57, start.plusSeconds(20));
        TransactionDTO dto3 = new TransactionDTO(1199.99, start.plusSeconds(30));
        TransactionDTO dto4 = new TransactionDTO(630.2, start.minusSeconds(10));
        TransactionDTO dto5 = new TransactionDTO(2170.5, start.plusSeconds(70));

        createTransaction(dto1, dto2, dto3, dto4, dto5);

        List<Transaction> result = repository.findByDateTimeBetween(start, end);

        assertThat(result).hasSize(3);
    }

    private void createTransaction(TransactionDTO... dtos){
        for(TransactionDTO dto : dtos){
            Transaction transaction = new Transaction(dto);
            entityManager.persist(transaction);
        }
    }
}
