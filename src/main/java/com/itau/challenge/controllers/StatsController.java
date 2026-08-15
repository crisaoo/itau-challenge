package com.itau.challenge.controllers;

import com.itau.challenge.dtos.StatsDTO;
import com.itau.challenge.services.StatsService;
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
    public StatsDTO getStats(@RequestParam(defaultValue = "60") int seconds){
        return service.getStats(seconds);
    }
}
