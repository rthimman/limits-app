package com.stellantis.securitization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class LimitHistoryResponse {
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private String criteriaCode;
    private BigDecimal oldValue;
    private BigDecimal newValue;
}
