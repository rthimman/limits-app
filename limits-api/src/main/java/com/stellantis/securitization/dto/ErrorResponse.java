package com.stellantis.securitization.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
}
