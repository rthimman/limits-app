package com.stellantis.securitization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LimitResponseDto {
    private String limitName;
    private String labelEn;
    private String operator;
    private BigDecimal thresholdValue;
    private String valueType;
    private Integer displayOrder;

}
