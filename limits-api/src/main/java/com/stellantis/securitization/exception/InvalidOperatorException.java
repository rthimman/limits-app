package com.stellantis.securitization.exception;

public class InvalidOperatorException extends RuntimeException{
    public InvalidOperatorException(String operator) {
        super("Invalid operator: " + operator + ". Allowed operators: <=, >=");
    }

}
