package com.stellantis.securitization.service;

import com.stellantis.securitization.enums.AuditSource;
import com.stellantis.securitization.dto.LimitListResponse;
import com.stellantis.securitization.dto.LimitResponseDto;
import com.stellantis.securitization.dto.LimitUpdateRequest;
import com.stellantis.securitization.entity.LimitConfig;
import com.stellantis.securitization.entity.LimitConfigHistory;
import com.stellantis.securitization.exception.CriteriaNotFoundException;
import com.stellantis.securitization.exception.NoActiveLimitsFoundException;
import com.stellantis.securitization.repository.LimitConfigHistoryRepository;
import com.stellantis.securitization.repository.LimitConfigRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LimitConfigService {
    private final LimitConfigRepository limitRepo;
    private final LimitConfigHistoryRepository historyRepo;
    private static final Set<String> ALLOWED_OPERATORS = Set.of("<=", ">=");

    public LimitListResponse getLimits(String countryCode, String fundCode) {
        List<LimitResponseDto> list = limitRepo.findActiveLimits(countryCode, fundCode).stream()
                .map(lc -> LimitResponseDto.builder()
                        .limitName(lc.getId().getCriteriaCode())
                        .labelEn(lc.getLabelEn())
                        .operator(lc.getOperator())
                        .thresholdValue(lc.getThresholdValue())
                        .valueType(lc.getValueType())
                        .displayOrder(lc.getDisplayOrder())
                        .build())
                .toList();
        return new LimitListResponse(list);
    }

    @Transactional
    public int updateLimits(String countryCode, String fundCode, LimitUpdateRequest request) {

        int count = 0;
        for (LimitUpdateRequest.Item item : request.getUpdates()) {
            LimitConfig config = limitRepo.findByFundAndCriteria(countryCode, fundCode, item.getCriteriaCode());
            if (config == null) {
                throw new CriteriaNotFoundException(item.getCriteriaCode());
            }

            // 2. Validate Operator
//            if (item.getOperator() != null &&
//                    !ALLOWED_OPERATORS.contains(item.getOperator())) {
//                throw new InvalidOperatorException(item.getOperator());
//            }

            BigDecimal oldValue = config.getThresholdValue();

            config.setOperator(item.getOperator());
            config.setThresholdValue(item.getThresholdValue());
            config.setUpdatedBy("system");
            config.setUpdatedAt(LocalDateTime.now());
            limitRepo.save(config);

            historyRepo.save(
                    LimitConfigHistory.builder()
                            .fundId(config.getId().getFundId())
                            .criteriaCode(config.getId().getCriteriaCode())
                            .oldValue(oldValue)
                            .newValue(config.getThresholdValue())
                            .source(AuditSource.INLINE_EDIT)
                            .eventId(null)
                            .modifiedBy("system")
                            .modifiedAt(LocalDateTime.now())
                            .build()
            );
            count++;
        }
        return count;
    }

    @Transactional
    public int deleteLimit(String countryCode, String fundCode, String criteriaCode) {
        LimitConfig limitConfig = limitRepo.findByFundAndCriteria(countryCode, fundCode, criteriaCode);

        if (limitConfig == null) {
            throw new CriteriaNotFoundException(criteriaCode);
        }
        BigDecimal oldValue = limitConfig.getThresholdValue();
        limitRepo.delete(limitConfig);

        historyRepo.save(
                LimitConfigHistory.builder()
                        .fundId(limitConfig.getId().getFundId())
                        .criteriaCode(criteriaCode)
                        .oldValue(oldValue)
                        .newValue(null)
                        .source(AuditSource.INLINE_EDIT)
                        .eventId(null)
                        .modifiedBy("system")
                        .modifiedAt(LocalDateTime.now())
                        .build()
        );
        return 1;

    }

    public byte[] exportLimits(String countryCode, String fundCode) throws IOException {
        List<LimitConfig> limits = limitRepo.findActiveLimitsForExport(countryCode, fundCode);
        if (limits.isEmpty()){
            throw new NoActiveLimitsFoundException(countryCode, fundCode);
        }

        final List<String> headers = List.of(
                "CRITERIA_CODE", "LABEL_EN", "LABEL_FR",
                "OPERATOR", "THRESHOLD_VALUE", "VALUE_TYPE"
        );
        try (var workbook = new XSSFWorkbook();
             var out = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("Limits");
            createHeaderRow(sheet, headers);
            int rowIndex = 1;
            for (LimitConfig lc : limits) {
                var row = sheet.createRow(rowIndex++);

                int col = 0;
                row.createCell(col++).setCellValue(lc.getId().getCriteriaCode());
                row.createCell(col++).setCellValue(Objects.toString(lc.getLabelEn(), ""));
                row.createCell(col++).setCellValue(Objects.toString(lc.getLabelFr(), ""));
                row.createCell(col++).setCellValue(lc.getOperator());
                row.createCell(col++).setCellValue(
                        lc.getThresholdValue() != null ?
                                lc.getThresholdValue().doubleValue() : 0
                );
                row.createCell(col).setCellValue(lc.getValueType());
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createHeaderRow(Sheet sheet, List<String> headers) {
        var headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

    }
}
