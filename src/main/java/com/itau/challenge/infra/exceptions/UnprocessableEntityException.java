package com.itau.challenge.infra.exceptions;

public class UnprocessableEntityException extends RuntimeException{
    public UnprocessableEntityException(String message){super(message);}
}
