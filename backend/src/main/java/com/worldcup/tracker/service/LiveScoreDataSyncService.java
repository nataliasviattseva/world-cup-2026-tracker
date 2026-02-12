package com.worldcup.tracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worldcup.tracker.model.*;
import com.worldcup.tracker.repository.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Slf4j
@Service
@Transactional
public class LiveScoreDataSyncService {

    private final LiveScoreApiService apiService;
    private final MatchRepository matchRepository;
    private final EquipeRepository equipeRepository;
    private final StadeRepository stadeRepository;
    private final PhaseCompetitionRepository phaseRepository;
    private final GroupeRepository groupeRepository;
    private final ClassementGroupeRepository classementRepository;
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LiveScoreDataSyncService(
            LiveScoreApiService apiService,
            MatchRepository matchRepository,
            EquipeRepository equipeRepository,
            StadeRepository stadeRepository,
            PhaseCompetitionRepository phaseRepository,
            GroupeRepository groupeRepository,
            ClassementGroupeRepository classementRepository) {
        this.apiService = apiService;
        this.matchRepository = matchRepository;
        this.equipeRepository = equipeRepository;
        this.stadeRepository = stadeRepository;
        this.phaseRepository = phaseRepository;
        this.groupeRepository = groupeRepository;
        this.classementRepository = classementRepository;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Initialize data from Live Score API on startup
     */
    @Bean
    public CommandLineRunner syncDataOnStartup() {
        return args -> {
            log.info("=== Starting Live Score API Data Synchronization ===");
            log.info("Note: World Cup 2026 hasn't happened yet, so the API may not have specific fixtures.");
            log.info("The sync will load any available competitions and matches from the API.");
            
            try {
                // Sync competitions and matches
                syncCompetitionsAndMatches();
                
                log.info("=== Live Score API Data Synchronization Completed ===");
            } catch (Exception e) {
                log.error("Error during data synchronization: {}", e.getMessage(), e);
            }
        };
    }

    /**
     * Sync live matches every 5 seconds
     */
    @Scheduled(fixedRate = 5000) // 5 seconds
    public void syncLiveMatches() {
        log.info("Syncing live matches...");
        
        try {
            String liveScoresJson = apiService.getLiveScores();
            JsonNode rootNode = objectMapper.readTree(liveScoresJson);
            
            // The API returns: {"success": true, "data": [ {match1}, {match2}, ... ]}
            if (rootNode.has("data") && rootNode.get("data").isArray()) {
                JsonNode matches = rootNode.get("data");
                
                log.info("Found {} matches in API response", matches.size());
                for (JsonNode matchNode : matches) {
                    syncMatchFromApi(matchNode, true);
                }
                log.info("Live matches sync completed");
            } else {
                log.warn("No 'data' array found in API response");
            }
        } catch (Exception e) {
            log.error("Error syncing live matches: {}", e.getMessage());
        }
    }

    /**
     * Sync competitions and all matches
     */
    public void syncCompetitionsAndMatches() {
        boolean foundWorldCup = false;
        
        try {
            log.info("Fetching competitions from Live Score API...");
            String competitionsJson = apiService.getCompetitions();
            JsonNode rootNode = objectMapper.readTree(competitionsJson);
            
            // Log the API response structure
            log.info("Competitions API Response keys: {}", rootNode.fieldNames());
            log.debug("Full competitions response: {}", rootNode.toPrettyString());
            
            if (rootNode.has("data") && rootNode.get("data").has("competition")) {
                JsonNode competitions = rootNode.get("data").get("competition");
                
                if (competitions.isArray()) {
                    log.info("Found {} competitions", competitions.size());
                    
                    // Log all available competitions
                    log.info("=== Available Competitions in API ===");
                    for (JsonNode compNode : competitions) {
                        String compName = compNode.has("name") ? compNode.get("name").asText() : "Unknown";
                        String compId = compNode.has("id") ? compNode.get("id").asText() : "Unknown";
                        log.info("  - {} (ID: {})", compName, compId);
                    }
                    log.info("=====================================");
                    
                    // Look for World Cup 2026
                    for (JsonNode compNode : competitions) {
                        String compName = compNode.has("name") ? compNode.get("name").asText() : "";
                        
                        if (compName.toLowerCase().contains("world cup") && compName.contains("2026")) {
                            log.info("Found World Cup 2026: {}", compName);
                            String competitionId = compNode.get("id").asText();
                            
                            // Sync fixtures for this competition
                            syncFixtures(competitionId);
                            
                            // Sync standings
                            syncStandings(competitionId);
                            foundWorldCup = true;
                            break;
                        }
                    }
                    
                    if (!foundWorldCup) {
                        log.warn("World Cup 2026 not found in competitions list.");
                        log.warn("The tournament may not have started yet or is not available in the API.");
                        log.warn("Check the list of available competitions above.");
                    }
                } else {
                    log.warn("Competitions node is not an array");
                }
            } else {
                log.warn("No 'data.competition' found in API response");
            }
            
            // Also sync current live matches
            syncLiveMatches();
            
            // Load World Cup 2026 data now that we have the correct competition ID
            log.info("=== Loading World Cup 2026 data (Competition ID: 362) ===");
            syncWorldCup2026Data();
            
            // Load some fixture data to populate tables (for testing until World Cup 2026)
            if (!foundWorldCup) {
                log.info("=== World Cup 2026 not found in competitions list, using direct competition ID 362 ===");
            }
            
        } catch (Exception e) {
            log.error("Error syncing competitions: {}", e.getMessage(), e);
        }
    }

    /**
     * Sync today's fixtures to populate match data
     */
    private void syncTodayFixtures() {
        try {
            log.info("Fetching today's fixtures from API...");
            String fixturesJson = apiService.getFixturesByDate("today");
            JsonNode rootNode = objectMapper.readTree(fixturesJson);
            
            log.debug("Fixtures API response: {}", rootNode.toPrettyString());
            
            if (rootNode.has("data") && rootNode.get("data").has("fixtures") && rootNode.get("data").get("fixtures").isArray()) {
                JsonNode fixtures = rootNode.get("data").get("fixtures");
                log.info("Found {} fixtures for today", fixtures.size());
                
                int count = 0;
                for (JsonNode matchNode : fixtures) {
                    syncMatchFromApi(matchNode, false);
                    count++;
                    if (count >= 10) break; // Limit to 10 matches for testing
                }
                log.info("Synced {} fixtures", count);
            } else {
                log.warn("No fixtures found for today. Response structure: {}", rootNode.fieldNames());
            }
        } catch (Exception e) {
            log.error("Error syncing today's fixtures: {}", e.getMessage());
        }
    }
    
    /**
     * Sync recent match history to populate tables
     */
    private void syncRecentMatches() {
        try {
            log.info("Fetching recent match history from API...");
            String historyJson = apiService.getMatchHistory();
            JsonNode rootNode = objectMapper.readTree(historyJson);
            
            log.debug("Match history API response: {}", rootNode.toPrettyString());
            
            if (rootNode.has("data") && rootNode.get("data").has("match") && rootNode.get("data").get("match").isArray()) {
                JsonNode matches = rootNode.get("data").get("match");
                log.info("Found {} recent matches", matches.size());
                
                int count = 0;
                for (JsonNode matchNode : matches) {
                    syncMatchFromApi(matchNode, false);
                    count++;
                    if (count >= 10) break; // Limit to 10 matches for testing
                }
                log.info("Synced {} recent matches", count);
            } else {
                log.warn("No recent matches found. Response structure: {}", rootNode.fieldNames());
            }
        } catch (Exception e) {
            log.error("Error syncing recent matches: {}", e.getMessage());
        }
    }

    /**
     * Sync teams from API to populate equipes table
     */
    private void syncTeamsFromApi() {
        try {
            log.info("Fetching World Cup 2026 participants from API...");
            String teamsJson = apiService.getWorldCupParticipants();
            JsonNode rootNode = objectMapper.readTree(teamsJson);
            
            log.debug("World Cup participants API response: {}", rootNode.toPrettyString());
            
            JsonNode teamsNode = null;
            
            if (rootNode.has("data")) {
                JsonNode dataNode = rootNode.get("data");
                if (dataNode.isArray()) {
                    teamsNode = dataNode;
                } else if (dataNode.has("teams") && dataNode.get("teams").isArray()) {
                    teamsNode = dataNode.get("teams");
                } else if (dataNode.has("team") && dataNode.get("team").isArray()) {
                    teamsNode = dataNode.get("team");
                } else if (dataNode.has("participants") && dataNode.get("participants").isArray()) {
                    teamsNode = dataNode.get("participants");
                }
            }
            
            if (teamsNode != null && teamsNode.isArray()) {
                log.info("Found {} World Cup 2026 participants from API", teamsNode.size());
                
                int count = 0;
                for (JsonNode teamNode : teamsNode) {
                    syncTeamFromApi(teamNode);
                    count++;
                }
                log.info("Successfully synced {} World Cup 2026 participants", count);
            } else {
                log.warn("No World Cup participants found in API response. Response keys: {}", rootNode.fieldNames());
                
                // Fallback to general teams endpoint
                log.info("Falling back to general teams endpoint...");
                syncGeneralTeamsFromApi();
            }
        } catch (Exception e) {
            log.error("Error syncing World Cup participants from API: {}", e.getMessage());
            
            // Fallback to general teams endpoint
            log.info("Falling back to general teams endpoint due to error...");
            syncGeneralTeamsFromApi();
        }
    }
    
    /**
     * Sync a single team from API data
     */
    private void syncTeamFromApi(JsonNode teamNode) {
        try {
            if (!teamNode.has("name")) {
                log.warn("Skipping team without name");
                return;
            }
            
            String teamName = teamNode.get("name").asText();
            String teamId = teamNode.has("id") ? teamNode.get("id").asText() : null;
            String countryCode = teamNode.has("cc") ? teamNode.get("cc").asText() : 
                                teamName.substring(0, Math.min(3, teamName.length())).toUpperCase();
            
            // Map country names to proper flags
            String flagUrl = getCountryFlag(teamName, countryCode);
            
            // Extract group information from API if available
            String groupLetter = null;
            String groupName = null;
            Groupe groupe = null;
            
            if (teamNode.has("group_name")) {
                String apiGroupName = teamNode.get("group_name").asText();
                // Extract letter from names like "Group A", "Groupe A", etc.
                if (apiGroupName != null && apiGroupName.length() > 0) {
                    String lastChar = apiGroupName.substring(apiGroupName.length() - 1);
                    if (lastChar.matches("[A-L]")) {
                        groupLetter = lastChar;
                        groupName = "Groupe " + groupLetter;
                    }
                }
            } else if (teamNode.has("group")) {
                String apiGroup = teamNode.get("group").asText();
                if (apiGroup != null && apiGroup.matches("[A-L]")) {
                    groupLetter = apiGroup;
                    groupName = "Groupe " + groupLetter;
                }
            }
            
            // Create or find the group if we have group information
            if (groupLetter != null) {
                final String finalGroupLetter = groupLetter;
                groupe = groupeRepository.findByLettreIgnoreCase(groupLetter)
                        .orElseGet(() -> {
                            Groupe newGroupe = new Groupe();
                            newGroupe.setLettre(finalGroupLetter);
                            newGroupe.setNom("Groupe " + finalGroupLetter);
                            Groupe saved = groupeRepository.save(newGroupe);
                            log.info("Created group: {}", saved.getNom());
                            return saved;
                        });
            }
            
            // Check if team already exists
            Equipe existingTeam = equipeRepository.findAll().stream()
                    .filter(e -> e.getNom().equalsIgnoreCase(teamName))
                    .findFirst()
                    .orElse(null);
            
            if (existingTeam == null) {
                Equipe newTeam = Equipe.builder()
                        .nom(teamName)
                        .codePays(countryCode)
                        .drapeauUrl(flagUrl)
                        .groupeCode(groupLetter)
                        .groupe(groupe)
                        .build();
                equipeRepository.save(newTeam);
                log.info("Created team: {} with flag: {} in group: {}", teamName, flagUrl, groupLetter != null ? groupLetter : "none");
            } else if (groupe != null && existingTeam.getGroupe() == null) {
                // Update existing team with group information
                existingTeam.setGroupe(groupe);
                existingTeam.setGroupeCode(groupLetter);
                equipeRepository.save(existingTeam);
                log.info("Updated team {} with group: {}", teamName, groupLetter);
            }
        } catch (Exception e) {
            log.error("Error syncing team: {}", e.getMessage());
        }
    }
    
    /**
     * Get country flag emoji based on team name or country code
     */
    private String getCountryFlag(String teamName, String countryCode) {
        // World Cup 2026 specific mappings
        switch (teamName.toLowerCase()) {
            // Group A
            case "mexico":
                return "🇲🇽";
            case "south africa":
                return "🇿🇦";
            case "republic of korea":
            case "korea republic":
                return "🇰🇷";
            
            // Group B
            case "canada":
                return "🇨🇦";
            case "qatar":
                return "🇶🇦";
            case "switzerland":
                return "🇨🇭";
            
            // Group C
            case "brazil":
                return "🇧🇷";
            case "morocco":
                return "🇲🇦";
            case "haiti":
                return "🇭🇹";
            case "scotland":
                return "🏴󠁧󠁢󠁳󠁣󠁴󠁿";
            
            // Group D
            case "united states":
            case "usa":
                return "🇺🇸";
            case "paraguay":
                return "🇵🇾";
            case "australia":
                return "🇦🇺";
            
            // Group E
            case "germany":
                return "🇩🇪";
            case "curacao":
                return "🇨🇼";
            case "ivory coast":
            case "côte d'ivoire":
                return "🇨🇮";
            case "ecuador":
                return "🇪🇨";
            
            // Group F
            case "netherlands":
                return "🇳🇱";
            case "japan":
                return "🇯🇵";
            case "tunisia":
                return "🇹🇳";
            
            // Group G
            case "belgium":
                return "🇧🇪";
            case "egypt":
                return "🇪🇬";
            case "iran":
                return "🇮🇷";
            case "new zealand":
                return "🇳🇿";
            
            // Group H
            case "spain":
                return "🇪🇸";
            case "cape verde":
                return "🇨🇻";
            case "saudi arabia":
                return "🇸🇦";
            case "uruguay":
                return "🇺🇾";
            
            // Group I
            case "france":
                return "🇫🇷";
            case "senegal":
                return "🇸🇳";
            case "norway":
                return "🇳🇴";
            
            // Group J
            case "argentina":
                return "🇦🇷";
            case "algeria":
                return "🇩🇿";
            case "austria":
                return "🇦🇹";
            case "jordan":
                return "🇯🇴";
            
            // Group K
            case "portugal":
                return "🇵🇹";
            case "uzbekistan":
                return "🇺🇿";
            case "colombia":
                return "🇨🇴";
            
            // Group L
            case "england":
                return "🏴󠁧󠁢󠁥󠁮󠁧󠁿";
            case "croatia":
                return "🇭🇷";
            case "ghana":
                return "🇬🇭";
            case "panama":
                return "🇵🇦";
            
            // Additional common variations
            case "italy":
                return "🇮🇹";
            default:
                // Fallback based on country code
                return getCountryFlagByCode(countryCode);
        }
    }
    
    /**
     * Get country flag by country code
     */
    private String getCountryFlagByCode(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) {
            return "🏳️";
        }
        
        switch (countryCode.toUpperCase()) {
            // Group A
            case "MX": return "🇲🇽";
            case "ZA": return "🇿🇦";
            case "KR": return "🇰🇷";
            
            // Group B
            case "CA": return "🇨🇦";
            case "QA": return "🇶🇦";
            case "CH": return "🇨🇭";
            
            // Group C
            case "BR": return "🇧🇷";
            case "MA": return "🇲🇦";
            case "HT": return "🇭🇹";
            
            // Group D
            case "US": return "🇺🇸";
            case "PY": return "🇵🇾";
            case "AU": return "🇦🇺";
            
            // Group E
            case "DE": return "🇩🇪";
            case "CW": return "🇨🇼";
            case "CI": return "🇨🇮";
            case "EC": return "🇪🇨";
            
            // Group F
            case "NL": return "🇳🇱";
            case "JP": return "🇯🇵";
            case "TN": return "🇹🇳";
            
            // Group G
            case "BE": return "🇧🇪";
            case "EG": return "🇪🇬";
            case "IR": return "🇮🇷";
            case "NZ": return "🇳🇿";
            
            // Group H
            case "ES": return "🇪🇸";
            case "CV": return "🇨🇻";
            case "SA": return "🇸🇦";
            case "UY": return "🇺🇾";
            
            // Group I
            case "FR": return "🇫🇷";
            case "SN": return "🇸🇳";
            case "NO": return "🇳🇴";
            
            // Group J
            case "AR": return "🇦🇷";
            case "DZ": return "🇩🇿";
            case "AT": return "🇦🇹";
            case "JO": return "🇯🇴";
            
            // Group K
            case "PT": return "🇵🇹";
            case "UZ": return "🇺🇿";
            case "CO": return "🇨🇴";
            
            // Group L
            case "GB": return "🇬🇧";
            case "HR": return "🇭🇷";
            case "GH": return "🇬🇭";
            case "PA": return "🇵🇦";
            
            // Additional common codes
            case "IT": return "🇮🇹";
            default: return "🏳️";
        }
    }
    
