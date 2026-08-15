package com.itau.challenge.infra.exceptions;

public class BadTransactionException extends RuntimeException{
    public BadTransactionException(String message){
        super(message);
    }
}
