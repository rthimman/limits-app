package com.stellantis.securitization.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LimitUpdateRequest {
    private List<Item> updates;

    @Data
    public static class Item {
        private String criteriaCode;
        private String operator;
        private BigDecimal thresholdValue;
    }
}
