package com.itau.challenge.exception;

public class BadTransactionException extends RuntimeException{
    public BadTransactionException(String message){
        super(message);
    }
}
