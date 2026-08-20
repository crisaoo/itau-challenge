package com.itau.challenge.controllers;

import com.itau.challenge.dtos.StatsDTO;
import com.itau.challenge.services.StatsService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StatsControllerTest {
    @InjectMocks
    private StatsController controller;

    @Mock
    private StatsService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Should return a 200 response with the stats calculated correctly")
    void getStatsCase1() throws Exception{
        // arrange
        StatsDTO stats = new StatsDTO(3, 600.00, 200.00, 100.00, 300.00);
        when(service.getStats(60)).thenReturn(stats);

        // act + assert
        mockMvc.perform(get("/stats")
                .param("seconds", "60")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(stats.count()))
                .andExpect(jsonPath("$.sum").value(stats.sum()))
                .andExpect(jsonPath("$.avg").value(stats.avg()))
                .andExpect(jsonPath("$.min").value(stats.min()))
                .andExpect(jsonPath("$.max").value(stats.max()));
    }

    @Test
    @DisplayName("Should return a 400 response when the param type is incorrect")
    void getStatsCase2() throws Exception{
        // act + assert
        mockMvc.perform(get("/stats")
                .param("seconds", "60 segundos")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}
