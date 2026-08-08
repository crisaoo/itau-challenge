package com.itau.challenge.dtos;

import java.time.OffsetDateTime;

public record TransactionDTO(Double value, OffsetDateTime dateTime){}
