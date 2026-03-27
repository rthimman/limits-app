package com.stellantis.securitization.repository;

import com.stellantis.securitization.entity.LimitConfig;
import com.stellantis.securitization.entity.LimitConfigId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LimitConfigRepository extends JpaRepository<LimitConfig, LimitConfigId> {

    @Query("""
                SELECT lc FROM LimitConfig lc
                JOIN lc.fund f
                WHERE f.countryCode = :countryCode
                  AND f.fundCode = :fundCode
                  AND lc.isActive = TRUE
                ORDER BY lc.displayOrder
            """)
    List<LimitConfig> findActiveLimits(
            @Param("countryCode") String countryCode,
            @Param("fundCode") String fundCode
    );


    @Query("""
                SELECT lc FROM LimitConfig lc
                JOIN lc.fund f
                WHERE f.countryCode = :countryCode
                  AND f.fundCode = :fundCode
                  AND lc.id.criteriaCode = :criteriaCode
            """)
    LimitConfig findByFundAndCriteria(
            @Param("countryCode") String countryCode,
            @Param("fundCode") String fundCode,
            @Param("criteriaCode") String criteriaCode
    );

    @Query("""
                SELECT lc FROM LimitConfig lc
                JOIN lc.fund f
                WHERE f.countryCode = :countryCode
                  AND f.fundCode = :fundCode
                  AND lc.isActive = TRUE
                ORDER BY lc.displayOrder
            """)
    List<LimitConfig> findActiveLimitsForExport(@Param("countryCode") String countryCode,
                                                @Param("fundCode") String fundCode);

    void deleteByFundId(UUID id);
}
