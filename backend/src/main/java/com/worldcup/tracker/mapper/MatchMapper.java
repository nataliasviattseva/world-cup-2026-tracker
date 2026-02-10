package com.worldcup.tracker.mapper;

import com.worldcup.tracker.dto.MatchDTO;
import com.worldcup.tracker.model.Match;

public class MatchMapper {
    public static MatchDTO toDTO(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setEquipe1(match.getEquipe1().getNom());
        dto.setEquipe2(match.getEquipe2().getNom());
        dto.setScoreEquipe1(match.getScoreEquipe1());
        dto.setScoreEquipe2(match.getScoreEquipe2());
        dto.setGroupe(match.getGroupe());
        dto.setPhase(match.getPhase().getNom().name());
        dto.setStatut(match.getStatut().name());
        dto.setStade(match.getStade().getNom());
        dto.setDateHeure(match.getDateHeure().toString());
        return dto;
    }
}
