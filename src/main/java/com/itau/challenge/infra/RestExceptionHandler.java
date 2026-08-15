package com.itau.challenge.infra;

import com.itau.challenge.infra.exceptions.BadTransactionException;
import com.itau.challenge.infra.exceptions.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<RestBodyResponse> handleUnprocessableEntityException(UnprocessableEntityException ex){
        return handleException(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(BadTransactionException.class)
    public ResponseEntity<RestBodyResponse> handleBadTransactionException(BadTransactionException ex){
        return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<RestBodyResponse> handleException(HttpStatus status, String message){
        log.error(message);
        RestBodyResponse body = new RestBodyResponse(status, message);
        return ResponseEntity.status(body.getStatus()).body(body);
    }
}
