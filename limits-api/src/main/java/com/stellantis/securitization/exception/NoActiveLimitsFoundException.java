package com.stellantis.securitization.exception;

public class NoActiveLimitsFoundException extends RuntimeException {
    public NoActiveLimitsFoundException(String countryCode, String fundCode) {
        super("No active limits found for countryCode=" + countryCode + " and fundCode=" + fundCode);
    }
}
