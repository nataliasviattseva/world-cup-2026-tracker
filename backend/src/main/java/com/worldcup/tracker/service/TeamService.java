package com.worldcup.tracker.service;

import com.worldcup.tracker.dto.TeamDTO;
import java.util.List;

public interface TeamService {
    List<TeamDTO> getAllTeams();
    TeamDTO getTeamById(Long id);
}
