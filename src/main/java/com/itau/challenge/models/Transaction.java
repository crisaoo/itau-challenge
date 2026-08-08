package com.itau.challenge.models;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Transaction {
    // TODO: ORM
    private long id;
    private double value;
    private OffsetDateTime dateTime;
}
