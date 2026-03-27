package com.stellantis.securitization.entity;

import com.stellantis.securitization.enums.AuditSource;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ConstraintMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Enumerated(EnumType.STRING)
    private AuditSource source;

    @Column(name = "ID_EVENT")
    private UUID eventId;

    @Column(name = "MODIFIED_BY", length = 100, nullable = false)
    private String modifiedBy;

    @Column(name = "MODIFIED_AT")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FUND", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @NotFound(action = NotFoundAction.IGNORE)
    private Fund fund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "ID_FUND", referencedColumnName = "ID_FUND",
                    insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
            @JoinColumn(
                    name = "CRITERIA_CODE", referencedColumnName = "CRITERIA_CODE",
                    insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
            )
    })
    @NotFound(action = NotFoundAction.IGNORE)
    private LimitConfig limitConfig;

}
