package com.stellantis.securitization.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_LIMIT_CONFIG")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LimitConfig {

    @EmbeddedId
    private LimitConfigId id;

    @Column(name = "LABEL_EN", length = 100)
    private String labelEn;

    @Column(name = "LABEL_FR", length = 100)
    private String labelFr;

    @Column(name = "PROCESS", length = 20, nullable = false)
    private String process; // SELECTION | MONITORING | BUYBACK

    @Column(name = "OPERATOR", length = 5, nullable = false)
    private String operator; // <= or >=

    @Column(name = "THRESHOLD_VALUE", precision = 15, scale = 4)
    private BigDecimal thresholdValue;

    @Column(name = "VALUE_TYPE", length = 20, nullable = false)
    private String valueType; // PERCENTAGE | MONTHS | EUR | RATE | COUNT

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @MapsId("fundId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FUND", nullable = false)
    private Fund fund;

}
