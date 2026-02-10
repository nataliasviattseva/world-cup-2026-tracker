package com.worldcup.tracker.service;

import com.worldcup.tracker.dto.MatchStatisticsDTO;


public interface IMatchStatisticsService {
    MatchStatisticsDTO getStatisticsForMatch(Long matchId);
}
