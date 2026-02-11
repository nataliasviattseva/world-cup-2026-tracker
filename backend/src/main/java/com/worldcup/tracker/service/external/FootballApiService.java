package com.worldcup.tracker.service.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FootballApiService {

    private final RestTemplate restTemplate;

    @Value("${football.api.base-url}")
    private String baseUrl;

    public String getMatch(Long matchId) {
        String url = baseUrl + "/fixtures?id=" + matchId;
        return restTemplate.getForObject(url, String.class);
    }
}
