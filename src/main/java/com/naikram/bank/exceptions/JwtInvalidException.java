package com.naikram.bank.exceptions;
import org.springframework.security.core.AuthenticationException;

public class JwtInvalidException extends AuthenticationException{
    public JwtInvalidException(String message){
        super(message);
    }
}
