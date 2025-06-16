package com.example.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "feature-flags.movies-service")
public class FeatureFlagConfig {
    private boolean enabled;
    private int trafficPercentage;
}