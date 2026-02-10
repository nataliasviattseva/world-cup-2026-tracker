package com.worldcup.tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.worldcup.tracker.model.*;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for MatchService implementation.
 * 
 * This test class demonstrates best practices for unit testing Spring services:
 * - Using Mockito for dependency mocking
 * - Testing business logic in isolation
 * - Comprehensive coverage of success and error scenarios
 * - Proper validation testing
 * - Clear test naming and documentation
 * 
 * Unit testing vs Integration testing:
 * - Unit tests: Mock all dependencies, test business logic in isolation
 * - Integration tests: Use real Spring context and database
 * 
 * Key patterns demonstrated:
 * - Arrange-Act-Assert structure
 * - Mock verification for side effects
 * - Exception testing with assertThatThrownBy
 * - Edge case validation
 * - Null and invalid input handling
 * 
 * @author World Cup Tracker Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MatchService Unit Tests")
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    
    @Mock
    private PhaseCompetitionRepository phaseRepository;
    
    @InjectMocks
    private MatchServiceImpl matchService;
    
    private Match testMatch;
    private Equipe equipe1;
    private Equipe equipe2;
    private PhaseCompetition phase;
    private Stade stade;
    
    @BeforeEach
    void setUp() {
        // Create test entities using builders
        equipe1 = Equipe.builder()
            .id(1L)
            .nom("Brazil")
            .codePays("BRA")
            .build();
        
        equipe2 = Equipe.builder()
            .id(2L)
            .nom("Germany")  
            .codePays("GER")
            .build();
        
        phase = PhaseCompetition.builder()
            .id(1L)
            .nom(PhaseNomEnum.PHASE_GROUPES)
            .ordre(1)
            .build();
        
        stade = Stade.builder()
            .id(1L)
            .nom("Stadium 974")
            .ville("Doha")
            .build();
        
        testMatch = Match.builder()
            .id(1L)
            .equipe1(equipe1)
            .equipe2(equipe2)
            .phase(phase)
            .stade(stade)
            .dateHeure(LocalDateTime.of(2026, 6, 15, 18, 0))
            .statut(StatutMatchEnum.A_VENIR)
            .groupe("A")
            .scoreEquipe1(0)
            .scoreEquipe2(0)
            .build();
    }
    
    @Test
    @DisplayName("Should get match by ID successfully")
    void shouldGetMatchByIdSuccessfully() {
        // Arrange
        Long matchId = 1L;
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(testMatch));
        
        // Act
        Match result = matchService.getEntityById(matchId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(matchId);
        assertThat(result.getEquipe1().getNom()).isEqualTo("Brazil");
        assertThat(result.getEquipe2().getNom()).isEqualTo("Germany");
        verify(matchRepository).findById(matchId);
    }
    
    @Test
    @DisplayName("Should throw exception when match not found by ID")
    void shouldThrowExceptionWhenMatchNotFoundById() {
        // Arrange
        Long nonExistentId = 999L;
        when(matchRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> matchService.getEntityById(nonExistentId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Match not found: " + nonExistentId);
        
        verify(matchRepository).findById(nonExistentId);
    }
    
    @Test
    @DisplayName("Should get all matches")
    void shouldGetAllMatches() {
        // Arrange
        List<Match> expectedMatches = List.of(testMatch);
        when(matchRepository.findAll()).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getAll();
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findAll();
    }
    
    @Test
    @DisplayName("Should get all matches ordered by kickoff time")
    void shouldGetAllMatchesOrderedByKickoff() {
        // Arrange
        List<Match> expectedMatches = List.of(testMatch);
        when(matchRepository.findAllByOrderByDateHeureAsc()).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getAllOrderedByKickoff();
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findAllByOrderByDateHeureAsc();
    }
    
    @Test
    @DisplayName("Should get matches by phase ID successfully")
    void shouldGetMatchesByPhaseIdSuccessfully() {
        // Arrange
        Long phaseId = 1L;
        List<Match> expectedMatches = List.of(testMatch);
        when(phaseRepository.existsById(phaseId)).thenReturn(true);
        when(matchRepository.findByPhaseIdOrderByDateHeureAsc(phaseId)).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getByPhase(phaseId);
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(phaseRepository).existsById(phaseId);
        verify(matchRepository).findByPhaseIdOrderByDateHeureAsc(phaseId);
    }
    
    @Test
    @DisplayName("Should throw exception when phase not found")
    void shouldThrowExceptionWhenPhaseNotFound() {
        // Arrange
        Long nonExistentPhaseId = 999L;
        when(phaseRepository.existsById(nonExistentPhaseId)).thenReturn(false);
        
        // Act & Assert
        assertThatThrownBy(() -> matchService.getByPhase(nonExistentPhaseId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Phase not found: " + nonExistentPhaseId);
        
        verify(phaseRepository).existsById(nonExistentPhaseId);
        verify(matchRepository, never()).findByPhaseIdOrderByDateHeureAsc(anyLong());
    }
    
    @Test
    @DisplayName("Should get matches by phase name")
    void shouldGetMatchesByPhaseName() {
        // Arrange
        String phaseName = "PHASE_GROUPES";
        List<Match> expectedMatches = List.of(testMatch);
        when(matchRepository.findByPhaseNomOrderByDateHeureAsc(phaseName)).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getMatchesByPhase(phaseName);
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findByPhaseNomOrderByDateHeureAsc(phaseName);
    }
    
    @Test
    @DisplayName("Should return empty list for null or blank phase name")
    void shouldReturnEmptyListForNullOrBlankPhaseName() {
        // Act & Assert
        assertThat(matchService.getMatchesByPhase(null)).isEmpty();
        assertThat(matchService.getMatchesByPhase("")).isEmpty();
        assertThat(matchService.getMatchesByPhase("   ")).isEmpty();
        
        verify(matchRepository, never()).findByPhaseNomOrderByDateHeureAsc(anyString());
    }
    
    @Test
    @DisplayName("Should get matches by group")
    void shouldGetMatchesByGroup() {
        // Arrange
        String groupe = "A";
        List<Match> expectedMatches = List.of(testMatch);
        when(matchRepository.findByGroupeOrderByDateHeureAsc(groupe)).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getByGroupe(groupe);
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findByGroupeOrderByDateHeureAsc(groupe);
    }
    
    @Test
    @DisplayName("Should return empty list for null or blank group")
    void shouldReturnEmptyListForNullOrBlankGroup() {
        // Act & Assert
        assertThat(matchService.getByGroupe(null)).isEmpty();
        assertThat(matchService.getByGroupe("")).isEmpty();
        assertThat(matchService.getByGroupe("   ")).isEmpty();
        
        verify(matchRepository, never()).findByGroupeOrderByDateHeureAsc(anyString());
    }
    
    @Test
    @DisplayName("Should get matches by status")
    void shouldGetMatchesByStatus() {
        // Arrange
        StatutMatchEnum statut = StatutMatchEnum.A_VENIR;
        List<Match> expectedMatches = List.of(testMatch);
        when(matchRepository.findByStatutOrderByDateHeureAsc(statut)).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getByStatut(statut);
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findByStatutOrderByDateHeureAsc(statut);
    }
    
    @Test
    @DisplayName("Should return empty list for null status")
    void shouldReturnEmptyListForNullStatus() {
        // Act
        List<Match> result = matchService.getByStatut(null);
        
        // Assert
        assertThat(result).isEmpty();
        verify(matchRepository, never()).findByStatutOrderByDateHeureAsc(any());
    }
    
    @Test
    @DisplayName("Should get live matches")
    void shouldGetLiveMatches() {
        // Arrange
        List<Match> liveMatches = List.of(testMatch);
        when(matchRepository.findByStatutOrderByDateHeureAsc(StatutMatchEnum.EN_COURS)).thenReturn(liveMatches);
        
        // Act
        List<Match> result = matchService.getLiveMatches();
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findByStatutOrderByDateHeureAsc(StatutMatchEnum.EN_COURS);
    }
    
    @Test
    @DisplayName("Should get matches by team")
    void shouldGetMatchesByTeam() {
        // Arrange
        Long equipeId = 1L;
        List<Match> expectedMatches = List.of(testMatch);
        when(matchRepository.findByEquipe1IdOrEquipe2Id(equipeId, equipeId)).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getByEquipe(equipeId);
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findByEquipe1IdOrEquipe2Id(equipeId, equipeId);
    }
    
    @Test
    @DisplayName("Should return empty list for null team ID")
    void shouldReturnEmptyListForNullTeamId() {
        // Act
        List<Match> result = matchService.getByEquipe(null);
        
        // Assert
        assertThat(result).isEmpty();
        verify(matchRepository, never()).findByEquipe1IdOrEquipe2Id(any(), any());
    }
    
    @Test
    @DisplayName("Should get matches between dates")
    void shouldGetMatchesBetweenDates() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 30, 23, 59);
        List<Match> expectedMatches = List.of(testMatch);
        when(matchRepository.findByDateHeureBetweenOrderByDateHeureAsc(start, end)).thenReturn(expectedMatches);
        
        // Act
        List<Match> result = matchService.getBetween(start, end);
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testMatch);
        verify(matchRepository).findByDateHeureBetweenOrderByDateHeureAsc(start, end);
    }
    
    @Test
    @DisplayName("Should return empty list for invalid date range")
    void shouldReturnEmptyListForInvalidDateRange() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2026, 6, 30, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 1, 0, 0); // end before start
        
        // Act & Assert
        assertThat(matchService.getBetween(start, end)).isEmpty();
        assertThat(matchService.getBetween(null, end)).isEmpty();
        assertThat(matchService.getBetween(start, null)).isEmpty();
        
        verify(matchRepository, never()).findByDateHeureBetweenOrderByDateHeureAsc(any(), any());
    }
    
    @Test
    @DisplayName("Should save match successfully")
    void shouldSaveMatchSuccessfully() {
        // Arrange
        when(matchRepository.save(testMatch)).thenReturn(testMatch);
        
        // Act
        Match result = matchService.save(testMatch);
        
        // Assert
        assertThat(result).isEqualTo(testMatch);
        verify(matchRepository).save(testMatch);
    }
    
    @Test
    @DisplayName("Should throw exception when saving null match")
    void shouldThrowExceptionWhenSavingNullMatch() {
        // Act & Assert
        assertThatThrownBy(() -> matchService.save(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("match must not be null");
        
        verify(matchRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should throw exception when match missing teams")
    void shouldThrowExceptionWhenMatchMissingTeams() {
        // Arrange
        Match matchWithoutTeam1 = testMatch.toBuilder().equipe1(null).build();
        Match matchWithoutTeam2 = testMatch.toBuilder().equipe2(null).build();
        
        // Act & Assert
        assertThatThrownBy(() -> matchService.save(matchWithoutTeam1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Match must have equipe1 and equipe2");
        
        assertThatThrownBy(() -> matchService.save(matchWithoutTeam2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Match must have equipe1 and equipe2");
        
        verify(matchRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should throw exception when match has same teams")
    void shouldThrowExceptionWhenMatchHasSameTeams() {
        // Arrange
        Match matchWithSameTeams = testMatch.toBuilder().equipe2(equipe1).build();
        
        // Act & Assert
        assertThatThrownBy(() -> matchService.save(matchWithSameTeams))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("equipe1 and equipe2 must be different");
        
        verify(matchRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should throw exception when match missing required fields")
    void shouldThrowExceptionWhenMatchMissingRequiredFields() {
        // Arrange
        Match matchWithoutPhase = testMatch.toBuilder().phase(null).build();
        Match matchWithoutStade = testMatch.toBuilder().stade(null).build();
        Match matchWithoutDate = testMatch.toBuilder().dateHeure(null).build();
        Match matchWithoutStatus = testMatch.toBuilder().statut(null).build();
        
        // Act & Assert
        assertThatThrownBy(() -> matchService.save(matchWithoutPhase))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Match must have a phase");
        
        assertThatThrownBy(() -> matchService.save(matchWithoutStade))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Match must have a stade");
        
        assertThatThrownBy(() -> matchService.save(matchWithoutDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("dateHeure is required");
        
        assertThatThrownBy(() -> matchService.save(matchWithoutStatus))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("statut is required");
        
        verify(matchRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should update match score and status")
    void shouldUpdateMatchScoreAndStatus() {
        // Arrange
        Long matchId = 1L;
        Integer newScore1 = 2;
        Integer newScore2 = 1;
        StatutMatchEnum newStatus = StatutMatchEnum.TERMINE;
        
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(testMatch));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Match result = matchService.updateScoreAndStatus(matchId, newScore1, newScore2, newStatus);
        
        // Assert
        assertThat(result.getScoreEquipe1()).isEqualTo(newScore1);
        assertThat(result.getScoreEquipe2()).isEqualTo(newScore2);
        assertThat(result.getStatut()).isEqualTo(newStatus);
        verify(matchRepository).findById(matchId);
        verify(matchRepository).save(any(Match.class));
    }
    
    @Test
    @DisplayName("Should update match score without changing status")
    void shouldUpdateMatchScoreWithoutChangingStatus() {
        // Arrange
        Long matchId = 1L;
        Integer newScore1 = 1;
        Integer newScore2 = 0;
        StatutMatchEnum originalStatus = testMatch.getStatut();
        
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(testMatch));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Match result = matchService.updateScoreAndStatus(matchId, newScore1, newScore2, null);
        
        // Assert
        assertThat(result.getScoreEquipe1()).isEqualTo(newScore1);
        assertThat(result.getScoreEquipe2()).isEqualTo(newScore2);
        assertThat(result.getStatut()).isEqualTo(originalStatus); // Status unchanged
        verify(matchRepository).findById(matchId);
        verify(matchRepository).save(any(Match.class));
    }
    
    @Test
    @DisplayName("Should throw exception when updating non-existing match")
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        // Arrange
        Long nonExistentId = 999L;
        when(matchRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> matchService.updateScoreAndStatus(nonExistentId, 1, 0, StatutMatchEnum.TERMINE))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Match not found: " + nonExistentId);
        
        verify(matchRepository).findById(nonExistentId);
        verify(matchRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should delete match successfully")
    void shouldDeleteMatchSuccessfully() {
        // Arrange
        Long matchId = 1L;
        when(matchRepository.existsById(matchId)).thenReturn(true);
        
        // Act
        matchService.delete(matchId);
        
        // Assert
        verify(matchRepository).existsById(matchId);
        verify(matchRepository).deleteById(matchId);
    }
    
    @Test
    @DisplayName("Should throw exception when deleting non-existing match")
    void shouldThrowExceptionWhenDeletingNonExistingMatch() {
        // Arrange
        Long nonExistentId = 999L;
        when(matchRepository.existsById(nonExistentId)).thenReturn(false);
        
        // Act & Assert
        assertThatThrownBy(() -> matchService.delete(nonExistentId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Match not found: " + nonExistentId);
        
        verify(matchRepository).existsById(nonExistentId);
        verify(matchRepository, never()).deleteById(anyLong());
    }
}