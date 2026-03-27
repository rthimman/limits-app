package com.stellantis.securitization.service;

import com.stellantis.securitization.dto.LimitImportRow;
import com.stellantis.securitization.dto.UploadLimitResponse;
import com.stellantis.securitization.entity.Fund;
import com.stellantis.securitization.entity.LimitConfig;
import com.stellantis.securitization.entity.LimitConfigId;
import com.stellantis.securitization.entity.LimitConfigHistory;
import com.stellantis.securitization.entity.ConfigImportStatus;
import com.stellantis.securitization.enums.ImportStatus;
import com.stellantis.securitization.enums.LimitProcess;
import com.stellantis.securitization.enums.ValueType;
import com.stellantis.securitization.enums.LimitOperator;
import com.stellantis.securitization.enums.AuditSource;
import com.stellantis.securitization.enums.ConfigType;
import com.stellantis.securitization.exception.FundNotFoundException;
import com.stellantis.securitization.repository.ConfigImportStatusRepository;
import com.stellantis.securitization.repository.FundRepository;
import com.stellantis.securitization.repository.LimitConfigHistoryRepository;
import com.stellantis.securitization.repository.LimitConfigRepository;
import com.stellantis.securitization.util.ExcelParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.stellantis.securitization.util.LimitApplicationConstants.ERROR;
import static com.stellantis.securitization.util.LimitApplicationConstants.SUCCESS;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class LimitImportService {
    private final FundRepository fundRepository;
    private final LimitConfigRepository limitRepo;
    private final LimitConfigHistoryRepository historyRepo;
    private final ConfigImportStatusRepository importStatusRepo;
    private static final Set<String> ALLOWED_OPERATORS = Set.of("<=", ">=");

    @Transactional
    public UploadLimitResponse importLimits(String countryCode,
                                            String fundCode,
                                            MultipartFile file,
                                            String userSsoId) throws IOException {
        List<LimitImportRow> rows = ExcelParser.parse(file);
        List<String> validationErrors = validateRows(rows);

        Fund fund = getFund(countryCode, fundCode);

        if (!validationErrors.isEmpty()) {
            saveImportStatus(
                    fund.getId(),
                    ImportStatus.ERROR,
                    0,
                    String.join("\n", validationErrors),
                    userSsoId
            );

            return errorResponse(validationErrors);
        }

        replaceLimits(fund, rows, userSsoId);

        saveImportStatus(
                fund.getId(),
                ImportStatus.SUCCESS,
                rows.size(),
                null,
                userSsoId
        );

        return successResponse(rows.size());
    }

    private List<String> validateRows(List<LimitImportRow> rows) {
        List<String> errors = new ArrayList<>();
        int rowIndex = 2;
        for (LimitImportRow row : rows) {
            List<String> rowErrors = new ArrayList<>();
            validateCriteriaCode(row.getCriteriaCode(), rowErrors);
            validateProcess(row.getProcess(), rowErrors);
            validateOperator(row.getOperator(), rowErrors);
            validateValueType(row.getValueType(), rowErrors);

            if (!rowErrors.isEmpty()) {
                errors.add("Row " + rowIndex + ": " + String.join(", ", rowErrors));
            }
            rowIndex++;
        }
        return errors;
    }

    private void validateCriteriaCode(String value, List<String> errors) {
        if (isBlank(value)) errors.add("Missing CRITERIA_CODE");
    }

    private void validateProcess(String value, List<String> errors) {
        if (isBlank(value)) {
            errors.add("Missing PROCESS");
            return;
        }
        try {
            LimitProcess.valueOf(value.trim().toUpperCase());
        } catch (Exception ex) {
            errors.add("Invalid PROCESS: " + value);
        }
    }

    private void validateOperator(String value, List<String> errors) {
        if (isBlank(value)) {
            errors.add("Missing OPERATOR");
            return;
        }
        if (!ALLOWED_OPERATORS.contains(value.trim())) {
            errors.add("Invalid OPERATOR: " + value);
        }
    }

    private void validateValueType(String value, List<String> errors) {
        if (isBlank(value)) {
            errors.add("Missing VALUE_TYPE");
            return;
        }
        try {
            ValueType.valueOf(value.trim().toUpperCase());
        } catch (Exception ex) {
            errors.add("Invalid VALUE_TYPE: " + value);
        }
    }

    private UploadLimitResponse errorResponse(List<String> errors) {
        return UploadLimitResponse.builder()
                .status(ERROR)
                .message(errors)
                .totalRecords("0 Limits Imported")
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Fund getFund(String countryCode, String fundCode) {
        return fundRepository.findByFundCodeAndCountryCode(fundCode, countryCode)
                .orElseThrow(() -> new FundNotFoundException(countryCode, fundCode));
    }

    private void replaceLimits(Fund fund, List<LimitImportRow> rows, String userSsoId) {
        limitRepo.deleteByFundId(fund.getId());
        int displayOrder = 1;
        for (LimitImportRow row : rows) {

            LimitConfig config = mapRowToEntity(fund, row, displayOrder++, userSsoId);
            limitRepo.save(config);

            saveHistory(fund, row, userSsoId);
        }
    }

    private LimitConfig mapRowToEntity(Fund fund,
                                       LimitImportRow row,
                                       int displayOrder,
                                       String userSsoId) {
        LimitConfigId id = new LimitConfigId(fund.getId(), row.getCriteriaCode());
        return LimitConfig.builder()
                .id(id)
                .fund(fund)
                .labelEn(row.getLabelEn())
                .labelFr(row.getLabelFr())
                .process(LimitProcess.valueOf(row.getProcess().trim().toUpperCase()))
                .operator(LimitOperator.fromSymbol(row.getOperator()))
                .valueType(ValueType.valueOf(row.getValueType().trim().toUpperCase()))
                .thresholdValue(row.getThresholdValue())
                .displayOrder(displayOrder)
                .isActive(true)
                .createdBy(userSsoId)
                .updatedBy(userSsoId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void saveHistory(Fund fund, LimitImportRow row, String userSsoId) {
        historyRepo.save(
                LimitConfigHistory.builder()
                        .fundId(fund.getId())
                        .criteriaCode(row.getCriteriaCode())
                        .oldValue(null)
                        .newValue(row.getThresholdValue())
                        .source(AuditSource.EXCEL_IMPORT)
                        .modifiedBy(userSsoId)
                        .modifiedAt(LocalDateTime.now())
                        .build()
        );
    }

    private void saveImportStatus(UUID fundId,
                                  ImportStatus status,
                                  int recordsImported,
                                  String errorMessage,
                                  String userSsoId) {
        importStatusRepo.save(
                ConfigImportStatus.builder()
                        .fundId(fundId)
                        .configType(ConfigType.LIMITS)
                        .status(status)
                        .recordsImported(recordsImported)
                        .errorMessage(errorMessage)
                        .importedBy(userSsoId)
                        .importedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    private UploadLimitResponse successResponse(int totalRecords) {
        return UploadLimitResponse.builder()
                .status(SUCCESS)
                .message(List.of("Import successful"))
                .totalRecords(totalRecords + " Limits Imported")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
