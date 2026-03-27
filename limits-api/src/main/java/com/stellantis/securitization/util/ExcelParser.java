package com.stellantis.securitization.util;

import com.stellantis.securitization.dto.LimitImportRow;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.stellantis.securitization.util.LimitApplicationConstants.REGEX_PATTERN;
import static com.stellantis.securitization.util.LimitApplicationConstants.EXPECTED_HEADERS;

public class ExcelParser {
    public static List<LimitImportRow> parse(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            validateHeaderRow(sheet.getRow(0));

            List<LimitImportRow> result = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || isEmptyRow(row)) continue;

                LimitImportRow dto = new LimitImportRow();
                dto.setCriteriaCode(getString(row, 0));
                dto.setLabelEn(getString(row, 1));
                dto.setLabelFr(getString(row, 2));
                dto.setProcess(getString(row, 3));
                dto.setOperator(getString(row, 4));
                dto.setThresholdValue(parseNumericOrNull(getString(row, 5)));
                dto.setValueType(getString(row, 6));

                result.add(dto);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel: " + e.getMessage(), e);
        }
    }

    private static void validateHeaderRow(Row header) {
        if (header == null) {
            throw new RuntimeException("Missing Excel header row");
        }

        for (int i = 0; i < EXPECTED_HEADERS.size(); i++) {
            String actual = getString(header, i);
            if (actual == null || !actual.equalsIgnoreCase(EXPECTED_HEADERS.get(i))) {
                throw new RuntimeException(
                        "Invalid Excel header at column " + (i + 1) +
                                ": expected '" + EXPECTED_HEADERS.get(i) +
                                "', found '" + actual + "'"
                );
            }
        }
    }

    private static boolean isEmptyRow(Row row) {
        for (int i = 0; i < EXPECTED_HEADERS.size(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    private static String getString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> null;
            default -> cell.toString().trim();
        };
    }

    private static BigDecimal parseNumericOrNull(String value) {
        if (value == null || value.isBlank()) return null;

        if (!value.matches(REGEX_PATTERN)) {
            return null;
        }

        return new BigDecimal(value);
    }
}