    /**
     * Fallback method to sync teams from general teams endpoint
     */
    private void syncGeneralTeamsFromApi() {
        try {
            log.info("Fetching teams from general API endpoint...");
            String teamsJson = apiService.getTeams();
            JsonNode rootNode = objectMapper.readTree(teamsJson);
            
            log.debug("General teams API response: {}", rootNode.toPrettyString());
            
            JsonNode teamsNode = null;
            
            if (rootNode.has("data")) {
                JsonNode dataNode = rootNode.get("data");
                if (dataNode.isArray()) {
                    teamsNode = dataNode;
                } else if (dataNode.has("teams") && dataNode.get("teams").isArray()) {
                    teamsNode = dataNode.get("teams");
                } else if (dataNode.has("team") && dataNode.get("team").isArray()) {
                    teamsNode = dataNode.get("team");
                }
            }
            
            if (teamsNode != null && teamsNode.isArray()) {
                log.info("Found {} teams from general API", teamsNode.size());
                
                int count = 0;
                for (JsonNode teamNode : teamsNode) {
                    syncTeamFromApi(teamNode);
                    count++;
                    if (count >= 50) break; // Limit to 50 teams
                }
                log.info("Successfully synced {} teams from general API", count);
            } else {
                log.warn("No teams found in general API response. Response keys: {}", rootNode.fieldNames());
            }
        } catch (Exception e) {
            log.error("Error syncing teams from general API: {}", e.getMessage());
        }
    }
    
