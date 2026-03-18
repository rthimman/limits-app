package com.stellantis.securitization.repository;

import com.stellantis.securitization.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FundRepository extends JpaRepository<Fund, UUID> {
    Optional<Fund> findByFundCodeAndCountryCode(String fundCode, String countryCode);
}
