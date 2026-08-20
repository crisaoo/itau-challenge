package com.itau.challenge.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.infra.RestExceptionHandler;
import com.itau.challenge.infra.exceptions.BadTransactionException;
import com.itau.challenge.infra.exceptions.UnprocessableEntityException;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @InjectMocks
    private TransactionController controller;

    @Mock
    private TransactionService service;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Should return a 201 response with the transaction created")
    void createTransactionCase1() throws Exception {
        // arrange
        OffsetDateTime dateTime = OffsetDateTime.of(2026, 8, 18, 10, 52, 10, 0, ZoneOffset.UTC);
        TransactionDTO dto = new TransactionDTO(300.00, dateTime);
        Transaction transactionCreated = new Transaction(UUID.randomUUID(), dto.value(), dto.dateTime());
        when(service.createTransaction(any(TransactionDTO.class))).thenReturn(transactionCreated);

        // act + assert
        mockMvc.perform(post("/transaction")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transactionCreated.getId().toString()))
                .andExpect(jsonPath("$.value").value(transactionCreated.getValue()))
                .andExpect(jsonPath("$.dateTime").value(transactionCreated.getDateTime().toString()));
    }

    @Test
    @DisplayName("Should return a 422 response when the fields don't meet the transaction requirements")
    void createTransactionCase2() throws Exception {
        // arrange
        OffsetDateTime dateTime = OffsetDateTime.of(2026, 8, 18, 10, 52, 10, 0, ZoneOffset.UTC);
        TransactionDTO dto = new TransactionDTO(-300.0, dateTime);
        doThrow(new UnprocessableEntityException("Transaction value cannot be negative"))
                .when(service)
                .createTransaction(any(TransactionDTO.class));

        // act + assert
        mockMvc.perform(post("/transaction")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Transaction value cannot be negative"));
    }

    @Test
    @DisplayName("Should return a 400 response when the json request is invalid")
    void createTransactionCase3() throws Exception {
        // arrange
        TransactionDTO dto = new TransactionDTO(null, null);
        doThrow(new BadTransactionException("Transaction date and value cannot be empty"))
                .when(service)
                .createTransaction(any(TransactionDTO.class));

        // act + assert
        mockMvc.perform(post("/transaction")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Transaction date and value cannot be empty"));
    }

    @Test
    @DisplayName("Should return a 200 response when deleting all transactions in database")
    void deleteAllTransactions() throws Exception {
        // arrange
        doNothing().when(service).deleteAllTransactions();

        // act + assert
        mockMvc.perform(delete("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
