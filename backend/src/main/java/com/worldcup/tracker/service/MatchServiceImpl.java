package com.worldcup.tracker.service;

import com.worldcup.tracker.dto.MatchDTO;

import com.worldcup.tracker.dto.DTOMapper;
import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Collections;

@Service
@Transactional
public class MatchServiceImpl implements IMatchService {

    private final MatchRepository matchRepository;
    private final PhaseCompetitionRepository phaseRepository; // Might be useful later
    private final DTOMapper dtoMapper;

    public MatchServiceImpl(MatchRepository matchRepository, PhaseCompetitionRepository phaseRepository,
            DTOMapper dtoMapper) {
        this.matchRepository = matchRepository;
        this.phaseRepository = phaseRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public Match getEntityById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found: " + id));
    }

    @Override
    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(dtoMapper::mapToMatchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> getAllOrderedByKickoff() {
        return matchRepository.findAllByOrderByDateHeureAsc();
    }

    @Override
    public List<MatchDTO> getMatchesByPhase(String phaseName) {
        if (phaseName == null || phaseName.isBlank()) {
            return List.of();
        }
        // This is a bit tricky because the PhaseNomEnum might not match the string
        // exactly
        // For simplicity, we might iterate or try to match.
        // In a real app we'd map the string to the Enum or use ID.
        // Assuming we pass the ID or exact name for now, or fetch all and filter.
        // Ideally we should use findByPhaseId if we have the ID.

        // Let's rely on retrieving all matches and filtering or better, rely on a
        // custom query if needed.
        // But wait, the frontend sends a string ID like "Phase de groupes".
        // We should map that string to our PhaseNomEnum if possible or simply filter.

        // For this implementation, I will fetch all and map to DTO, filtering by phase
        // name comparison if needed,
        // OR better: Assume the frontend passes the phase name as string matching our
        // PhaseCompetition description or name.

        // Actually, let's just return all matches mapped to DTOs for now if filter
        // isn't strict,
        // OR implement a lookup.

        // Let's implement a filter based on the phase Name/Description.
        return matchRepository.findAllByOrderByDateHeureAsc().stream()
                .filter(m -> m.getPhase().getNom().name().equalsIgnoreCase(phaseName) ||
                        m.getPhase().getDescription().contains(phaseName) ||
                        phaseName.equals(String.valueOf(m.getPhase().getId()))) // Fallback to ID check if string is ID
                .map(dtoMapper::mapToMatchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchDTO> getByGroupe(String groupe) {
        if (groupe == null || groupe.isBlank()) {
            return List.of();
        }
        return matchRepository.findByGroupeOrderByDateHeureAsc(groupe).stream()
                .map(dtoMapper::mapToMatchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchDTO> getByStatut(StatutMatchEnum statut) {
        if (statut == null) {
            return List.of();
        }
        return matchRepository.findByStatutOrderByDateHeureAsc(statut).stream()
                .map(dtoMapper::mapToMatchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchDTO> getLiveMatches() {
        return matchRepository.findByStatutOrderByDateHeureAsc(StatutMatchEnum.EN_COURS).stream()
                .map(dtoMapper::mapToMatchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchDTO> getByEquipe(Long equipeId) {
        if (equipeId == null) {
            return List.of();
        }
        return matchRepository.findByEquipe1IdOrEquipe2Id(equipeId, equipeId).stream()
                .map(dtoMapper::mapToMatchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchDTO> getBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            return List.of();
        }
        return matchRepository.findByDateHeureBetweenOrderByDateHeureAsc(start, end).stream()
                .map(dtoMapper::mapToMatchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Match save(Match match) {
        if (match == null)
            throw new IllegalArgumentException("match must not be null");
        if (match.getEquipe1() == null || match.getEquipe2() == null) {
            throw new IllegalArgumentException("Match must have equipe1 and equipe2");
        }
        if (match.getEquipe1().getId().equals(match.getEquipe2().getId())) {
            throw new IllegalArgumentException("equipe1 and equipe2 must be different");
        }
        if (match.getPhase() == null)
            throw new IllegalArgumentException("Match must have a phase");
        if (match.getStade() == null)
            throw new IllegalArgumentException("Match must have a stade");
        if (match.getDateHeure() == null)
            throw new IllegalArgumentException("dateHeure is required");
        if (match.getStatut() == null)
            throw new IllegalArgumentException("statut is required");

        return matchRepository.save(match);
    }

    @Override
    public Match updateScoreAndStatus(Long matchId, Integer score1, Integer score2, StatutMatchEnum statut) {
        Match match = getEntityById(matchId);
        if (match != null) {
            match.setScoreEquipe1(score1);
            match.setScoreEquipe2(score2);
            if (statut != null) {
                match.setStatut(statut);
            }
        }
        return matchRepository.save(match);
    }

    @Override
    public void delete(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new EntityNotFoundException("Match not found: " + id);
        }
        matchRepository.deleteById(id);
    }

    // Additional methods with proper validation

    @Override
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getByPhase(Long phaseId) {
        if (!phaseRepository.existsById(phaseId)) {
            throw new EntityNotFoundException("Phase not found: " + phaseId);
        }
        return matchRepository.findByPhaseIdOrderByDateHeureAsc(phaseId);
    }

    // Enhanced getMatchesByPhase with better validation
    public List<Match> getMatchesByPhaseEntity(String phase) {
        if (phase == null || phase.isBlank())
            return Collections.emptyList();
        return matchRepository.findByPhaseNomOrderByDateHeureAsc(phase.trim());
    }

    @Override
    public MatchDTO getMatchById(Long id) {
        Match match = getEntityById(id);
        return dtoMapper.mapToMatchDTO(match);
    }

    // private MatchDTO mapToDTO(Match match) {
    // return MatchDTO.builder()
    // .id(match.getId())
    // .phase(match.getPhase().getNom().name()) // Or description
    // .teamA(EquipeDTO.builder()
    // .id(match.getEquipe1().getId())
    // .name(match.getEquipe1().getNom())
    // .flag(match.getEquipe1().getDrapeauUrl())
    // .code(match.getEquipe1().getCodePays())
    // .score(match.getScoreEquipe1())
    // .build())
    // .teamB(EquipeDTO.builder()
    // .id(match.getEquipe2().getId())
    // .name(match.getEquipe2().getNom())
    // .flag(match.getEquipe2().getDrapeauUrl())
    // .code(match.getEquipe2().getCodePays())
    // .score(match.getScoreEquipe2())
    // .build())
    // .date(match.getDateHeure().format(DateTimeFormatter.ofPattern("dd MMMM
    // yyyy")))
    // .time(match.getDateHeure().format(DateTimeFormatter.ofPattern("HH:mm")))
    // .stadium(match.getStade().getNom())
    // .city(match.getStade().getVille())
    // .status(mapStatus(match.getStatut()))
    // .currentMinute(calculateCurrentMinute(match)) // Logic to calculate minute
    // .events(match.getEvenements().stream().map(this::mapEventToDTO).collect(Collectors.toList()))
    // .build();
    // }

    // private EvenementMatchDTO mapEventToDTO(EvenementMatch event) {
    // return EvenementMatchDTO.builder()
    // .minute(event.getMinute())
    // .type(event.getTypeEvenement())
    // .player(event.getJoueurNom())
    // .description(event.getDescription())
    // .team(determineTeam(event))
    // .build();
    // }

    // private String determineTeam(EvenementMatch event) {
    // // Determine if event belongs to team A or B based on match teams
    // if (event.getEquipe() == null)
    // return null;
    // if (event.getEquipe().getId().equals(event.getMatch().getEquipe1().getId()))
    // {
    // return "A";
    // } else if
    // (event.getEquipe().getId().equals(event.getMatch().getEquipe2().getId())) {
    // return "B";
    // }
    // return null;
    // }

    // private String mapStatus(StatutMatchEnum status) {
    // switch (status) {
    // case EN_COURS:
    // return "live";
    // case TERMINE:
    // return "finished";
    // case A_VENIR:
    // default:
    // return "upcoming";
    // }
    // }

    // private Integer calculateCurrentMinute(Match match) {
    // // Simplistic logic, normally would calculate diff from now to start time if
    // // live
    // if (match.getStatut() == StatutMatchEnum.EN_COURS) {
    // // This is just a placeholder. In real app, we diff LocalDateTime.now() with
    // // match.getDateHeure()
    // // considering half-time breaks etc.
    // long minutes = java.time.Duration.between(match.getDateHeure(),
    // LocalDateTime.now()).toMinutes();
    // return (int) minutes;
    // }
    // return null;
    // }
}
