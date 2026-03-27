package com.stellantis.securitization.repository;

import com.stellantis.securitization.entity.ConfigImportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigImportStatusRepository extends JpaRepository<ConfigImportStatus, Long> {
}
