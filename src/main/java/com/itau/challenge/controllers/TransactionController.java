package com.itau.challenge.controllers;

import com.itau.challenge.dtos.StatisticsDTO;
import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.services.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionDTO transactionDTO){
        return service.createTransaction(transactionDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteAllTransactions(){
        service.deleteAllTransactions();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/statistic")
    public StatisticsDTO getStats(@RequestParam(defaultValue = "60") int seconds){
        return service.getStats(seconds);
    }
}
