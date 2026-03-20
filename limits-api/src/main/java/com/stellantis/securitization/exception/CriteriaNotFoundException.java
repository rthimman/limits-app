package com.stellantis.securitization.exception;

public class CriteriaNotFoundException extends RuntimeException{
    public CriteriaNotFoundException(String criteriaCode) {
        super("CriteriaCode not found: " + criteriaCode);
    }
}
