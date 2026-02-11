import { Match } from '../data/matches';

const API_BASE_URL = '/api';

export const api = {
  getMatches: async (): Promise<Match[]> => {
    const response = await fetch(`${API_BASE_URL}/matches`);
    if (!response.ok) throw new Error('Failed to fetch matches');
    return response.json();
  },

  getMatchesByPhase: async (phase: string): Promise<Match[]> => {
    // The backend endpoint expects /api/matches/phase/{phaseName}
    // We handle the phase name encoding here
    const response = await fetch(`${API_BASE_URL}/matches/phase/${encodeURIComponent(phase)}`);
    if (!response.ok) throw new Error('Failed to fetch matches by phase');
    return response.json();
  },

  getMatchById: async (id: string): Promise<Match> => {
    const response = await fetch(`${API_BASE_URL}/matches/${id}`);
    if (!response.ok) throw new Error('Failed to fetch match details');
    return response.json();
  },

  getLiveMatches: async (): Promise<Match[]> => {
    const response = await fetch(`${API_BASE_URL}/matches/live`);
    if (!response.ok) throw new Error('Failed to fetch live matches');
    return response.json();
  },

  getPhases: async (): Promise<any[]> => {
    const response = await fetch(`${API_BASE_URL}/phases`);
    if (!response.ok) throw new Error('Failed to fetch phases');
    return response.json();
  }
};
