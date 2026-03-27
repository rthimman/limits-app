package com.stellantis.securitization.entity;

import com.stellantis.securitization.enums.LimitOperator;
import com.stellantis.securitization.enums.LimitProcess;
import com.stellantis.securitization.enums.ValueType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Convert;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "PROCESS", length = 20, nullable = false)
    private LimitProcess process;

    @Convert(converter = OperatorConverter.class)
    @Column(name = "OPERATOR", length = 5, nullable = false)
    private LimitOperator operator;

    @Column(name = "THRESHOLD_VALUE", precision = 15, scale = 4)
    private BigDecimal thresholdValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "VALUE_TYPE", length = 20, nullable = false)
    private ValueType valueType;

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
