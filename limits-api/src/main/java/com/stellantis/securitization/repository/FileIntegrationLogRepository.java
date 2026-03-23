package com.stellantis.securitization.repository;

import com.stellantis.securitization.dto.FileIntegrationProjection;
import com.stellantis.securitization.entity.FileIntegrationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileIntegrationLogRepository extends JpaRepository<FileIntegrationLog, Long> {

    @Query(value = """
                SELECT DISTINCT ON (fil.file_type, fil.country_code, fil.product_code)
                       fil.file_type AS fileType,
                       fil.country_code AS countryCode,
                       fil.product_code AS productCode,
                       fil.source_filename AS sourceFilename,
                       fil.accounting_date AS accountingDate,
                       fil.integration_date AS integrationDate,
                       fil.status AS status,
                       fil.total_records AS totalRecords,
                       fil.records_ok AS recordsOk,
                       fil.records_ko AS recordsKo
                FROM t_file_integration_log fil
                WHERE (:fileType IS NULL OR fil.file_type = :fileType)
                  AND (:countryCode IS NULL OR fil.country_code = :countryCode)
                  AND (:productCode IS NULL OR fil.product_code = :productCode)
                ORDER BY fil.file_type,
                         fil.country_code,
                         fil.product_code,
                         fil.integration_date DESC
            """, nativeQuery = true)
    List<FileIntegrationProjection> findLatestFileIntegration(
            @Param("fileType") String fileType,
            @Param("countryCode") String countryCode,
            @Param("productCode") String productCode
    );

}