    /**
     * Sync World Cup qualifier matches to populate tables
     */
    private void syncWorldCupQualifierMatches() {
        try {
            // World Cup CONMEBOL Qualifiers (ID: 361) - South American qualifiers
            log.info("Fetching World Cup CONMEBOL Qualifier matches...");
            String fixturesJson = apiService.getFixturesByCompetition("361");
            syncMatchesFromResponse(fixturesJson, "World Cup CONMEBOL Qualifiers");
            
        } catch (Exception e) {
            log.error("Error syncing World Cup qualifier matches: {}", e.getMessage());
        }
    }
    
    /**
     * Sync World Cup 2026 specific data (teams, groups, fixtures)
     */
    private void syncWorldCup2026Data() {
        try {
            log.info("Starting World Cup 2026 data synchronization...");
            
            // 1. Sync World Cup participants
            log.info("=== Syncing World Cup 2026 Participants ===");
            syncTeamsFromApi(); // This now uses the World Cup participants endpoint
            
            // 2. Sync World Cup 2026 fixtures
            log.info("=== Syncing World Cup 2026 Fixtures ===");
            syncWorldCupFixtures();
            
            // 3. Sync team fixtures (more likely to work than group standings)
            log.info("=== Syncing Team Fixtures ===");
            syncAllWorldCupTeamFixtures();
            
            // 4. Try to sync group data (may fail due to API availability)
            log.info("=== Attempting to Sync Group Data ===");
            syncAllWorldCupGroupsWithFallback();
            
            log.info("World Cup 2026 data synchronization completed");
            
        } catch (Exception e) {
            log.error("Error syncing World Cup 2026 data: {}", e.getMessage());
            log.info("Continuing with available data...");
        }
    }
    
