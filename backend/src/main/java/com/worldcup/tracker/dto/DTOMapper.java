package com.worldcup.tracker.dto;

import com.worldcup.tracker.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between entities and DTOs
 */
@Component
public class DTOMapper {

    // Team mappings
    public EquipeDTO mapToTeamDTO(Equipe equipe) {
        if (equipe == null) return null;
        
        EquipeDTO.EquipeDTOBuilder builder = EquipeDTO.builder()
                .id(equipe.getId())
                .nom(equipe.getNom())
                .codePays(equipe.getCodePays())
                .drapeauUrl(equipe.getDrapeauUrl())
                .groupeCode(equipe.getGroupeCode())
                .fifaRanking(equipe.getFifaRanking())
                .confederation(equipe.getConfederation());
        
        if (equipe.getGroupe() != null) {
            builder.groupe(EquipeDTO.GroupeBasicDTO.builder()
                    .id(equipe.getGroupe().getId())
                    .lettre(equipe.getGroupe().getLettre())
                    .nom(equipe.getGroupe().getNom())
                    .build());
        }
        
        return builder.build();
    }

    // Phase mappings
    public PhaseCompetitionDTO mapToPhaseDTO(PhaseCompetition phase) {
        if (phase == null) return null;
        
        return PhaseCompetitionDTO.builder()
                .id(phase.getId())
                .nom(mapPhaseNom(phase.getNom()))
                .ordre(phase.getOrdre())
                .dateDebut(phase.getDateDebut())
                .dateFin(phase.getDateFin())
                .description(phase.getDescription())
                .nombreMatchs(phase.getNombreMatchs())
                .build();
    }

    private PhaseCompetitionDTO.PhaseNomDTO mapPhaseNom(PhaseNomEnum phaseNom) {
        if (phaseNom == null) return null;
        return PhaseCompetitionDTO.PhaseNomDTO.valueOf(phaseNom.name());
    }

    // Stadium mappings
    public StadeDTO mapToStadiumDTO(Stade stade) {
        if (stade == null) return null;
        
        return StadeDTO.builder()
                .id(stade.getId())
                .nom(stade.getNom())
                .ville(stade.getVille())
                .pays(stade.getPays())
                .capacite(stade.getCapacite())
                .latitude(stade.getLatitude())
                .longitude(stade.getLongitude())
                .timezone(stade.getTimezone())
                .build();
    }

    // Match Statistics mappings
    public StatistiqueMatchDTO mapToMatchStatisticsDTO(StatistiqueMatch stats) {
        if (stats == null) return null;
        
        return StatistiqueMatchDTO.builder()
                .id(stats.getId())
                .matchId(stats.getMatch() != null ? stats.getMatch().getId() : null)
                .possessionEquipe1(stats.getPossessionEquipe1())
                .possessionEquipe2(stats.getPossessionEquipe2())
                .tirsEquipe1(stats.getTirsEquipe1())
                .tirsEquipe2(stats.getTirsEquipe2())
                .tirsCadresEquipe1(stats.getTirsCadresEquipe1())
                .tirsCadresEquipe2(stats.getTirsCadresEquipe2())
                .cornersEquipe1(stats.getCornersEquipe1())
                .cornersEquipe2(stats.getCornersEquipe2())
                .fautesEquipe1(stats.getFautesEquipe1())
                .fautesEquipe2(stats.getFautesEquipe2())
                .cartonsJaunesEquipe1(stats.getCartonsJaunesEquipe1())
                .cartonsJaunesEquipe2(stats.getCartonsJaunesEquipe2())
                .cartonsRougesEquipe1(stats.getCartonsRougesEquipe1())
                .cartonsRougesEquipe2(stats.getCartonsRougesEquipe2())
                .horsJeuEquipe1(stats.getHorsJeuEquipe1())
                .horsJeuEquipe2(stats.getHorsJeuEquipe2())
                .build();
    }

    // Match Event mappings
    public EvenementMatchDTO mapToMatchEventDTO(EvenementMatch event) {
        if (event == null) return null;
        
        EvenementMatchDTO.EvenementMatchDTOBuilder builder = EvenementMatchDTO.builder()
                .id(event.getId())
                .matchId(event.getMatch() != null ? event.getMatch().getId() : null)
                .typeEvenement(event.getTypeEvenement())
                .minute(event.getMinute())
                .minuteAdditionnelle(event.getMinuteAdditionnelle())
                .joueurNom(event.getJoueurNom())
                .joueurNumero(event.getJoueurNumero())
                .description(event.getDescription());
        
        if (event.getEquipe() != null) {
            builder.equipe(EvenementMatchDTO.EquipeBasicDTO.builder()
                    .id(event.getEquipe().getId())
                    .nom(event.getEquipe().getNom())
                    .codePays(event.getEquipe().getCodePays())
                    .build());
        }
        
        return builder.build();
    }

    // Group mappings
    public GroupeDTO mapToGroupDTO(Groupe groupe) {
        if (groupe == null) return null;
        
        GroupeDTO.GroupeDTOBuilder builder = GroupeDTO.builder()
                .id(groupe.getId())
                .lettre(groupe.getLettre())
                .nom(groupe.getNom());
        
        if (groupe.getEquipes() != null) {
            List<EquipeDTO> teamDTOs = groupe.getEquipes().stream()
                    .map(this::mapToTeamDTO)
                    .collect(Collectors.toList());
            builder.equipes(teamDTOs);
        }
        
        return builder.build();
    }

