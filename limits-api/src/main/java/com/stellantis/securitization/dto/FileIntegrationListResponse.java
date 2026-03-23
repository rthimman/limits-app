package com.stellantis.securitization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileIntegrationListResponse {
    private List<FileIntegrationResponse> data;
}
