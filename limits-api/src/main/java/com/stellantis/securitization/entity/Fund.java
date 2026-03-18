package com.stellantis.securitization.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "T_FUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fund {

    @Id
    @UuidGenerator
    @Column(name = "ID_FUND", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "DESCRIPTION", length = 100, nullable = false)
    private String description;

    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;

    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;

    @Column(name = "STATUS", length = 20)
    private String status;     // DRAFT, ACTIVE, REVOLVING, LIQUIDATED, CLOSED

    @Column(name = "PRODUCT_CODE", length = 30)
    private String productCode;

    @Column(name = "FUND_CODE", length = 20)
    private String fundCode;

    @Column(name = "FUND_TYPE", length = 20)
    private String fundType;   // SECURITIZATION, WAREHOUSE, PORTFOLIO

    @Column(name = "LEGAL_ENTITY", length = 100)
    private String legalEntity;

    @Column(name = "FUND_MANAGER", length = 100)
    private String fundManager;

    @Column(name = "ENTITY_CODE", length = 10)
    private String entityCode;

    @Column(name = "SUB_LEDGER_CODE", length = 30)
    private String subLedgerCode;

    @Column(name = "GL_ACCOUNT_CESSION", length = 20)
    private String glAccountCession;

    @Column(name = "ISSUER_CODE", length = 10)
    private String issuerCode;

    @Column(name = "EUREFI_SECURITIZED_ENTITY", length = 100)
    private String eurefiSecuritizedEntity;

    @Column(name = "BIS_ENTITY", length = 100)
    private String bisEntity;

    @Column(name = "CAS_ACCOUNT_FLAG")
    private Boolean casAccountFlag;

    @Column(name = "MAXIMUM_EXPOSURE", precision = 15, scale = 2)
    private BigDecimal maximumExposure;

    @Column(name = "MINIMUM_EXPOSURE", precision = 15, scale = 2)
    private BigDecimal minimumExposure;

    @Column(name = "TARGET_YIELD", precision = 8, scale = 4)
    private BigDecimal targetYield;

    @Column(name = "INCEPTION_DATE")
    private LocalDate inceptionDate;

    @Column(name = "MATURITY_DATE")
    private LocalDate maturityDate;

    @Column(name = "LAST_REVIEW_DATE")
    private LocalDate lastReviewDate;

    @Column(name = "NEXT_REVIEW_DATE")
    private LocalDate nextReviewDate;

    @Column(name = "INITIAL_PORTFOLIO_AMOUNT", precision = 15, scale = 2)
    private BigDecimal initialPortfolioAmount;

    @Column(name = "OUTSTANDING_PRINCIPAL_BALANCE", precision = 15, scale = 2)
    private BigDecimal outstandingPrincipalBalance;

    @Column(name = "IS_RETENTION_FUND")
    private Boolean isRetentionFund;

    @Column(name = "RETENTION_FUND_CODE", length = 20)
    private String retentionFundCode;

    @Column(name = "RETENTION_FUND_AMOUNT", precision = 15, scale = 2)
    private BigDecimal retentionFundAmount;

    @Column(name = "SERVICING_FEE_RATE", precision = 8, scale = 6)
    private BigDecimal servicingFeeRate;

    @Column(name = "LIQUIDATION_DATE")
    private LocalDate liquidationDate;

    @Column(name = "LIQUIDATED_BY", length = 100)
    private String liquidatedBy;

    @Column(name = "CREATED_BY", length = 20)
    private String createdBy;

    @Column(name = "UPDATED_BY", length = 20)
    private String updatedBy;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}