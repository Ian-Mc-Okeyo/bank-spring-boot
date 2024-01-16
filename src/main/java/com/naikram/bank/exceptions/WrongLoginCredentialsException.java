package com.naikram.bank.exceptions;

public class WrongLoginCredentialsException extends RuntimeException{
    public WrongLoginCredentialsException(String message){
        super(message);
    }
}
