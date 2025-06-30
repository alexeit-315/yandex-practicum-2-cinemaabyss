package com.example.proxy.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "feature-flags.movies-service")
public class FeatureFlagConfig {
    @Value("${MOVIES_MIGRATION_PERCENT:#{null}}")
    private Integer migrationPercent;

    @Value("${GRADUAL_MIGRATION:false}")
    private boolean enabled;

    // Переменная окружения
    public int getTrafficPercentage() {
            log.info("Info message: TrafficPercentage -  {}", migrationPercent);
            return migrationPercent;
    }

    public boolean isFullyOnMonolith() {
        return !enabled || migrationPercent == 0;
    }

    public boolean isFullyOnMicroservice() {
        return enabled && migrationPercent == 100;
    }

    public boolean isEnabled() {
        log.info("Info message:  -  {}", enabled);
        return enabled;
    }
}