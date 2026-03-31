package com.stellantis.securitization.service;

import com.stellantis.securitization.dto.LimitHistoryResponse;
import com.stellantis.securitization.repository.LimitConfigHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitHistoryExportService {

    private final LimitConfigHistoryRepository historyRepo;

    public List<LimitHistoryResponse> getLimitHistory(String countryCode, String fundCode) {
        return historyRepo.findByFund_FundCodeAndFund_CountryCodeOrderByModifiedAtDesc(fundCode, countryCode)
                .stream()
                .map(history -> LimitHistoryResponse.builder()
                        .modifiedAt(history.getModifiedAt())
                        .modifiedBy(history.getModifiedBy())
                        .criteriaCode(history.getCriteriaCode())
                        .oldValue(history.getOldValue())
                        .newValue(history.getNewValue())
                        .build())
                .toList();
    }
}
