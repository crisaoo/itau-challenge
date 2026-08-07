package com.itau.challenge.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Transaction {
    // TODO: ORM
    private long id;
    private double value;
    private OffsetDateTime dateTime;
}