    // Group Standing mappings
    public ClassementGroupeDTO mapToGroupStandingDTO(ClassementGroupe standing) {
        if (standing == null) return null;
        
        return ClassementGroupeDTO.builder()
                .id(standing.getId())
                .groupe(mapToGroupDTO(standing.getGroupe()))
                .equipe(mapToTeamDTO(standing.getEquipe()))
                .matchsJoues(standing.getMatchsJoues())
                .victoires(standing.getVictoires())
                .nuls(standing.getNuls())
                .defaites(standing.getDefaites())
                .butsPour(standing.getButsPour())
                .butsContre(standing.getButsContre())
                .differenceButs(standing.getDifferenceButs())
                .points(standing.getPoints())
                .position(standing.getPosition())
                .build();
    }

    // Match mappings
    public MatchDTO mapToMatchDTO(Match match) {
        if (match == null) return null;
        
        MatchDTO.MatchDTOBuilder builder = MatchDTO.builder()
                .id(match.getId())
                .phase(mapToPhaseDTO(match.getPhase()))
                .dateHeure(match.getDateHeure())
                .stade(mapToStadiumDTO(match.getStade()))
                .equipe1(mapToTeamDTO(match.getEquipe1()))
                .equipe2(mapToTeamDTO(match.getEquipe2()))
                .scoreEquipe1(match.getScoreEquipe1())
                .scoreEquipe2(match.getScoreEquipe2())
                .statut(mapMatchStatus(match.getStatut()))
                .groupe(match.getGroupe())
                .tempsReglementaire(match.getTempsReglementaire())
                .prolongations(match.getProlongations())
                .tirsAuBut(match.getTirsAuBut())
                .statistique(mapToMatchStatisticsDTO(match.getStatistique()));
        
        if (match.getEvenements() != null) {
            List<EvenementMatchDTO> eventDTOs = match.getEvenements().stream()
                    .map(this::mapToMatchEventDTO)
                    .collect(Collectors.toList());
            builder.evenements(eventDTOs);
        }
        
        return builder.build();
    }

    private MatchDTO.StatutMatchDTO mapMatchStatus(StatutMatchEnum status) {
        if (status == null) return null;
        return MatchDTO.StatutMatchDTO.valueOf(status.name());
    }

    // Match Summary mapping (lighter version)
    public MatchSummaryDTO mapToMatchSummaryDTO(Match match) {
        if (match == null) return null;
        
        return MatchSummaryDTO.builder()
                .id(match.getId())
                .phaseName(match.getPhase() != null ? match.getPhase().getNom().name() : null)
                .dateHeure(match.getDateHeure())
                .stadeNom(match.getStade() != null ? match.getStade().getNom() : null)
                .stadeVille(match.getStade() != null ? match.getStade().getVille() : null)
                .equipe1Nom(match.getEquipe1() != null ? match.getEquipe1().getNom() : null)
                .equipe1CodePays(match.getEquipe1() != null ? match.getEquipe1().getCodePays() : null)
                .equipe2Nom(match.getEquipe2() != null ? match.getEquipe2().getNom() : null)
                .equipe2CodePays(match.getEquipe2() != null ? match.getEquipe2().getCodePays() : null)
                .scoreEquipe1(match.getScoreEquipe1())
                .scoreEquipe2(match.getScoreEquipe2())
                .statut(mapMatchStatus(match.getStatut()))
                .groupe(match.getGroupe())
                .prolongations(match.getProlongations())
                .tirsAuBut(match.getTirsAuBut())
                .build();
    }

    // List mappings
    public List<EquipeDTO> mapToTeamDTOs(List<Equipe> equipes) {
        if (equipes == null) return null;
        return equipes.stream().map(this::mapToTeamDTO).collect(Collectors.toList());
    }

    public List<MatchDTO> mapToMatchDTOs(List<Match> matches) {
        if (matches == null) return null;
        return matches.stream().map(this::mapToMatchDTO).collect(Collectors.toList());
    }

    public List<MatchSummaryDTO> mapToMatchSummaryDTOs(List<Match> matches) {
        if (matches == null) return null;
        return matches.stream().map(this::mapToMatchSummaryDTO).collect(Collectors.toList());
    }

    public List<PhaseCompetitionDTO> mapToPhaseDTOs(List<PhaseCompetition> phases) {
        if (phases == null) return null;
        return phases.stream().map(this::mapToPhaseDTO).collect(Collectors.toList());
    }

    public List<StadeDTO> mapToStadiumDTOs(List<Stade> stades) {
        if (stades == null) return null;
        return stades.stream().map(this::mapToStadiumDTO).collect(Collectors.toList());
    }

    public List<GroupeDTO> mapToGroupDTOs(List<Groupe> groupes) {
        if (groupes == null) return null;
        return groupes.stream().map(this::mapToGroupDTO).collect(Collectors.toList());
    }

    public List<ClassementGroupeDTO> mapToGroupStandingDTOs(List<ClassementGroupe> standings) {
        if (standings == null) return null;
        return standings.stream().map(this::mapToGroupStandingDTO).collect(Collectors.toList());
    }
}