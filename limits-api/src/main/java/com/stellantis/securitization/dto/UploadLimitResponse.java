package com.stellantis.securitization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadLimitResponse {
    private String status;
    private List<String> message;
    private String totalRecords;
    private LocalDateTime timestamp;
}
