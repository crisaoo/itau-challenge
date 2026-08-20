package com.itau.challenge.controllers;

import com.itau.challenge.dtos.StatsDTO;
import com.itau.challenge.services.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stats")
public class StatsController {
    private final StatsService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(description = "Endpoint responsible for calculating statistics on recent transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid params"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public StatsDTO getStats(@RequestParam(defaultValue = "60") int seconds){
        return service.getStats(seconds);
    }
}
