package com.stellantis.securitization.entity;

import com.stellantis.securitization.enums.ConfigType;
import com.stellantis.securitization.enums.ImportStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "T_CONFIG_IMPORT_STATUS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigImportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_FUND", nullable = false)
    private UUID fundId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONFIG_TYPE", nullable = false, length = 30)
    private ConfigType configType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 10)
    private ImportStatus status;

    @Column(name = "RECORDS_IMPORTED")
    private Integer recordsImported;

    @Column(name = "ERROR_MESSAGE", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "IMPORTED_BY", length = 100)
    private String importedBy;

    @Column(name = "IMPORTED_AT", nullable = false)
    private LocalDateTime importedAt;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (importedAt == null) {
            importedAt = LocalDateTime.now();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}
