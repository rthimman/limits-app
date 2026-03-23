package com.stellantis.securitization.controller;

import com.stellantis.securitization.dto.LimitListResponse;
import com.stellantis.securitization.dto.LimitUpdateRequest;
import com.stellantis.securitization.service.LimitConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/funds/{countryCode}/{fundCode}/limits")
@RequiredArgsConstructor
public class LimitController {
    private final LimitConfigService limitService;

    @GetMapping
    public ResponseEntity<LimitListResponse> getLimits(@PathVariable String countryCode,
                                                       @PathVariable String fundCode) {
        return ResponseEntity.ok(
                limitService.getLimits(countryCode, fundCode)
        );
    }

    @PutMapping
    public ResponseEntity<?> updateLimits(
            @PathVariable String countryCode,
            @PathVariable String fundCode,
            @RequestBody LimitUpdateRequest request) {

        int updatedCount = limitService.updateLimits(countryCode, fundCode, request);
        return ResponseEntity.ok(Map.of("updatedCount", updatedCount));
    }

    @DeleteMapping("/{criteriaCode}")
    public ResponseEntity<Map<String, Integer>> deleteLimit(
            @PathVariable String countryCode,
            @PathVariable String fundCode,
            @PathVariable String criteriaCode) {

        int deletedCount = limitService.deleteLimit(countryCode, fundCode, criteriaCode);

        return ResponseEntity.ok(Map.of("deletedCount", deletedCount));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportLimits(@PathVariable String countryCode,@PathVariable String fundCode) throws IOException {
        byte[] excelFile = limitService.exportLimits(countryCode, fundCode);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=\"limits_export.xlsx\"")
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
    }

}
