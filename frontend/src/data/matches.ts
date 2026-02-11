// Type definitions for matches
// All match data is now fetched from the database via API

export type MatchStatus = 'upcoming' | 'live' | 'finished';

export interface Match {
  id: string | number;
  phase: string;
  teamA: {
    id?: number;
    name: string;
    flag: string;
    score?: number;
    code?: string;
  };
  teamB: {
    id?: number;
    name: string;
    flag: string;
    score?: number;
    code?: string;
  };
  date: string;
  time: string;
  stadium: string;
  city: string;
  status: MatchStatus;
  currentMinute?: number;
  events?: MatchEvent[];
}

export interface MatchEvent {
  minute: number;
  type: 'goal' | 'yellow_card' | 'red_card' | 'substitution';
  team: 'A' | 'B';
  player: string;
  description: string;
}