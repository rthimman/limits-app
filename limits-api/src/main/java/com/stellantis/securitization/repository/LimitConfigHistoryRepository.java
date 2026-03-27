package com.stellantis.securitization.repository;


import com.stellantis.securitization.entity.LimitConfigHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LimitConfigHistoryRepository extends JpaRepository<LimitConfigHistory, Long> {

    List<LimitConfigHistory>
    findByFund_FundCodeAndFund_CountryCodeOrderByModifiedAtDesc(
            String fundCode,
            String countryCode
    );

}

