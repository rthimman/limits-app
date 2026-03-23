package com.stellantis.securitization.entity;

import com.stellantis.securitization.enums.FileStatus;
import com.stellantis.securitization.enums.FileType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_FILE_INTEGRATION_LOG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileIntegrationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "FILE_TYPE", nullable = false)
    private FileType fileType;

    @Column(name = "COUNTRY_CODE", length = 3)
    private String countryCode;

    @Column(name = "PRODUCT_CODE", length = 10)
    private String productCode;

    @Column(name = "SOURCE_FILENAME", length = 255, nullable = false)
    private String sourceFilename;

    @Column(name = "ACCOUNTING_DATE")
    private LocalDate accountingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private FileStatus status;

    @Column(name = "TOTAL_RECORDS")
    private Integer totalRecords;

    @Column(name = "RECORDS_OK")
    private Integer recordsOk;

    @Column(name = "RECORDS_KO")
    private Integer recordsKo;

    @Column(name = "ERROR_MESSAGE", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "INTEGRATION_DATE", nullable = false)
    private LocalDateTime integrationDate;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

}
