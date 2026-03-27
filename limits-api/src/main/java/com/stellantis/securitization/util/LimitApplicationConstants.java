package com.stellantis.securitization.util;

import java.util.List;
import java.util.Set;

public final class LimitApplicationConstants {
    public static final String BASE_ENDPOINT = "/api/v1/funds";
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final String EXCEL_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String LIMIT_HEADER_VALUES = "attachment; filename=\"limits_export.xlsx\"";
    public static final String NEW_LINE = "\n";
    public static final Set<String> ALLOWED_OPERATORS = Set.of("<=", ">=");
    public static final String REGEX_PATTERN = "^-?\\d+(\\.\\d+)?$";
    public static final List<String> EXPECTED_HEADERS = List.of("CRITERIA_CODE", "LABEL_EN", "LABEL_FR", "PROCESS", "OPERATOR", "THRESHOLD_VALUE", "VALUE_TYPE"
    );
}
