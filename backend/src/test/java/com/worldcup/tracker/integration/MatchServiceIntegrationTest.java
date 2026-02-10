package com.worldcup.tracker.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import com.worldcup.tracker.dto.MatchDTO;
import com.worldcup.tracker.model.*;
import com.worldcup.tracker.service.IMatchService;
import com.worldcup.tracker.repository.*;

import jakarta.persistence.EntityNotFoundException;

/**
 * Integration tests for the MatchService class.
 * 
 * This test verifies the integration of:
 * - MatchService business logic
 * - JPA repository operations
 * - Database constraints and relationships
 * - Error handling with proper exceptions
 * 
 * Test scenarios include:
 * - Creating matches with complete data
 * - Updating match scores and status
 * - Retrieving matches by various criteria (phase, team, status)
 * - Live match functionality
 * - Validation of business rules
 * - Error handling for edge cases
 * 
 * @author World Cup Tracker Team
 * @version 1.0
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MatchServiceIntegrationTest {
    
    @Autowired
    private IMatchService matchService;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private EquipeRepository equipeRepository;
    
    @Autowired
    private PhaseCompetitionRepository phaseRepository;
    
    @Autowired
    private GroupeRepository groupeRepository;
    
    @Autowired
    private StadeRepository stadeRepository;
    
    private Equipe equipe1;
    private Equipe equipe2;
    private PhaseCompetition phase;
    private Groupe groupe;
    private Stade stade;
    private Match match;
    
    @BeforeEach
    void setupBefore() {
        // Create test group
        groupe = Groupe.builder()
            .lettre("A")
            .nom("Group A")
            .build();
        groupe = groupeRepository.save(groupe);
        
        // Create test phase
        phase = PhaseCompetition.builder()
            .nom(PhaseNomEnum.PHASE_GROUPES)
            .ordre(1)
            .description("Group Stage")
            .nombreMatchs(48)
            .build();
        phase = phaseRepository.save(phase);
        
        // Create test teams
        equipe1 = Equipe.builder()
            .nom("Brazil")
            .codePays("BRA")
            .confederation("CONMEBOL")
            .groupe(groupe)
            .build();
        equipe1 = equipeRepository.save(equipe1);
        
        equipe2 = Equipe.builder()
            .nom("Germany")
            .codePays("GER")
            .confederation("UEFA")
            .groupe(groupe)
            .build();
        equipe2 = equipeRepository.save(equipe2);
        
        // Create test stade
        stade = Stade.builder()
            .nom("Stadium 974")
            .ville("Doha")
            .pays("Qatar")
            .capacite(40000)
            .build();
        stade = stadeRepository.save(stade);
        
        // Create test match
        match = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe2)
            .phase(phase)
            .stade(stade)
            .dateHeure(LocalDateTime.now().plusDays(1))
            .statut(StatutMatchEnum.A_VENIR)
            .groupe("A")
            .scoreEquipe1(0)
            .scoreEquipe2(0)
            .build();
    }
    
    @Test
    @DisplayName("Should save a match in the database")
    void shouldSaveMatchInDatabase() {
        // Given
        // When
        Match savedMatch = matchService.save(match);
        
        // Then
        assertThat(savedMatch).isNotNull();
        assertThat(savedMatch.getId()).isGreaterThan(0);
        assertThat(matchRepository.findById(savedMatch.getId())).isPresent();
        assertThat(savedMatch.getEquipe1().getNom()).isEqualTo("Brazil");
        assertThat(savedMatch.getEquipe2().getNom()).isEqualTo("Germany");
        assertThat(savedMatch.getStatut()).isEqualTo(StatutMatchEnum.A_VENIR);
    }
    
    @Test
    @DisplayName("Should retrieve match by ID")
    void shouldRetrieveMatchById() {
        // Given
        Match savedMatch = matchService.save(match);
        
        // When
        Match retrievedMatch = matchService.getEntityById(savedMatch.getId());
        
        // Then
        assertThat(retrievedMatch).isNotNull();
        assertThat(retrievedMatch.getId()).isEqualTo(savedMatch.getId());
        assertThat(retrievedMatch.getStade().getNom()).isEqualTo("Stadium 974");
        assertThat(retrievedMatch.getStade().getVille()).isEqualTo("Doha");
    }
    
    @Test
    @DisplayName("Should update match score and status")
    void shouldUpdateMatchScoreAndStatus() {
        // Given
        Match savedMatch = matchService.save(match);
        
        // When
        Match updatedMatch = matchService.updateScoreAndStatus(
            savedMatch.getId(), 2, 1, StatutMatchEnum.TERMINE);
        
        // Then
        assertThat(updatedMatch.getScoreEquipe1()).isEqualTo(2);
        assertThat(updatedMatch.getScoreEquipe2()).isEqualTo(1);
        assertThat(updatedMatch.getStatut()).isEqualTo(StatutMatchEnum.TERMINE);
    }
    
    @Test
    @DisplayName("Should retrieve matches by phase")
    void shouldRetrieveMatchesByPhase() {
        // Given
        matchService.save(match);
        
        // Create another match in different phase
        PhaseCompetition knockoutPhase = PhaseCompetition.builder()
            .nom(PhaseNomEnum.HUITIEMES_FINALE)
            .ordre(2)
            .description("Round of 16")
            .nombreMatchs(8)
            .build();
        knockoutPhase = phaseRepository.save(knockoutPhase);
        
        Stade lusailStadium = Stade.builder()
            .nom("Lusail Stadium")
            .ville("Lusail")
            .pays("Qatar")
            .capacite(80000)
            .build();
        lusailStadium = stadeRepository.save(lusailStadium);
        
        Match knockoutMatch = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe2)
            .phase(knockoutPhase)
            .stade(lusailStadium)
            .dateHeure(LocalDateTime.now().plusDays(2))
            .statut(StatutMatchEnum.A_VENIR)
            .scoreEquipe1(0)
            .scoreEquipe2(0)
            .build();
        matchService.save(knockoutMatch);
        
        // When
        List<Match> groupMatches = matchService.getByPhase(phase.getId());
        List<Match> knockoutMatches = matchService.getByPhase(knockoutPhase.getId());
        
        // Then
        assertThat(groupMatches).hasSize(1);
        assertThat(knockoutMatches).hasSize(1);
        assertThat(groupMatches.get(0).getPhase().getNom()).isEqualTo(PhaseNomEnum.PHASE_GROUPES);
        assertThat(knockoutMatches.get(0).getPhase().getNom()).isEqualTo(PhaseNomEnum.HUITIEMES_FINALE);
    }
    
    @Test
    @DisplayName("Should retrieve matches by team")
    void shouldRetrieveMatchesByTeam() {
        // Given
        matchService.save(match);
        
        // Create another team and match
        Equipe equipe3 = Equipe.builder()
            .nom("France")
            .codePays("FRA")
            .confederation("UEFA")
            .groupe(groupe)
            .build();
        equipe3 = equipeRepository.save(equipe3);
        
        Stade alBaytStadium = Stade.builder()
            .nom("Al Bayt Stadium")
            .ville("Al Khor")
            .pays("Qatar")
            .capacite(60000)
            .build();
        alBaytStadium = stadeRepository.save(alBaytStadium);
        
        Match anotherMatch = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe3)
            .phase(phase)
            .stade(alBaytStadium)
            .dateHeure(LocalDateTime.now().plusDays(3))
            .statut(StatutMatchEnum.A_VENIR)
            .groupe("A")
            .scoreEquipe1(0)
            .scoreEquipe2(0)
            .build();
        matchService.save(anotherMatch);
        
        // When
        List<MatchDTO> brazilMatches = matchService.getByEquipe(equipe1.getId());
        List<MatchDTO> germanyMatches = matchService.getByEquipe(equipe2.getId());
        List<MatchDTO> franceMatches = matchService.getByEquipe(equipe3.getId());
        
        // Then
        assertThat(brazilMatches).hasSize(2); // Brazil plays in both matches
        assertThat(germanyMatches).hasSize(1); // Germany plays in one match
        assertThat(franceMatches).hasSize(1); // France plays in one match
    }
    
    @Test
    @DisplayName("Should retrieve live matches")
    void shouldRetrieveLiveMatches() {
        // Given
        match.setStatut(StatutMatchEnum.EN_COURS);
        Match liveMatch = matchService.save(match);
        
        // Create a finished match
        Stade educationCityStadium = Stade.builder()
            .nom("Education City Stadium")
            .ville("Al Rayyan")
            .pays("Qatar")
            .capacite(45000)
            .build();
        educationCityStadium = stadeRepository.save(educationCityStadium);
        
        Match finishedMatch = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe2)
            .phase(phase)
            .stade(educationCityStadium)
            .dateHeure(LocalDateTime.now().minusHours(2))
            .statut(StatutMatchEnum.TERMINE)
            .groupe("A")
            .scoreEquipe1(1)
            .scoreEquipe2(0)
            .build();
        matchService.save(finishedMatch);
        
        // When
        List<MatchDTO> liveMatches = matchService.getLiveMatches();
        
        // Then - account for seed data that may also contain EN_COURS matches
        assertThat(liveMatches).isNotEmpty();
        assertThat(liveMatches).anyMatch(m -> m.getId().equals(liveMatch.getId()));
        assertThat(liveMatches).allMatch(m -> m.getStatut() == MatchDTO.StatutMatchDTO.EN_COURS);
    }
    
    @Test
    @DisplayName("Should retrieve matches by group")
    void shouldRetrieveMatchesByGroup() {
        // Given
        matchService.save(match);
        
        // Create match in different group
        Stade ahmadBinAliStadium = Stade.builder()
            .nom("Ahmad bin Ali Stadium")
            .ville("Al Rayyan")
            .pays("Qatar")
            .capacite(44740)
            .build();
        ahmadBinAliStadium = stadeRepository.save(ahmadBinAliStadium);
        
        Match groupBMatch = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe2)
            .phase(phase)
            .stade(ahmadBinAliStadium)
            .dateHeure(LocalDateTime.now().plusDays(4))
            .statut(StatutMatchEnum.A_VENIR)
            .groupe("B")
            .scoreEquipe1(0)
            .scoreEquipe2(0)
            .build();
        matchService.save(groupBMatch);
        
        // When
        List<MatchDTO> groupAMatches = matchService.getByGroupe("A");
        List<MatchDTO> groupBMatches = matchService.getByGroupe("B");
        
        // Then
        assertThat(groupAMatches).hasSize(1);
        assertThat(groupBMatches).hasSize(1);
        assertThat(groupAMatches.get(0).getGroupe()).isEqualTo("A");
        assertThat(groupBMatches.get(0).getGroupe()).isEqualTo("B");
    }
    
    @Test
    @DisplayName("Should retrieve matches within date range")
    void shouldRetrieveMatchesWithinDateRange() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        
        match.setDateHeure(LocalDateTime.now().plusDays(2));
        matchService.save(match);
        
        // Create match outside range
        Stade alJanoubStadium = Stade.builder()
            .nom("Al Janoub Stadium")
            .ville("Al Wakrah")
            .pays("Qatar")
            .capacite(40000)
            .build();
        alJanoubStadium = stadeRepository.save(alJanoubStadium);
        
        Match outsideMatch = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe2)
            .phase(phase)
            .stade(alJanoubStadium)
            .dateHeure(LocalDateTime.now().plusDays(10))
            .statut(StatutMatchEnum.A_VENIR)
            .groupe("A")
            .scoreEquipe1(0)
            .scoreEquipe2(0)
            .build();
        matchService.save(outsideMatch);
        
        // When
        List<MatchDTO> matchesInRange = matchService.getBetween(startDate, endDate);
        
        // Then
        assertThat(matchesInRange).hasSize(1);
        assertThat(matchesInRange.get(0).getDateHeure()).isBetween(startDate, endDate);
    }
    
    @Test
    @DisplayName("Should not save match with same teams")
    void shouldFailToSaveMatchWithSameTeams() {
        // Given
        Match invalidMatch = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe1) // Same team as opponent
            .phase(phase)
            .stade(stade)
            .dateHeure(LocalDateTime.now().plusDays(1))
            .statut(StatutMatchEnum.A_VENIR)
            .groupe("A")
            .scoreEquipe1(0)
            .scoreEquipe2(0)
            .build();
        
        // When/Then
        assertThatThrownBy(() -> matchService.save(invalidMatch))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("equipe1 and equipe2 must be different");
    }
    
    @Test
    @DisplayName("Should not save match without required fields")
    void shouldFailToSaveMatchWithoutRequiredFields() {
        // Given
        Match incompleteMatch = Match.builder()
            .equipe1(equipe1)
            .equipe2(equipe2)
            // Missing phase, stade, dateHeure, statut
            .build();
        
        // When/Then
        assertThatThrownBy(() -> matchService.save(incompleteMatch))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("Should not retrieve non-existing match")
    void shouldFailToRetrieveNonExistingMatch() {
        // Given
        Long nonExistingId = 999L;
        
        // When/Then
        assertThatThrownBy(() -> matchService.getEntityById(nonExistingId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Match not found: " + nonExistingId);
    }
    
    @Test
    @DisplayName("Should delete match successfully")
    void shouldDeleteMatchSuccessfully() {
        // Given
        Match savedMatch = matchService.save(match);
        Long matchId = savedMatch.getId();
        
        // When
        matchService.delete(matchId);
        
        // Then
        assertThat(matchRepository.findById(matchId)).isEmpty();
        assertThatThrownBy(() -> matchService.getEntityById(matchId))
            .isInstanceOf(EntityNotFoundException.class);
    }
    
    @Test
    @DisplayName("Should not delete non-existing match")
    void shouldFailToDeleteNonExistingMatch() {
        // Given
        Long nonExistingId = 999L;
        
        // When/Then
        assertThatThrownBy(() -> matchService.delete(nonExistingId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Match not found: " + nonExistingId);
    }
}
