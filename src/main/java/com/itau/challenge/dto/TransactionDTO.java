package com.itau.challenge.dto;

import java.time.OffsetDateTime;

public record TransactionDTO(Double value, OffsetDateTime dateTime){}
