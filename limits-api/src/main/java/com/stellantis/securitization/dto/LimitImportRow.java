package com.stellantis.securitization.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LimitImportRow {
    private  String criteriaCode;
    private String labelEn;
    private String labelFr;
    private String process;
    private String operator;
    private BigDecimal thresholdValue;
    private String valueType;
}
