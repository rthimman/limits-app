package com.stellantis.securitization.service;

import com.stellantis.securitization.dto.FileIntegrationListResponse;
import com.stellantis.securitization.dto.FileIntegrationResponse;
import com.stellantis.securitization.repository.FileIntegrationLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileIntegrationService {

    private final FileIntegrationLogRepository repository;

    public FileIntegrationService(FileIntegrationLogRepository repo) {
        this.repository = repo;
    }

    public FileIntegrationListResponse getFileIntegrationStatus(String fileType,
                                                                String countryCode,
                                                                String productCode) {

        List<FileIntegrationResponse> data = repository.findLatestFileIntegration(fileType, countryCode, productCode)
                .stream()
                .map(p -> FileIntegrationResponse.builder()
                        .fileType(p.getFileType())
                        .countryCode(p.getCountryCode())
                        .productCode(p.getProductCode())
                        .sourceFilename(p.getSourceFilename())
                        .accountingDate(p.getAccountingDate())
                        .integrationDate(p.getIntegrationDate())
                        .status(p.getStatus())
                        .totalRecords(p.getTotalRecords())
                        .recordsOk(p.getRecordsOk())
                        .recordsKo(p.getRecordsKo())
                        .build()
                )
                .toList();

        return new FileIntegrationListResponse(data);

    }
}
