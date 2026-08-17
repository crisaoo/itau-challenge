package com.itau.challenge.controllers;

import com.itau.challenge.dtos.TransactionDTO;
import com.itau.challenge.models.Transaction;
import com.itau.challenge.services.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(description = "Endpoint responsible for create transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "422", description = "Fields don't meet the transaction requirements"),
            @ApiResponse(responseCode = "400", description = "Invalid JSON"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public Transaction createTransaction(@RequestBody TransactionDTO transactionDTO){
        return service.createTransaction(transactionDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    @Operation(description = "Endpoint responsible for delete all transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All transactions were deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public void deleteAllTransactions(){
        service.deleteAllTransactions();
    }
}
