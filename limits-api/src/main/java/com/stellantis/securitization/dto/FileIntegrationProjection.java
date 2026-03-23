package com.stellantis.securitization.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface FileIntegrationProjection {

    String getFileType();

    String getCountryCode();

    String getProductCode();

    String getSourceFilename();

    LocalDate getAccountingDate();

    LocalDateTime getIntegrationDate();

    String getStatus();

    Integer getTotalRecords();

    Integer getRecordsOk();

    Integer getRecordsKo();

}
