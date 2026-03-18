package com.stellantis.securitization.controller;

import com.stellantis.securitization.dto.LimitListResponse;
import com.stellantis.securitization.dto.LimitUpdateRequest;
import com.stellantis.securitization.service.LimitConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/funds/{countryCode}/{fundCode}/limits")
@RequiredArgsConstructor
public class LimitController {
    private final LimitConfigService limitService;

    @GetMapping
    public ResponseEntity<LimitListResponse> getLimits(String countryCode,
                                                       String fundCode) {
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

}
