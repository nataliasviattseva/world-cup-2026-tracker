package com.worldcup.tracker.service.external.mapper;

import com.worldcup.tracker.model.Equipe;
import com.worldcup.tracker.model.Match;

public class FootballApiMapper {

    public Match toMatch(ApiMatchResponse api) {
        return Match.builder()
                .dateHeure(api.getFixture().getDate())
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
}
