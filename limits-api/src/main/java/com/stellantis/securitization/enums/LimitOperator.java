package com.stellantis.securitization.enums;

import lombok.Getter;

@Getter
public enum LimitOperator {

    LTE("<="),
    GTE(">=");

    private final String symbol;

    LimitOperator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static LimitOperator fromSymbol(String symbol) {
        return "<=".equals(symbol) ? LTE : GTE;
    }
}