    /**
     * Attempt to sync all World Cup groups with fallback handling
     */
    private void syncAllWorldCupGroupsWithFallback() {
        String[] groupIds = {"4286", "4287", "4288", "4289", "4290", "4291", "4292", "4293", "4297", "4296", "4295", "4294"};
        String[] groupNames = {"Group A", "Group B", "Group C", "Group D", "Group E", "Group F", "Group G", "Group H", "Group I", "Group J", "Group K", "Group L"};
        
        int successCount = 0;
        int failCount = 0;
        
        for (int i = 0; i < groupIds.length; i++) {
            try {
                syncWorldCupGroupDataSafely(groupIds[i], groupNames[i]);
                successCount++;
                
                // Add small delay between requests to avoid overwhelming the API
                Thread.sleep(1000);
                
            } catch (Exception e) {
                failCount++;
                log.warn("Failed to sync {} (ID: {}): {}. Continuing with next group...", 
                        groupNames[i], groupIds[i], e.getMessage());
            }
        }
        
        log.info("Group sync completed: {} successful, {} failed", successCount, failCount);
    }
    
    /**
     * Sync data for a specific World Cup group with safer error handling
     */
    private void syncWorldCupGroupDataSafely(String groupId, String groupName) {
        log.info("Attempting to sync {} (ID: {})...", groupName, groupId);
        
        try {
            // Try group fixtures first (more likely to work)
            log.debug("Fetching {} fixtures...", groupName);
            String fixturesJson = apiService.getWorldCupGroupFixtures(groupId);
            syncMatchesFromResponse(fixturesJson, groupName + " Fixtures");
            
            // Try group standings (may fail with 503)
            try {
                log.debug("Fetching {} standings...", groupName);
                String standingsJson = apiService.getWorldCupGroupTable(groupId);
                log.debug("{} standings response: {}", groupName, standingsJson);
            } catch (Exception standingsError) {
                log.warn("Group standings unavailable for {}: {}. Fixtures sync completed successfully.", 
                         groupName, standingsError.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Failed to sync any data for {}: {}", groupName, e.getMessage());
            throw e; // Re-throw to be caught by the caller
        }
    }
    
    /**
     * Sync all World Cup 2026 fixtures
     */
    private void syncWorldCupFixtures() {
        try {
            log.info("Fetching World Cup 2026 fixtures...");
            String fixturesJson = apiService.getWorldCupFixtures();
            JsonNode rootNode = objectMapper.readTree(fixturesJson);
            
            log.debug("World Cup fixtures API response: {}", rootNode.toPrettyString());
            
            JsonNode fixturesNode = null;
            
            if (rootNode.has("data")) {
                JsonNode dataNode = rootNode.get("data");
                if (dataNode.has("fixtures") && dataNode.get("fixtures").isArray()) {
                    fixturesNode = dataNode.get("fixtures");
                } else if (dataNode.isArray()) {
                    fixturesNode = dataNode;
                }
            }
            
            if (fixturesNode != null && fixturesNode.isArray()) {
                log.info("Found {} World Cup 2026 fixtures", fixturesNode.size());
                
                int count = 0;
                for (JsonNode matchNode : fixturesNode) {
                    syncMatchFromApi(matchNode, false);
                    count++;
                }
                log.info("Successfully synced {} World Cup 2026 fixtures", count);
            } else {
                log.warn("No fixtures found in World Cup 2026 API response");
            }
        } catch (Exception e) {
            log.error("Error syncing World Cup 2026 fixtures: {}", e.getMessage());
        }
    }
    
    /**
     * Sync fixtures for all World Cup 2026 teams
     */
    private void syncAllWorldCupTeamFixtures() {
        // Group A teams
        syncWorldCupTeamFixtures("1450", "Mexico");
        syncWorldCupTeamFixtures("2767", "South Africa"); 
        syncWorldCupTeamFixtures("1452", "Republic of Korea");
        
        // Group B teams
        syncWorldCupTeamFixtures("1674", "Canada");
        syncWorldCupTeamFixtures("1427", "Qatar");
        syncWorldCupTeamFixtures("208", "Switzerland");
        
        // Group C teams
        syncWorldCupTeamFixtures("1448", "Brazil");
        syncWorldCupTeamFixtures("1435", "Morocco");
        syncWorldCupTeamFixtures("2777", "Haiti");
        syncWorldCupTeamFixtures("1741", "Scotland");
        
        // Group D teams
        syncWorldCupTeamFixtures("1849", "USA");
        syncWorldCupTeamFixtures("4040", "Paraguay");
        syncWorldCupTeamFixtures("1440", "Australia");
        
        // Group E teams
        syncWorldCupTeamFixtures("1449", "Germany");
        syncWorldCupTeamFixtures("2732", "Curacao");
        syncWorldCupTeamFixtures("1628", "Ivory Coast");
        syncWorldCupTeamFixtures("1847", "Ecuador");
        
        // Group F teams
        syncWorldCupTeamFixtures("1649", "Netherlands");
        syncWorldCupTeamFixtures("1458", "Japan");
        syncWorldCupTeamFixtures("1455", "Tunisia");
        
        // Group G teams
        syncWorldCupTeamFixtures("1453", "Belgium");
        syncWorldCupTeamFixtures("215", "Egypt");
        syncWorldCupTeamFixtures("1436", "Iran");
        syncWorldCupTeamFixtures("5345", "New Zealand");
        
        // Group H teams
        syncWorldCupTeamFixtures("1438", "Spain");
        syncWorldCupTeamFixtures("1608", "Cape Verde");
        syncWorldCupTeamFixtures("1432", "Saudi Arabia");
        syncWorldCupTeamFixtures("1434", "Uruguay");
        
        // Group I teams
        syncWorldCupTeamFixtures("1439", "France");
        syncWorldCupTeamFixtures("1460", "Senegal");
        syncWorldCupTeamFixtures("2677", "Norway");
        
        // Group J teams
        syncWorldCupTeamFixtures("1443", "Argentina");
        syncWorldCupTeamFixtures("1528", "Algeria");
        syncWorldCupTeamFixtures("2684", "Austria");
        syncWorldCupTeamFixtures("1790", "Jordan");
        
        // Group K teams
        syncWorldCupTeamFixtures("1437", "Portugal");
        syncWorldCupTeamFixtures("1776", "Uzbekistan");
        syncWorldCupTeamFixtures("1457", "Colombia");
        
        // Group L teams
        syncWorldCupTeamFixtures("1456", "England");
        syncWorldCupTeamFixtures("211", "Croatia");
        syncWorldCupTeamFixtures("214", "Ghana");
        syncWorldCupTeamFixtures("1454", "Panama");
    }
    
    /**
     * Sync fixtures for a specific World Cup team with error handling
     */
    private void syncWorldCupTeamFixtures(String teamId, String teamName) {
        try {
            log.debug("Fetching fixtures for {} (ID: {})...", teamName, teamId);
            String fixturesJson = apiService.getWorldCupTeamFixtures(teamId);
            syncMatchesFromResponse(fixturesJson, teamName + " Fixtures");
            
        } catch (Exception e) {
            log.warn("Failed to sync fixtures for {}: {}. Continuing with next team...", teamName, e.getMessage());
        }
    }
    
    /**
     * Generic method to sync matches from API response
     */
    private void syncMatchesFromResponse(String jsonResponse, String source) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            
            log.debug("{} API response: {}", source, rootNode.toPrettyString());
            
            // Try different response structures
            JsonNode matchesNode = null;
            
            if (rootNode.has("data")) {
                JsonNode dataNode = rootNode.get("data");
                if (dataNode.isArray()) {
                    matchesNode = dataNode;
                } else if (dataNode.has("fixtures") && dataNode.get("fixtures").isArray()) {
                    matchesNode = dataNode.get("fixtures");
                } else if (dataNode.has("match") && dataNode.get("match").isArray()) {
                    matchesNode = dataNode.get("match");
                }
            }
            
            if (matchesNode != null && matchesNode.isArray()) {
                log.info("Found {} matches from {}", matchesNode.size(), source);
                
                int count = 0;
                for (JsonNode matchNode : matchesNode) {
                    syncMatchFromApi(matchNode, false);
                    count++;
                    if (count >= 20) break; // Limit to 20 matches
                }
                log.info("Successfully synced {} matches from {}", count, source);
            } else {
                log.warn("No matches found in {} response. Response keys: {}", source, rootNode.fieldNames());
            }
        } catch (Exception e) {
            log.error("Error parsing {} response: {}", source, e.getMessage());
        }
    }

