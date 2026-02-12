package com.worldcup.tracker.service.impl;

import com.worldcup.tracker.dto.MatchDTO;
import com.worldcup.tracker.dto.MatchEventDTO;
import com.worldcup.tracker.dto.TeamDTO;
import com.worldcup.tracker.model.EvenementMatch;
import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;
import com.worldcup.tracker.service.MatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final PhaseCompetitionRepository phaseRepository; // Might be useful later

    public MatchServiceImpl(MatchRepository matchRepository, PhaseCompetitionRepository phaseRepository) {
        this.matchRepository = matchRepository;
        this.phaseRepository = phaseRepository;
    }

    @Override
    public Match getEntityById(Long id) {

        return matchRepository.findById(id).orElse(null);
    }

    @Override
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Override
    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .filter(m -> m.getPhase() != null &&
                        m.getEquipe1() != null &&
                        m.getEquipe2() != null &&
                        m.getStade() != null)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> getAllOrderedByKickoff() {
        return matchRepository.findAllByOrderByDateHeureAsc();
    }

    @Override
    public List<Match> getByPhase(Long phaseId) {
        return List.of();
    }

    @Override
    public List<MatchDTO> getMatchesByPhase(String phaseName) {
        // This is a bit tricky because the PhaseNomEnum might not match the string exactly
        // For simplicity, we might iterate or try to match.
        // In a real app we'd map the string to the Enum or use ID.
        // Assuming we pass the ID or exact name for now, or fetch all and filter.
        // Ideally we should use findByPhaseId if we have the ID.

        // Let's rely on retrieving all matches and filtering or better, rely on a custom query if needed.
        // But wait, the frontend sends a string ID like "Phase de groupes".
        // We should map that string to our PhaseNomEnum if possible or simply filter.

        // For this implementation, I will fetch all and map to DTO, filtering by phase name comparison if needed,
        // OR better: Assume the frontend passes the phase name as string matching our PhaseCompetition description or name.

        // Actually, let's just return all matches mapped to DTOs for now if filter isn't strict,
        // OR implement a lookup.

        // Let's implement a filter based on the phase Name/Description.
        return matchRepository.findAllByOrderByDateHeureAsc().stream()
                .filter(m -> m.getPhase().getNom().name().equalsIgnoreCase(phaseName) ||
                             m.getPhase().getDescription().contains(phaseName) ||
                             phaseName.equals(String.valueOf(m.getPhase().getId()))) // Fallback to ID check if string is ID
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> getByGroupe(String groupe) {
        return matchRepository.findByGroupeOrderByDateHeureAsc(groupe);
    }

    @Override
    public List<Match> getByStatut(StatutMatchEnum statut) {
        return matchRepository.findByStatutOrderByDateHeureAsc(statut);
    }

    @Override
    public List<MatchDTO> getLiveMatches() {
        return matchRepository.findByStatutOrderByDateHeureAsc(StatutMatchEnum.EN_COURS).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> getByEquipe(Long equipeId) {
        return matchRepository.findByEquipe1IdOrEquipe2Id(equipeId, equipeId);
    }

    @Override
    public List<Match> getBetween(LocalDateTime start, LocalDateTime end) {
        return matchRepository.findByDateHeureBetweenOrderByDateHeureAsc(start, end);
    }

    @Override
    public Match save(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match updateScoreAndStatus(Long matchId, Integer score1, Integer score2, StatutMatchEnum statut) {
        Match match = getEntityById(matchId);
        if (match != null) {
            match.setScoreEquipe1(score1);
            match.setScoreEquipe2(score2);
            match.setStatut(statut);
            return matchRepository.save(match);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        matchRepository.deleteById(id);
    }

    @Override
    public MatchDTO getMatchById(Long id) {
        Match match = getEntityById(id);
        return match != null ? mapToDTO(match) : null;
    }

    private MatchDTO mapToDTO(Match match) {
        return MatchDTO.builder()
                .id(match.getId())
                .phase(match.getPhase().getNom().name()) // Or description
                .teamA(TeamDTO.builder()
                        .id(match.getEquipe1().getId())
                        .name(match.getEquipe1().getNom())
                        .flag(match.getEquipe1().getDrapeauUrl())
                        .code(match.getEquipe1().getCodePays())
                        .score(match.getScoreEquipe1())
                        .build())
                .teamB(TeamDTO.builder()
                        .id(match.getEquipe2().getId())
                        .name(match.getEquipe2().getNom())
                        .flag(match.getEquipe2().getDrapeauUrl())
                        .code(match.getEquipe2().getCodePays())
                        .score(match.getScoreEquipe2())
                        .build())
                .date(match.getDateHeure().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                .time(match.getDateHeure().format(DateTimeFormatter.ofPattern("HH:mm")))
                .stadium(match.getStade().getNom())
                .city(match.getStade().getVille())
                .status(mapStatus(match.getStatut()))
                .currentMinute(calculateCurrentMinute(match)) // Logic to calculate minute
                .events(match.getEvenements().stream().map(this::mapEventToDTO).collect(Collectors.toList()))
                .build();
    }

    private MatchEventDTO mapEventToDTO(EvenementMatch event) {
        return MatchEventDTO.builder()
                .minute(event.getMinute())
                .type(event.getTypeEvenement())
                .player(event.getJoueurNom())
                .description(event.getDescription())
                .team(determineTeam(event))
                .build();
    }

    private String determineTeam(EvenementMatch event) {
        // Determine if event belongs to team A or B based on match teams
        if (event.getEquipe() == null) return null;
        if (event.getEquipe().getId().equals(event.getMatch().getEquipe1().getId())) {
            return "A";
        } else if (event.getEquipe().getId().equals(event.getMatch().getEquipe2().getId())) {
            return "B";
        }
        return null;
    }

    private String mapStatus(StatutMatchEnum status) {
        switch (status) {
            case EN_COURS: return "live";
            case TERMINE: return "finished";
            case A_VENIR:
            default: return "upcoming";
        }
    }

    private Integer calculateCurrentMinute(Match match) {
        // Simplistic logic, normally would calculate diff from now to start time if live
        if (match.getStatut() == StatutMatchEnum.EN_COURS) {
            // This is just a placeholder. In real app, we diff LocalDateTime.now() with match.getDateHeure()
            // considering half-time breaks etc.
            long minutes = java.time.Duration.between(match.getDateHeure(), LocalDateTime.now()).toMinutes();
            return (int) minutes;
        }
        return null;
    }
}

