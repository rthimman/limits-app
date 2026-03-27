package com.stellantis.securitization.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExportUtil {
    public static XSSFWorkbook createWorkbook(){
        return new XSSFWorkbook();
    }

    public static Sheet createSheetWithHeader(XSSFWorkbook workbook, String sheetName, List<String> headers){
        Sheet sheet = workbook.createSheet(sheetName);
        Row headerRow = sheet.createRow(0);
        for (int i =0;i<headers.size();i++){
            headerRow.createCell(i).setCellValue(headers.get(i));
        }
        return sheet;
    }

    public static byte[] writeToByteArray(XSSFWorkbook workbook, int columnCount) throws IOException {

        for (int i = 0; i < columnCount; i++) {
            workbook.getSheetAt(0).autoSizeColumn(i);
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        }

    }
}
