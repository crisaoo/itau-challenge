package com.itau.challenge.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class RestBodyResponse {
    private HttpStatus status;
    private String message;
}
