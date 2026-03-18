/*
package com.stellantis.securitization;

import com.stellantis.securitization.entity.Fund;
import com.stellantis.securitization.entity.LimitConfig;
import com.stellantis.securitization.entity.LimitConfigId;
import com.stellantis.securitization.repository.FundRepository;
import com.stellantis.securitization.repository.LimitConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class LimitConfigDataLoader implements CommandLineRunner {
    private final FundRepository fundRepository;
    private final LimitConfigRepository limitRepository;
    @Override
    public void run(String... args) throws Exception {

       */
/* String countryCode = "FR";
        String fundCode = "FR01";

        // Create test fund if missing
        Fund fund = fundRepository.findByFundCodeAndCountryCode(fundCode, countryCode)
                .orElseGet(() -> {
                    Fund f = new Fund();
                    f.setDescription("Sample Fund FR01");
                    f.setFundCode(fundCode);
                    f.setCountryCode(countryCode);
                    f.setCurrencyCode("EUR");
                    f.setServicingFeeRate(new BigDecimal("0.000900"));
                    return fundRepository.save(f);
                });


        // If limits already exist, skip to avoid duplicates
        if (!limitRepository.findActiveLimits(countryCode, fundCode).isEmpty()) {
            System.out.println(">>> Sample T_LIMIT_CONFIG data already present. Skipping seed.");
            return;
        }

        List<LimitConfig> entries = new ArrayList<>();
        String[] criteriaCodes = {
                "MAX_TERM", "MIN_FICO", "MAX_LOAN_AMOUNT", "MAX_LTV",
                "MIN_LTV", "MAX_EXPOSURE", "MIN_EXPOSURE", "MAX_AGE",
                "MIN_AGE", "MAX_ARREARS", "MIN_INCOME", "MAX_INCOME",
                "MAX_VEHICLE_AGE", "MAX_MILEAGE", "MIN_DOWNPAYMENT",
                "MAX_DOWNPAYMENT", "MAX_RATE", "MIN_RATE", "MAX_RISK_GRADE",
                "MIN_RISK_GRADE", "MAX_CONTRACTS", "MIN_CONTRACTS",
                "MAX_REPAYMENT", "MIN_REPAYMENT", "MAX_UTILIZATION"
        };

        Random random = new Random();
        int displayOrder = 1;

        for (String code : criteriaCodes) {

            LimitConfigId id = new LimitConfigId(fund.getId(), code);

            LimitConfig lc = LimitConfig.builder()
                    .id(id)
                    .fund(fund)
                    .labelEn(code.replace("_", " "))
                    .operator(random.nextBoolean() ? "<=" : ">=")
                    .thresholdValue(BigDecimal.valueOf(random.nextInt(100) + 1))
                    .valueType("PERCENTAGE")
                    .process("SELECTION")
                    .displayOrder(displayOrder++)
                    .isActive(true)
                    .createdBy(null)
                    .createdAt(LocalDateTime.now())
                    .updatedBy(null)
                    .updatedAt(LocalDateTime.now())
                    .build();

            entries.add(lc);
        }

        limitRepository.saveAll(entries);

        System.out.println(">>> Inserted " + entries.size() + " sample T_LIMIT_CONFIG records.");*//*




// ─────────────────────────────────────────────────────
        // 1. Create multiple seed funds
        // ─────────────────────────────────────────────────────
        List<Fund> funds = createFunds();

        // ─────────────────────────────────────────────────────
        // 2. Seed limits for each fund
        // ─────────────────────────────────────────────────────
        for (Fund fund : funds) {
            seedLimitConfigsForFund(fund);
        }

        System.out.println(">>> Multi-fund seed completed successfully.");
    }


    // ========================================================================
    // CREATE MULTIPLE FUNDS WITH RICH SAMPLE DATA
    // ========================================================================
    private List<Fund> createFunds() {

        List<Map<String, String>> seedFundData = List.of(
                Map.of("country", "FR", "code", "FR01", "currency", "EUR"),
                Map.of("country", "IT", "code", "IT99", "currency", "EUR"),
                Map.of("country", "ES", "code", "ES77", "currency", "EUR"),
                Map.of("country", "DE", "code", "DE55", "currency", "EUR"),
                Map.of("country", "GB", "code", "UK22", "currency", "GBP")
        );

        List<Fund> funds = new ArrayList<>();

        for (Map<String, String> row : seedFundData) {

            String country = row.get("country");
            String fundCode = row.get("code");
            String currency = row.get("currency");

            Optional<Fund> existing = fundRepository
                    .findByFundCodeAndCountryCode(fundCode, country);

            if (existing.isPresent()) {
                funds.add(existing.get());
                continue;
            }

            Fund f = new Fund();
            f.setDescription("Sample Fund " + fundCode);
            f.setFundCode(fundCode);
            f.setCountryCode(country);
            f.setCurrencyCode(currency);
            f.setProductCode("VAC");
            f.setFundType("SECURITIZATION");
            f.setServicingFeeRate(new BigDecimal("0.000900"));
            f.setInitialPortfolioAmount(BigDecimal.valueOf(10000000));
            f.setOutstandingPrincipalBalance(BigDecimal.valueOf(5000000));
            f.setInceptionDate(LocalDate.now().minusYears(2));
            f.setMaturityDate(LocalDate.now().plusYears(3));
            f.setStatus("ACTIVE");
            f.setIsRetentionFund(false);
            f.setCreatedAt(LocalDateTime.now());
            f.setUpdatedAt(LocalDateTime.now());

            funds.add(fundRepository.save(f));
        }

        return funds;
    }


    // ========================================================================
    // SEED LIMIT CONFIGS FOR EACH FUND
    // ========================================================================
    private void seedLimitConfigsForFund(Fund fund) {

        boolean alreadySeeded =
                !limitRepository.findActiveLimits(fund.getCountryCode(), fund.getFundCode()).isEmpty();

        if (alreadySeeded) {
            System.out.println(">>> Limits already exist for fund " + fund.getFundCode() + ", skipping.");
            return;
        }

        List<LimitConfig> entries = new ArrayList<>();

        String[] criteriaCodes = {
                "MAX_TERM", "MIN_FICO", "MAX_LOAN_AMOUNT", "MAX_LTV",
                "MIN_LTV", "MAX_EXPOSURE", "MIN_EXPOSURE", "MAX_AGE",
                "MIN_AGE", "MAX_ARREARS", "MIN_INCOME", "MAX_INCOME",
                "MAX_VEHICLE_AGE", "MAX_MILEAGE", "MIN_DOWNPAYMENT",
                "MAX_DOWNPAYMENT", "MAX_RATE", "MIN_RATE", "MAX_RISK_GRADE",
                "MIN_RISK_GRADE", "MAX_CONTRACTS", "MIN_CONTRACTS",
                "MAX_REPAYMENT", "MIN_REPAYMENT", "MAX_UTILIZATION"
        };

        Random random = new Random();
        int displayOrder = 1;

        for (String code : criteriaCodes) {

            LimitConfigId id = new LimitConfigId(fund.getId(), code);

            LimitConfig lc = LimitConfig.builder()
                    .id(id)
                    .fund(fund)
                    .labelEn(code.replace("_", " "))
                    .operator(random.nextBoolean() ? "<=" : ">=")
                    .thresholdValue(BigDecimal.valueOf(random.nextInt(90) + 10))
                    .valueType("PERCENTAGE")
                    .process("SELECTION")
                    .displayOrder(displayOrder++)
                    .isActive(true)
                    .createdBy(null)     // FK constraint avoided
                    .updatedBy(null)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            entries.add(lc);
        }

        limitRepository.saveAll(entries);

        System.out.println(
                ">>> Inserted " + entries.size()
                        + " limit configs for fund " + fund.getFundCode()
                        + " (" + fund.getCountryCode() + ")");
    }

}

*/
