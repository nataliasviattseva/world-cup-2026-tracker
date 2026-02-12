package com.worldcup.tracker.service;

import com.worldcup.tracker.config.ApiConfig;
import com.worldcup.tracker.model.ApiCache;
import com.worldcup.tracker.repository.ApiCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class LiveScoreApiService {

    private final ApiConfig config;
    private final ApiCacheRepository cacheRepository;

    public LiveScoreApiService(ApiConfig config, ApiCacheRepository cacheRepository) {
        this.config = config;
        this.cacheRepository = cacheRepository;
    }

    /**
     * Get live scores from API with caching
     * @return JSON response as String
     */
    public String getLiveScores() {
        return callApiWithCache("/matches/live.json");
    }

    /**
     * Get competitions from API with caching
     * @return JSON response as String
     */
    public String getCompetitions() {
        return callApiWithCache("/competitions/list.json");
    }

    /**
     * Get fixtures from API with caching
     * @return JSON response as String
     */
    public String getFixtures() {
        return callApiWithCache("/fixtures/list.json");
    }
    
    /**
     * Get match history with caching
     * @return JSON response as String
     */
    public String getMatchHistory() {
        return callApiWithCache("/matches/history.json");
    }

    /**
     * Get match events with caching
     * @param matchId Match ID
     * @return JSON response as String
     */
    public String getMatchEvents(String matchId) {
        return callApiWithCache("/matches/events.json?match_id=" + matchId);
    }
    
    /**
     * Get match lineups with caching
     * @param matchId Match ID
     * @return JSON response as String
     */
    public String getMatchLineups(String matchId) {
        return callApiWithCache("/matches/lineups.json?match_id=" + matchId);
    }
    
    /**
     * Get match commentary with caching
     * @param matchId Match ID
     * @return JSON response as String
     */
    public String getMatchCommentary(String matchId) {
        return callApiWithCache("/matches/commentary.json?match_id=" + matchId);
    }

    /**
     * Get match details with caching
     * @param matchId Match ID
     * @return JSON response as String
     */
    public String getMatchDetails(String matchId) {
        return callApiWithCache("/matches/view.json?id=" + matchId);
    }

    /**
     * Get team details with caching
     * @param teamId Team ID
     * @return JSON response as String
     */
    public String getTeamDetails(String teamId) {
        return callApiWithCache("/teams/view.json?id=" + teamId);
    }
    
    /**
     * Get all teams with caching
     * @return JSON response as String
     */
    public String getTeams() {
        return callApiWithCache("/teams/list.json");
    }
    
    /**
     * Get World Cup 2026 participants
     * @return JSON response as String
     */
    public String getWorldCupParticipants() {
        return callApiWithCache("/competitions/participants.json?competition_id=362&season=2026");
    }
    
    /**
     * Get World Cup 2026 group table/standings
     * @param groupId Group ID (e.g., 4286 for Group A)
     * @return JSON response as String
     */
    public String getWorldCupGroupTable(String groupId) {
        return callApiWithCache("/groups/table.json?competition_id=362&group_id=" + groupId);
    }
    
    /**
     * Get World Cup 2026 live group standings
     * @param groupId Group ID (e.g., 4286 for Group A)
     * @return JSON response as String
     */
    public String getWorldCupLiveStandings(String groupId) {
        return callApiWithCache("/standings/live.json?competition_id=362&group_id=" + groupId);
    }
    
    /**
     * Get World Cup 2026 all fixtures
     * @return JSON response as String
     */
    public String getWorldCupFixtures() {
        return callApiWithCache("/fixtures/list.json?competition_id=362");
    }
    
    /**
     * Get World Cup 2026 group fixtures
     * @param groupId Group ID (e.g., 4286 for Group A)
     * @return JSON response as String
     */
    public String getWorldCupGroupFixtures(String groupId) {
        return callApiWithCache("/fixtures/list.json?competition_id=362&group_id=" + groupId);
    }
    
    /**
     * Get World Cup 2026 team fixtures
     * @param teamId Team ID (e.g., 1450 for Mexico)
     * @return JSON response as String
     */
    public String getWorldCupTeamFixtures(String teamId) {
        return callApiWithCache("/fixtures/list.json?competition_id=362&team=" + teamId);
    }
    
    /**
     * Get head to head data with caching
     * @param team1Id First team ID
     * @param team2Id Second team ID
     * @return JSON response as String
     */
    public String getHeadToHead(String team1Id, String team2Id) {
        return callApiWithCache("/teams/head2head.json?team1_id=" + team1Id + "&team2_id=" + team2Id);
    }
    
    /**
     * Get country flag URL
     * @param countryId Country ID
     * @return Flag image URL
     */
    public String getCountryFlag(String countryId) {
        return config.getBaseUrl() + "/countries/flag.png?key=" + config.getKey() + 
               "&secret=" + config.getSecret() + "&country_id=" + countryId;
    }
    
    /**
     * Get standings with caching (if available)
     * @param competitionId Competition ID
     * @return JSON response as String
     */
    public String getStandings(String competitionId) {
        return callApiWithCache("/competitions/standings.json?competition_id=" + competitionId);
    }
    
    /**
     * Get all seasons with caching
     * @return JSON response as String
     */
    public String getSeasons() {
        return callApiWithCache("/seasons/list.json");
    }
    
    /**
     * Get fixtures by competition ID
     * @param competitionId Competition ID
     * @return JSON response as String
     */
    public String getFixturesByCompetition(String competitionId) {
        return callApiWithCache("/fixtures/list.json?competition_id=" + competitionId);
    }
    
    /**
     * Get fixtures by date (format: YYYY-MM-DD, or "today")
     * @param date Date in format YYYY-MM-DD or "today"
     * @return JSON response as String
     */
    public String getFixturesByDate(String date) {
        return callApiWithCache("/fixtures/list.json?date=" + date);
    }
    
    /**
     * Get competition groups (e.g., Group A, B, C for World Cup)
     * @param competitionId Competition ID
     * @return JSON response as String
     */
    public String getCompetitionGroups(String competitionId) {
        return callApiWithCache("/groups/list.json?competition_id=" + competitionId);
    }
    
    /**
     * Get standings for a specific group
     * @param groupId Group ID
     * @return JSON response as String
     */
    public String getGroupStandings(String groupId) {
        return callApiWithCache("/groups/standings.json?group_id=" + groupId);
    }
    
    /**
     * Get live standings (updated during matches)
     * @param competitionId Competition ID
     * @return JSON response as String
     */
    public String getLiveStandings(String competitionId) {
        return callApiWithCache("/competitions/live-standings.json?competition_id=" + competitionId);
    }
    
    /**
     * Get top goalscorers for a competition
     * @param competitionId Competition ID
     * @return JSON response as String
     */
    public String getTopScorers(String competitionId) {
        return callApiWithCache("/competitions/scorers.json?competition_id=" + competitionId);
    }
    
    /**
     * Get all countries
     * @return JSON response as String
     */
    public String getCountries() {
        return callApiWithCache("/countries/list.json");
    }
    
    /**
     * Get team's last matches
     * @param teamId Team ID
     * @param limit Number of matches to return (default: 5)
     * @return JSON response as String
     */
    public String getTeamLastMatches(String teamId, int limit) {
        return callApiWithCache("/teams/lastmatches.json?team_id=" + teamId + "&limit=" + limit);
    }

    /**
     * Generic method to call API with caching
     * @param endpoint API endpoint path
     * @return JSON response as String
     */
    public String callApiWithCache(String endpoint) {
        String fullUrl = config.getBaseUrl() + endpoint;
        
        // Check for valid cached response
        Optional<ApiCache> cachedResponse = cacheRepository.findValidCacheByUrl(
            fullUrl, 
            LocalDateTime.now()
        );

        if (cachedResponse.isPresent()) {
            log.info("Returning cached response for URL: {}", fullUrl);
            return cachedResponse.get().getJsonResponse();
        }

        // No valid cache, make API call
        log.info("Making API call to: {}", fullUrl);
        String jsonResponse = callApi(endpoint);

        // Save to cache (non-critical, continue if it fails)
        try {
            saveToCache(fullUrl, jsonResponse);
        } catch (Exception e) {
            log.warn("Failed to save API response to cache: {}", e.getMessage());
        }

        return jsonResponse;
    }

    /**
     * Make actual HTTP API call
     * @param endpoint API endpoint path
     * @return JSON response as String
     */
    private String callApi(String endpoint) {
        try {
            // Add API key and secret as query parameters
            String separator = endpoint.contains("?") ? "&" : "?";
            String fullUrl = config.getBaseUrl() + endpoint + separator + 
                           "key=" + config.getKey() + "&secret=" + config.getSecret();
            
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Set request method and headers
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            
            // Set timeouts
            conn.setConnectTimeout(10000); // 10 seconds
            conn.setReadTimeout(10000);    // 10 seconds

            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
                );
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                return response.toString();
            } else {
                log.error("API call failed with response code: {}", responseCode);
                
                // Try to read error response
                String errorContent = "";
                try {
                    if (conn.getErrorStream() != null) {
                        BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8)
                        );
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        
                        while ((errorLine = errorReader.readLine()) != null) {
                            errorResponse.append(errorLine);
                        }
                        errorReader.close();
                        errorContent = errorResponse.toString();
                        
                        // Log first 500 characters of error to help debug
                        String preview = errorContent.length() > 500 
                            ? errorContent.substring(0, 500) + "..." 
                            : errorContent;
                        log.error("Error response preview: {}", preview);
                    }
                } catch (Exception e) {
                    log.error("Could not read error stream", e);
                }
                
                throw new RuntimeException("API call failed with HTTP " + responseCode + 
                    ". Endpoint: " + endpoint + 
                    ". Check your API credentials and endpoint format.");
            }
        } catch (Exception e) {
            log.error("Error calling API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Live Score API", e);
        }
    }

    /**
     * Save API response to cache.
     * Uses saveAndFlush to immediately persist and detach, avoiding stale state issues.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void saveToCache(String url, String jsonResponse) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresAt = now.plusMinutes(config.getCacheDurationMinutes());

            // Check if cache entry already exists
            Optional<ApiCache> existing = cacheRepository.findByUrl(url);
            
            if (existing.isPresent()) {
                // Update existing cache entry
                ApiCache cache = existing.get();
                cache.setJsonResponse(jsonResponse);
                cache.setCachedAt(now);
                cache.setExpiresAt(expiresAt);
                cacheRepository.saveAndFlush(cache);
                log.info("Updated cache for URL: {}", url);
            } else {
                // Create new cache entry
                ApiCache cache = ApiCache.builder()
                    .url(url)
                    .jsonResponse(jsonResponse)
                    .cachedAt(now)
                    .expiresAt(expiresAt)
                    .build();
                cacheRepository.saveAndFlush(cache);
                log.info("Saved new cache for URL: {}", url);
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Duplicate key - another thread already saved this cache entry, just log and continue
            log.debug("Cache entry already exists for URL (race condition): {}", url);
        } catch (ObjectOptimisticLockingFailureException e) {
            // Version conflict - another transaction updated this cache entry, just log and continue
            log.warn("Cache entry was updated by another transaction for URL: {}", url);
        } catch (Exception e) {
            log.error("Error saving cache for URL {}: {}", url, e.getMessage());
        }
    }

    /**
     * Clean up expired cache entries - runs every hour
     */
    @Transactional
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void cleanExpiredCache() {
        log.info("Cleaning expired cache entries...");
        cacheRepository.deleteExpiredCache(LocalDateTime.now());
        log.info("Expired cache cleanup completed");
    }

    /**
     * Clear all cache entries
     */
    @Transactional
    public void clearAllCache() {
        log.info("Clearing all cache entries...");
        cacheRepository.deleteAll();
        log.info("All cache cleared");
    }

    /**
     * Clear cache for specific URL
     * @param endpoint API endpoint path
     */
    @Transactional
    public void clearCacheForEndpoint(String endpoint) {
        String fullUrl = config.getBaseUrl() + endpoint;
        cacheRepository.findByUrl(fullUrl).ifPresent(cache -> {
            cacheRepository.delete(cache);
            log.info("Cleared cache for URL: {}", fullUrl);
        });
    }
}
