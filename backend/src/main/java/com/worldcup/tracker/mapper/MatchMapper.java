package com.worldcup.tracker.mapper;

import com.worldcup.tracker.dto.MatchDTO;
import com.worldcup.tracker.dto.TeamDTO;
import com.worldcup.tracker.model.Match;

public class MatchMapper {
    public static MatchDTO toDTO(Match match) {
        return MatchDTO.builder()
                .id(match.getId())
                .phase(match.getPhase()
                        .getNom().name())

        // Teams
        .teamA(TeamDTO.builder()
                .id(match.getEquipe1().getId())
                .name(match.getEquipe1().getNom())
                .code(match.getEquipe1().getCodePays())
                .build())
        .teamB(TeamDTO.builder()
                .id(match.getEquipe2().getId())
                .name(match.getEquipe2().getNom())
                .code(match.getEquipe2().getCodePays())
                .build())

        // Score
        .scoreTeam1(match.getScoreEquipe1())
        .scoreTeam2(match.getScoreEquipe2())

        // Group
        .group(match.getGroupe())

        // Date & time
        .date(match.getDateHeure().toLocalDate().toString())
        .time(match.getDateHeure().toLocalTime().toString())

        // Stadium
        .stadium(match.getStade().getNom())
        .city(match.getStade().getVille())

        // Status
        .status(match.getStatut().name())

        .build();
    }
}
