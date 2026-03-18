package com.stellantis.securitization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitConfigId implements Serializable {

    @Column(name = "ID_FUND", nullable = false)
    private UUID fundId;

    @Column(name = "CRITERIA_CODE", length = 50, nullable = false)
    private String criteriaCode;

}
