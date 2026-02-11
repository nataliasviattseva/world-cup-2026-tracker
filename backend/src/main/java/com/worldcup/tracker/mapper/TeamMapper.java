package com.worldcup.tracker.mapper;

import com.worldcup.tracker.dto.TeamDTO;
import com.worldcup.tracker.model.Equipe;

public class TeamMapper {
    public static TeamDTO toDTO(Equipe equipe) {
        TeamDTO dto = new TeamDTO();
        dto.setId(equipe.getId());
        dto.setName(equipe.getNom());
        dto.setFlagUrl(equipe.getDrapeauUrl());


        return dto;
    }
}
