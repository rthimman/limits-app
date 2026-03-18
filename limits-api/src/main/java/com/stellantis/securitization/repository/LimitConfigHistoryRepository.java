package com.stellantis.securitization.repository;


import com.stellantis.securitization.entity.LimitConfigHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitConfigHistoryRepository extends JpaRepository<LimitConfigHistory, Long> {
}

