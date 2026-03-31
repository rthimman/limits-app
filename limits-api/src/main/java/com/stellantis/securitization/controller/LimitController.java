package com.stellantis.securitization.controller;

import com.stellantis.securitization.dto.LimitHistoryResponse;
import com.stellantis.securitization.dto.LimitListResponse;
import com.stellantis.securitization.dto.LimitUpdateRequest;
import com.stellantis.securitization.dto.UploadLimitResponse;
import com.stellantis.securitization.service.LimitConfigService;
import com.stellantis.securitization.service.LimitHistoryExportService;
import com.stellantis.securitization.service.LimitImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.stellantis.securitization.util.LimitApplicationConstants.BASE_ENDPOINT;
import static com.stellantis.securitization.util.LimitApplicationConstants.CONTENT_DISPOSITION;
import static com.stellantis.securitization.util.LimitApplicationConstants.LIMIT_HEADER_VALUES;
import static com.stellantis.securitization.util.LimitApplicationConstants.EXCEL_MEDIA_TYPE;
import static com.stellantis.securitization.util.LimitApplicationConstants.ERROR;

@RestController
@RequestMapping(BASE_ENDPOINT)
@RequiredArgsConstructor
public class LimitController {
    private final LimitConfigService limitService;
    private final LimitImportService importService;
    private final LimitHistoryExportService historyExportService;

    @GetMapping("/{countryCode}/{fundCode}/limits")
    public ResponseEntity<LimitListResponse> getLimits(@PathVariable String countryCode,
                                                       @PathVariable String fundCode) {
        return ResponseEntity.ok(
                limitService.getLimits(countryCode, fundCode)
        );
    }

    @PutMapping("/{countryCode}/{fundCode}/limits")
    public ResponseEntity<?> updateLimits(
            @PathVariable String countryCode,
            @PathVariable String fundCode,
            @RequestBody LimitUpdateRequest request) {

        int updatedCount = limitService.updateLimits(countryCode, fundCode, request);
        return ResponseEntity.ok(Map.of("updatedCount", updatedCount));
    }

    @DeleteMapping("/{countryCode}/{fundCode}/limits/{criteriaCode}")
    public ResponseEntity<Map<String, Integer>> deleteLimit(
            @PathVariable String countryCode,
            @PathVariable String fundCode,
            @PathVariable String criteriaCode) {

        int deletedCount = limitService.deleteLimit(countryCode, fundCode, criteriaCode);

        return ResponseEntity.ok(Map.of("deletedCount", deletedCount));
    }

    @GetMapping("/{countryCode}/{fundCode}/limits/export")
    public ResponseEntity<byte[]> exportLimits(@PathVariable String countryCode,@PathVariable String fundCode) throws IOException {
        byte[] excelFile = limitService.exportLimits(countryCode, fundCode);

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, LIMIT_HEADER_VALUES)
                .contentType(MediaType.parseMediaType(EXCEL_MEDIA_TYPE))
                .body(excelFile);
    }

    @PostMapping(value = "/{countryCode}/{fundCode}/limits/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadLimitResponse> importLimits(
            @PathVariable String countryCode,
            @PathVariable String fundCode,
            @RequestParam("file") MultipartFile file,
            @RequestParam("userSsoId") String userSsoId) {

        try {
            UploadLimitResponse response =
                    importService.importLimits(countryCode, fundCode, file, userSsoId);

            if (ERROR.equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            UploadLimitResponse error = UploadLimitResponse.builder()
                    .status(ERROR)
                    .message(List.of("Unexpected failure during import: " + e.getMessage()))
                    .totalRecords("0 Limits Imported")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/{countryCode}/{fundCode}/limits/history/export")
    public ResponseEntity<List<LimitHistoryResponse>> getLimitConfigHistory(
            @PathVariable String countryCode,
            @PathVariable String fundCode) {
        List<LimitHistoryResponse> history =
                historyExportService.getLimitHistory(countryCode, fundCode);
        return ResponseEntity.ok(history);
    }

}
