package com.AuthService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotValidException.class)
    public ResponseEntity<?> handleJwtSignatureNotaValid(){
        System.out.println("Error Occured");

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
