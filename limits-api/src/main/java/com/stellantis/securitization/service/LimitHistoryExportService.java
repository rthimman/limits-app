package com.stellantis.securitization.service;


import com.stellantis.securitization.dto.LimitHistoryExportRow;
import com.stellantis.securitization.entity.LimitConfigHistory;
import com.stellantis.securitization.repository.LimitConfigHistoryRepository;
import com.stellantis.securitization.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitHistoryExportService {

    private final LimitConfigHistoryRepository historyRepo;

    public byte[] exportLimitConfigHistory(String countryCode, String fundCode) throws IOException {

        List<LimitConfigHistory> historyList =
                historyRepo.findByFund_FundCodeAndFund_CountryCodeOrderByModifiedAtDesc(
                        fundCode, countryCode);

        List<LimitHistoryExportRow> rows = historyList.stream()
                .map(history -> new LimitHistoryExportRow(
                        history.getModifiedAt(),
                        history.getModifiedBy(),
                        (history.getLimitConfig() != null ? history.getLimitConfig().getLabelEn() : history.getCriteriaCode()),
                        history.getOldValue(),
                        history.getNewValue(),
                        extractOldOperator(history),
                        extractNewOperator(history)
                ))
                .toList();

        return buildHistoryExcel(rows);
    }

    private String extractOldOperator(LimitConfigHistory history) {
        if (history.getOldValue() == null) return null;
        return history.getLimitConfig() != null ? history.getLimitConfig().getOperator().getSymbol() : null;
    }

    private String extractNewOperator(LimitConfigHistory history) {
        return history.getLimitConfig() != null ? history.getLimitConfig().getOperator().getSymbol() : null;
    }

    private byte[] buildHistoryExcel(List<LimitHistoryExportRow> rows) throws IOException {

        List<String> headers = List.of(
                "Timestamp",
                "Modified By",
                "Limit Name",
                "Old Value",
                "New Value",
                "Old Operator",
                "New Operator"
        );

        XSSFWorkbook workbook = ExcelExportUtil.createWorkbook();
        Sheet sheet = ExcelExportUtil.createSheetWithHeader(workbook, "Limit History", headers);

        int rowIndex = 1;

        for (LimitHistoryExportRow r : rows) {
            Row row = sheet.createRow(rowIndex++);
            int col = 0;

            row.createCell(col++).setCellValue(r.getTimestamp() != null ? r.getTimestamp().toString() : "");
            row.createCell(col++).setCellValue(r.getModifiedBy());
            row.createCell(col++).setCellValue(r.getLimitName());
            row.createCell(col++).setCellValue(r.getOldValue() != null ? r.getOldValue().doubleValue() : 0);
            row.createCell(col++).setCellValue(r.getNewValue() != null ? r.getNewValue().doubleValue() : 0);
            row.createCell(col++).setCellValue(r.getOldOperator() != null ? r.getOldOperator() : "");
            row.createCell(col++).setCellValue(r.getNewOperator() != null ? r.getNewOperator() : "");
        }

        return ExcelExportUtil.writeToByteArray(workbook, headers.size());
    }

}
