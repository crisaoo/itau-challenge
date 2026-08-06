package com.itau.challenge.controller;

import com.itau.challenge.dto.TransactionDTO;
import com.itau.challenge.service.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService service;

    // TODO: map responses 201, 422 and 400
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createTransaction(@RequestBody TransactionDTO transactionDTO){
        service.createTransaction(transactionDTO);
    }
}
