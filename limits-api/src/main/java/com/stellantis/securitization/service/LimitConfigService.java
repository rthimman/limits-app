package com.stellantis.securitization.service;

import com.stellantis.securitization.dto.LimitListResponse;
import com.stellantis.securitization.dto.LimitResponseDto;
import com.stellantis.securitization.dto.LimitUpdateRequest;
import com.stellantis.securitization.entity.LimitConfig;
import com.stellantis.securitization.entity.LimitConfigHistory;
import com.stellantis.securitization.enums.AuditSource;
import com.stellantis.securitization.enums.LimitOperator;
import com.stellantis.securitization.exception.CriteriaNotFoundException;
import com.stellantis.securitization.exception.NoActiveLimitsFoundException;
import com.stellantis.securitization.repository.LimitConfigHistoryRepository;
import com.stellantis.securitization.repository.LimitConfigRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LimitConfigService {
    private final LimitConfigRepository limitRepo;
    private final LimitConfigHistoryRepository historyRepo;

    public LimitListResponse getLimits(String countryCode, String fundCode) {

        List<LimitResponseDto> list = limitRepo.findActiveLimits(countryCode, fundCode).stream()
                .map(lc -> LimitResponseDto.builder()
                        .limitName(lc.getId().getCriteriaCode())
                        .labelEn(lc.getLabelEn())
                        .operator(lc.getOperator().toString())
                        .thresholdValue(lc.getThresholdValue())
                        .valueType(lc.getValueType().name())
                        .displayOrder(lc.getDisplayOrder())
                        .build())
                .toList();
        return new LimitListResponse(list);
    }

    @Transactional
    public int updateLimits(String countryCode, String fundCode, LimitUpdateRequest request) {

        int count = 0;
        for (LimitUpdateRequest.Item item : request.getUpdates()) {
            LimitConfig config =
                    limitRepo.findByFundAndCriteria(countryCode, fundCode, item.getCriteriaCode());

            if (config == null) {
                throw new CriteriaNotFoundException(item.getCriteriaCode());
            }

            BigDecimal oldValue = config.getThresholdValue();

            if (item.getOperator() != null)
                config.setOperator(LimitOperator.fromSymbol(item.getOperator()));

            config.setThresholdValue(item.getThresholdValue());
            config.setUpdatedBy("system");
            config.setUpdatedAt(LocalDateTime.now());

            limitRepo.save(config);

            historyRepo.save(
                    LimitConfigHistory.builder()
                            .fundId(config.getFund().getId())
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
                        .fundId(limitConfig.getFund().getId())
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
        if (limits.isEmpty()) {
            throw new NoActiveLimitsFoundException(countryCode, fundCode);
        }

        List<String> headers = List.of(
                "CRITERIA_CODE",
                "LABEL_EN",
                "LABEL_FR",
                "PROCESS",
                "OPERATOR",
                "THRESHOLD_VALUE",
                "VALUE_TYPE"
        );

        try (var workbook = new XSSFWorkbook();
             var out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Limits");
            createHeaderRow(sheet, headers);

            int rowIndex = 1;

            for (LimitConfig lc : limits) {

                var row = sheet.createRow(rowIndex++);
                int col = 0;

                row.createCell(col++).setCellValue(lc.getId().getCriteriaCode());
                row.createCell(col++).setCellValue(Objects.toString(lc.getLabelEn(), ""));
                row.createCell(col++).setCellValue(Objects.toString(lc.getLabelFr(), ""));
                row.createCell(col++).setCellValue(lc.getProcess().toString());
                row.createCell(col++).setCellValue(lc.getOperator().toString());
                row.createCell(col++).setCellValue(
                        lc.getThresholdValue() != null ?
                                lc.getThresholdValue().doubleValue() : 0
                );
                row.createCell(col).setCellValue(lc.getValueType().name());
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
