package com.stellantis.securitization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LimitHistoryResponse {
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private String criteriaCode;
    private String labelEn;
    private BigDecimal oldValue;
    private BigDecimal newValue;
    private String source;
}
