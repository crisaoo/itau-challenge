package com.itau.challenge.infra;

import com.itau.challenge.exception.BadTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestBodyResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        RestBodyResponse body = new RestBodyResponse(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
        return ResponseEntity.status(body.getStatus()).body(body);
    }

    @ExceptionHandler(BadTransactionException.class)
    public ResponseEntity<RestBodyResponse> handleBadTransactionException(BadTransactionException ex){
        RestBodyResponse body = new RestBodyResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(body.getStatus()).body(body);
    }
}