    /**
     * Sync fixtures for a competition
     */
    private void syncFixtures(String competitionId) {
        try {
            log.info("Fetching fixtures for competition: {}", competitionId);
            String fixturesJson = apiService.getFixtures();
            JsonNode rootNode = objectMapper.readTree(fixturesJson);
            
            // Log API response for debugging
            log.info("Fixtures API response keys: {}", rootNode.fieldNames());
            log.debug("Full fixtures response: {}", rootNode.toPrettyString());
            
            if (rootNode.has("data") && rootNode.get("data").has("fixtures")) {
                JsonNode fixtures = rootNode.get("data").get("fixtures");
                
                if (fixtures.isArray()) {
                    log.info("Found {} fixtures", fixtures.size());
                    for (JsonNode matchNode : fixtures) {
                        syncMatchFromApi(matchNode, false);
                    }
                    log.info("Fixtures sync completed");
                } else {
                    log.warn("Fixtures node is not an array");
                }
            } else {
                log.warn("No 'data.fixtures' found in API response");
            }
        } catch (Exception e) {
            log.error("Error syncing fixtures: {}", e.getMessage(), e);
        }
    }

    /**
     * Sync standings for a competition
     */
    private void syncStandings(String competitionId) {
        try {
            log.info("Fetching standings for competition: {}", competitionId);
            String standingsJson = apiService.getStandings(competitionId);
            JsonNode rootNode = objectMapper.readTree(standingsJson);
            
            // Log API response for debugging
            log.info("Standings API response keys: {}", rootNode.fieldNames());
            log.debug("Full standings response: {}", rootNode.toPrettyString());
            
            if (rootNode.has("data") && rootNode.get("data").has("standings")) {
                JsonNode standings = rootNode.get("data").get("standings");
                
                if (standings.isArray()) {
                    log.info("Found {} standings", standings.size());
                    for (JsonNode standingNode : standings) {
                        syncStandingFromApi(standingNode);
                    }
                    log.info("Standings sync completed");
                } else {
                    log.warn("Standings node is not an array");
                }
            } else {
                log.warn("No 'data.standings' found in API response");
            }
        } catch (Exception e) {
            log.error("Error syncing standings: {}", e.getMessage(), e);
        }
    }

