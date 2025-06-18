package com.example.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Configuration
@ConfigurationProperties(prefix = "feature-flags.movies-service")
public class FeatureFlagConfig {
    @Value("${MOVIES_MIGRATION_PERCENT:#{null}}")
    private Integer migrationPercent;

    @Value("${GRADUAL_MIGRATION:false}")
    private boolean enabled;

    // Обновляем геттер, чтобы учитывать переменную окружения
    public int getTrafficPercentage() {
            return migrationPercent;
    }

    public boolean isFullyOnMonolith() {
        return !enabled || migrationPercent == 0;
    }

    public boolean isFullyOnMicroservice() {
        return enabled && migrationPercent == 100;
    }

    public boolean isEnabled() {
        return enabled;
    }
}