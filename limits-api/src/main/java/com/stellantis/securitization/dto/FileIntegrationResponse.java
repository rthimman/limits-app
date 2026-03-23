package com.stellantis.securitization.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class FileIntegrationResponse {
    private String fileType;
    private String countryCode;
    private String productCode;
    private String sourceFilename;
    private LocalDate accountingDate;
    private LocalDateTime integrationDate;
    private String status;
    private Integer totalRecords;
    private Integer recordsOk;
    private Integer recordsKo;

}