    /**
     * Sync a single match from API data
     */
    private void syncMatchFromApi(JsonNode matchNode, boolean isLive) {
        try {
            // Log the entire match node for debugging
            log.debug("Processing match node: {}", matchNode.toString());
            
            // Filter: only accept World Cup 2026 matches (competition_id = 362)
            if (!isLive && matchNode.has("competition_id")) {
                String compId = matchNode.get("competition_id").asText();
                if (!"362".equals(compId)) {
                    log.debug("Skipping non-World Cup match (competition_id: {})", compId);
                    return;
                }
            } else if (!isLive && matchNode.has("competition") && matchNode.get("competition").has("id")) {
                String compId = matchNode.get("competition").get("id").asText();
                if (!"362".equals(compId)) {
                    log.debug("Skipping non-World Cup match (competition.id: {})", compId);
                    return;
                }
            }

            // Extract match data
            String apiMatchId = matchNode.has("id") ? matchNode.get("id").asText() : null;
            if (apiMatchId == null) {
                log.warn("Skipping match without ID");
                return;
            }
            
            // Get home team from nested object
            if (!matchNode.has("home") || !matchNode.get("home").has("name")) {
                log.warn("Skipping match {}: home team not found", apiMatchId);
                return;
            }
            JsonNode homeNode = matchNode.get("home");
            String homeName = homeNode.get("name").asText();
            String homeId = homeNode.has("id") ? homeNode.get("id").asText() : null;
            
            // Get away team from nested object
            if (!matchNode.has("away") || !matchNode.get("away").has("name")) {
                log.warn("Skipping match {}: away team not found", apiMatchId);
                return;
            }
            JsonNode awayNode = matchNode.get("away");
            String awayName = awayNode.get("name").asText();
            String awayId = awayNode.has("id") ? awayNode.get("id").asText() : null;
            
            // Get or create teams
            Equipe homeTeam = getOrCreateTeam(homeName, homeId);
            Equipe awayTeam = getOrCreateTeam(awayName, awayId);
            
            // Safety check: prevent same team playing against itself
            if (homeTeam.getId().equals(awayTeam.getId())) {
                log.warn("Skipping match {}: same team for home and away ({})", apiMatchId, homeTeam.getNom());
                return;
            }
            
            // Get or create stadium
            String stadiumName = matchNode.has("location") ? matchNode.get("location").asText() : "Unknown Stadium";
            Stade stadium = getOrCreateStadium(stadiumName);
            
            // Get or create phase
            String phaseName = matchNode.has("stage") ? matchNode.get("stage").asText() : "PHASE_GROUPES";
            PhaseCompetition phase = getOrCreatePhase(phaseName);
            
            // Parse match date
            LocalDateTime matchDate = LocalDateTime.now();
            if (matchNode.has("date") && matchNode.has("time")) {
                try {
                    String dateTimeStr = matchNode.get("date").asText() + " " + matchNode.get("time").asText();
                    matchDate = LocalDateTime.parse(dateTimeStr, API_DATE_FORMATTER);
                } catch (Exception e) {
                    log.warn("Could not parse match date: {}", e.getMessage());
                }
            }
            
            // Parse scores from "score" field like "0 - 0"
            Integer homeScore = null;
            Integer awayScore = null;
            if (matchNode.has("scores") && matchNode.get("scores").has("score")) {
                String scoreStr = matchNode.get("scores").get("score").asText();
                if (scoreStr != null && !scoreStr.equals("? - ?")) {
                    String[] parts = scoreStr.split(" - ");
                    if (parts.length == 2) {
                        try {
                            homeScore = Integer.parseInt(parts[0].trim());
                            awayScore = Integer.parseInt(parts[1].trim());
                        } catch (NumberFormatException e) {
                            log.warn("Could not parse score: {}", scoreStr);
                        }
                    }
                }
            }
            
            // Determine status from API status field
            StatutMatchEnum status = StatutMatchEnum.A_VENIR;
            if (matchNode.has("status")) {
                String apiStatus = matchNode.get("status").asText();
                if ("IN PLAY".equalsIgnoreCase(apiStatus)) {
                    status = StatutMatchEnum.EN_COURS;
                } else if ("FINISHED".equalsIgnoreCase(apiStatus) || "ENDED".equalsIgnoreCase(apiStatus)) {
                    status = StatutMatchEnum.TERMINE;
                } else if ("NOT STARTED".equalsIgnoreCase(apiStatus)) {
                    status = StatutMatchEnum.A_VENIR;
                }
            }
            
            // Create final variables for lambda expression
            final Equipe finalHomeTeam = homeTeam;
            final Equipe finalAwayTeam = awayTeam;
            final LocalDateTime finalMatchDate = matchDate;
            
            // Check if match already exists (check both team orderings to avoid duplicates)
            Match match = matchRepository.findAll().stream()
                    .filter(m -> 
                        (m.getEquipe1().equals(finalHomeTeam) && 
                         m.getEquipe2().equals(finalAwayTeam) &&
                         m.getDateHeure().toLocalDate().equals(finalMatchDate.toLocalDate()))
                        ||
                        (m.getEquipe1().equals(finalAwayTeam) && 
                         m.getEquipe2().equals(finalHomeTeam) &&
                         m.getDateHeure().toLocalDate().equals(finalMatchDate.toLocalDate()))
                    )
                    .findFirst()
                    .orElse(null);
            
            if (match == null) {
                // Create new match
                match = Match.builder()
                        .phase(phase)
                        .equipe1(homeTeam)
                        .equipe2(awayTeam)
                        .stade(stadium)
                        .dateHeure(matchDate)
                        .scoreEquipe1(homeScore)
                        .scoreEquipe2(awayScore)
                        .statut(status)
                        .build();
                matchRepository.save(match);
                log.info("Created new match: {} vs {}", homeTeam.getNom(), awayTeam.getNom());
            } else {
                // Update existing match
                match.setScoreEquipe1(homeScore);
                match.setScoreEquipe2(awayScore);
                match.setStatut(status);
                matchRepository.save(match);
                log.info("Updated match: {} vs {}", homeTeam.getNom(), awayTeam.getNom());
            }
            
        } catch (NullPointerException e) {
            log.error("NullPointerException while syncing match. Match data: {}", matchNode.toString());
            log.error("Error details: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error syncing match: {}", e.getMessage());
        }
    }

