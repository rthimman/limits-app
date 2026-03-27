package com.stellantis.securitization.exception;

public class FundNotFoundException extends RuntimeException{
    public FundNotFoundException(String countryCode, String fundCode) {
        super("Fund not found for Country: " + countryCode + ", Fund: " + fundCode);
    }
}
