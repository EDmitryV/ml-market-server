package com.edmitryv.ml_market_server.authentication.exceptions;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException(String message){
        super(message);
    }
}
