package com.worldcup.tracker.service;

import com.worldcup.tracker.dto.MatchStatisticsDTO;

public interface MatchStatisticsService {
    MatchStatisticsDTO getStatisticsForMatch(Long matchId);
}