    /**
     * Sync standing from API data
     */
    private void syncStandingFromApi(JsonNode standingNode) {
        try {
            String teamName = standingNode.has("name") ? standingNode.get("name").asText() : null;
            if (teamName == null) return;
            
            Equipe team = getOrCreateTeam(teamName, null);
            
            // Get or create group
            String groupName = standingNode.has("group") ? standingNode.get("group").asText() : "Groupe A";
            Groupe groupe = getOrCreateGroup(groupName);
            
            // Extract standing data
            int played = standingNode.has("played") ? standingNode.get("played").asInt() : 0;
            int won = standingNode.has("won") ? standingNode.get("won").asInt() : 0;
            int drawn = standingNode.has("drawn") ? standingNode.get("drawn").asInt() : 0;
            int lost = standingNode.has("lost") ? standingNode.get("lost").asInt() : 0;
            int goalsFor = standingNode.has("goals_for") ? standingNode.get("goals_for").asInt() : 0;
            int goalsAgainst = standingNode.has("goals_against") ? standingNode.get("goals_against").asInt() : 0;
            int points = standingNode.has("points") ? standingNode.get("points").asInt() : 0;
            int position = standingNode.has("position") ? standingNode.get("position").asInt() : 1;
            
            // Check if standing already exists
            ClassementGroupe classement = classementRepository.findByGroupeNomAndEquipeId(groupe.getNom(), team.getId())
                    .orElse(null);
            
            if (classement == null) {
                classement = ClassementGroupe.builder()
                        .groupe(groupe)
                        .equipe(team)
                        .matchsJoues(played)
                        .victoires(won)
                        .nuls(drawn)
                        .defaites(lost)
                        .butsPour(goalsFor)
                        .butsContre(goalsAgainst)
                        .differenceButs(goalsFor - goalsAgainst)
                        .points(points)
                        .position(position)
                        .build();
                classementRepository.save(classement);
                log.info("Created standing for: {}", teamName);
            } else {
                classement.setMatchsJoues(played);
                classement.setVictoires(won);
                classement.setNuls(drawn);
                classement.setDefaites(lost);
                classement.setButsPour(goalsFor);
                classement.setButsContre(goalsAgainst);
                classement.setDifferenceButs(goalsFor - goalsAgainst);
                classement.setPoints(points);
                classement.setPosition(position);
                classementRepository.save(classement);
                log.info("Updated standing for: {}", teamName);
            }
            
        } catch (Exception e) {
            log.error("Error syncing standing: {}", e.getMessage(), e);
        }
    }

