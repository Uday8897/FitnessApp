package com.AuthService.exceptions;

public class UserNotValidException extends  Exception{
    public UserNotValidException(String message){
        super(message);
    }
}
