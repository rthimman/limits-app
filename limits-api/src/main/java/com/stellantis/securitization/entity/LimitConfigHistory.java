package com.stellantis.securitization.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "T_LIMIT_CONFIG_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LimitConfigHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORY")
    private Long id;

    @Column(name = "ID_FUND", nullable = false)
    private UUID fundId;

    @Column(name = "CRITERIA_CODE", length = 50, nullable = false)
    private String criteriaCode;

    @Column(name = "OLD_VALUE")
    private BigDecimal oldValue;

    @Column(name = "NEW_VALUE")
    private BigDecimal newValue;

    @Column(name = "SOURCE", length = 20, nullable = false)
    private String source; // INLINE_EDIT

    @Column(name = "ID_EVENT")
    private UUID eventId; // null for inline edit

    @Column(name = "MODIFIED_BY", length = 100, nullable = false)
    private String modifiedBy;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;
}
