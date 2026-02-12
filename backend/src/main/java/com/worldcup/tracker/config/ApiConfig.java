package com.worldcup.tracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "livescore.api")
@Getter
@Setter
public class ApiConfig {
    
    private String key;
    private String secret;
    private String baseUrl;
    private int cacheDurationMinutes = 5;
}
