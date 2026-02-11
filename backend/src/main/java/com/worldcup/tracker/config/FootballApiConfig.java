package com.worldcup.tracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FootballApiConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("X-RapidAPI-Key", apiKey);
            request.getHeaders().set("X-RapidAPI-Host", apiHost);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Value("${football.api.key}")
    private String apiKey;

    @Value("${football.api.host}")
    private String apiHost;

    @Value("${football.api.base-url}")
    private String baseUrl;

}
