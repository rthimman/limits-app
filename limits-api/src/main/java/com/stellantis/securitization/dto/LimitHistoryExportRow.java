package com.stellantis.securitization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitHistoryExportRow {
    private LocalDateTime timestamp;
    private String modifiedBy;
    private String limitName;
    private BigDecimal oldValue;
    private BigDecimal newValue;
    private String oldOperator;
    private String newOperator;
}
