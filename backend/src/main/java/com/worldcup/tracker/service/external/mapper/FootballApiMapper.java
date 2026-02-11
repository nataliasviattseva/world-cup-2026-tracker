package com.worldcup.tracker.service.external.mapper;

import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.StatutMatchEnum;
import com.worldcup.tracker.service.external.dto.ApiMatchResponse;
import com.worldcup.tracker.service.external.dto.ApiTeam;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class FootballApiMapper {

    public Match toMatch(ApiMatchResponse api) {

        OffsetDateTime odt = OffsetDateTime.parse(api.getFixture().getDate());
        LocalDateTime dateHeure = odt.toLocalDateTime();

        return Match.builder()
                .dateHeure(dateHeure)
                .equipe1(toEquipe(api.getTeams().getHome()))
                .equipe2(toEquipe(api.getTeams().getAway()))
                .statut(convertStatus(api.getFixture().getStatus()))
                .build();
    }

    public Equipe toEquipe(ApiTeam apiTeam) {
        return Equipe.builder()
                .id(apiTeam.getId())
                .nom(apiTeam.getName())
                .build();
    }

    private StatutMatchEnum convertStatus(ApiMatchResponse.Status status) {
        String s = status.getShortStatus();

        return switch (s) {
            case "NS" -> StatutMatchEnum.A_VENIR;
            case "1H", "HT", "2H" -> StatutMatchEnum.EN_COURS;
            case "FT" -> StatutMatchEnum.TERMINE;
            case "PST" -> StatutMatchEnum.REPORTE;
            case "CANC" -> StatutMatchEnum.ANNULE;
            default -> StatutMatchEnum.A_VENIR;
        };
    }
}
