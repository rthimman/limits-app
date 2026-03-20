package com.stellantis.securitization.exception;

public class MissingMandatoryFieldException extends RuntimeException{
    public MissingMandatoryFieldException(String message){
        super(message);
    }
}