    /**
     * Get or create team by name
     */
    private Equipe getOrCreateTeam(String teamName, String apiTeamId) {
        // First try to find by API ID if provided
        if (apiTeamId != null && !apiTeamId.isEmpty()) {
            Optional<Equipe> existingTeam = equipeRepository.findAll().stream()
                    .filter(e -> apiTeamId.equals(e.getCodePays()) || e.getNom().equalsIgnoreCase(teamName))
                    .findFirst();
            if (existingTeam.isPresent()) {
                return existingTeam.get();
            }
        }
        
        // Fallback: find by name only
        return equipeRepository.findAll().stream()
                .filter(e -> e.getNom().equalsIgnoreCase(teamName))
                .findFirst()
                .orElseGet(() -> {
                    Equipe newTeam = Equipe.builder()
                            .nom(teamName)
                            .codePays(teamName.substring(0, Math.min(3, teamName.length())).toUpperCase())
                            .drapeauUrl("🏳️")
                            .build();
                    return equipeRepository.save(newTeam);
                });
    }

    /**
     * Get or create stadium
     */
    private Stade getOrCreateStadium(String stadiumName) {
        return stadeRepository.findAll().stream()
                .filter(s -> s.getNom().equalsIgnoreCase(stadiumName))
                .findFirst()
                .orElseGet(() -> {
                    Stade newStadium = Stade.builder()
                            .nom(stadiumName)
                            .ville("Unknown")
                            .pays("Unknown")
                            .capacite(0)
                            .build();
                    return stadeRepository.save(newStadium);
                });
    }

    /**
     * Get or create phase
     */
    private PhaseCompetition getOrCreatePhase(String phaseName) {
        PhaseNomEnum phaseEnum;
        try {
            phaseEnum = PhaseNomEnum.valueOf(phaseName.toUpperCase().replace(" ", "_"));
        } catch (Exception e) {
            phaseEnum = PhaseNomEnum.PHASE_GROUPES;
        }
        
        PhaseNomEnum finalPhaseEnum = phaseEnum;
        return phaseRepository.findAll().stream()
                .filter(p -> p.getNom() == finalPhaseEnum)
                .findFirst()
                .orElseGet(() -> {
                    PhaseCompetition newPhase = PhaseCompetition.builder()
                            .nom(finalPhaseEnum)
                            .ordre(1)
                            .description(phaseName)
                            .nombreMatchs(0)
                            .build();
                    return phaseRepository.save(newPhase);
                });
    }

    /**
     * Get or create group
     */
    private Groupe getOrCreateGroup(String groupName) {
        return groupeRepository.findAll().stream()
                .filter(g -> g.getNom().equalsIgnoreCase(groupName))
                .findFirst()
                .orElseGet(() -> {
                    String letter = groupName.replaceAll("[^A-Z]", "");
                    if (letter.isEmpty()) letter = "A";
                    
                    Groupe newGroup = Groupe.builder()
                            .lettre(letter)
                            .nom(groupName)
                            .build();
                    return groupeRepository.save(newGroup);
                });
    }

    /**
     * Manual trigger for full data sync
     */
    public void forceSyncAll() {
        log.info("Manual full sync triggered");
        syncCompetitionsAndMatches();
    }
}
