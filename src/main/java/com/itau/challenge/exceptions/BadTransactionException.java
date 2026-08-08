package com.itau.challenge.exceptions;

public class BadTransactionException extends RuntimeException{
    public BadTransactionException(String message){
        super(message);
    }
}
