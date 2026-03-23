package com.stellantis.securitization.controller;

import com.stellantis.securitization.dto.FileIntegrationListResponse;
import com.stellantis.securitization.service.FileIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileIntegrationController {

    private final FileIntegrationService service;

    @GetMapping("/file-integration")
    public ResponseEntity<FileIntegrationListResponse> getFileIntegrationStatus(
            @RequestParam(name = "file_type", required = false) String fileType,
            @RequestParam(name = "country_code", required = false) String countryCode,
            @RequestParam(name = "product_code", required = false) String productCode) {

        FileIntegrationListResponse response =
                service.getFileIntegrationStatus(fileType, countryCode, productCode);

        return ResponseEntity.ok(response);
    }

}
