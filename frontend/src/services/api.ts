import { Match, MatchEvent, MatchStatus } from '../data/matches';

const API_BASE_URL = '/api';

// ── Frontend types ───────────────────────────────────────────────────

export interface Team {
  id: number;
  name: string;
  code: string;
  flag: string;
  groupCode: string;
  fifaRanking: number;
  confederation: string;
  group: {
    id: number;
    letter: string;
    name: string;
  } | null;
}

export interface Group {
  id: number;
  lettre: string;
  nom: string;
}

export interface Standing {
  id: number;
  groupe: {
    id: number;
    lettre: string;
    nom: string;
  };
  equipe: {
    id: number;
    nom: string;
    codePays: string;
    drapeauUrl: string;
  };
  matchsJoues: number;
  victoires: number;
  nuls: number;
  defaites: number;
  butsPour: number;
  butsContre: number;
  differenceButs: number;
  points: number;
  position: number;
}

// ── Backend DTO types ────────────────────────────────────────────────

interface BackendEquipe {
  id: number;
  nom: string;
  codePays: string;
  drapeauUrl: string;
}

interface BackendTeamDTO {
  id: number;
  nom: string;
  codePays: string;
  drapeauUrl: string;
  groupeCode: string;
  fifaRanking: number;
  confederation: string;
  groupe: {
    id: number;
    lettre: string;
    nom: string;
  } | null;
}

interface BackendStade {
  id: number;
  nom: string;
  ville: string;
  pays: string;
}

interface BackendPhase {
  id: number;
  nom: string;           // e.g. "PHASE_GROUPES"
  ordre: number;
  description: string;
  nombreMatchs: number;
}

interface BackendEvent {
  id: number;
  matchId: number;
  equipe: { id: number; nom: string; codePays: string } | null;
  typeEvenement: string; // "goal", "yellow_card", etc.
  minute: number;
  minuteAdditionnelle: number | null;
  joueurNom: string;
  description: string;
}

interface BackendMatchDTO {
  id: number;
  phase: BackendPhase;
  dateHeure: string;     // ISO date-time
  stade: BackendStade;
  equipe1: BackendEquipe;
  equipe2: BackendEquipe;
  scoreEquipe1: number | null;
  scoreEquipe2: number | null;
  statut: string;        // "A_VENIR" | "EN_COURS" | "TERMINE" | "REPORTE" | "ANNULE"
  groupe: string | null;
  tempsReglementaire: number | null;
  evenements: BackendEvent[] | null;
}

// ── Phase display name mapping ───────────────────────────────────────

const PHASE_DISPLAY_NAMES: Record<string, string> = {
  PHASE_GROUPES: 'Phase de groupes',
  SEIZIEMES_FINALE: 'Seizièmes de finale',
  HUITIEMES_FINALE: 'Huitièmes de finale',
  QUARTS_FINALE: 'Quarts de finale',
  DEMI_FINALES: 'Demi-finales',
  PETITE_FINALE: 'Petite finale',
  FINALE: 'Finale',
};

// ── Mappers ──────────────────────────────────────────────────────────

function mapStatus(statut: string): MatchStatus {
  switch (statut) {
    case 'EN_COURS':  return 'live';
    case 'TERMINE':   return 'finished';
    default:          return 'upcoming';   // A_VENIR, REPORTE, ANNULE
  }
}

function formatDate(iso: string): string {
  const d = new Date(iso);
  const months = ['janvier','février','mars','avril','mai','juin',
                  'juillet','août','septembre','octobre','novembre','décembre'];
  return `${d.getDate()} ${months[d.getMonth()]} ${d.getFullYear()}`;
}

function formatTime(iso: string): string {
  const d = new Date(iso);
  return `${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;
}

function mapEvent(evt: BackendEvent, equipe1Id: number, _equipe2Id: number): MatchEvent {
  const team: 'A' | 'B' = evt.equipe?.id === equipe1Id ? 'A' : 'B';
  return {
    minute: evt.minute,
    type: evt.typeEvenement as MatchEvent['type'],
    team,
    player: evt.joueurNom ?? '',
    description: evt.description ?? '',
  };
}

function estimateCurrentMinute(dateHeure: string): number {
  const kickoff = new Date(dateHeure).getTime();
  const now = Date.now();
  const elapsed = Math.floor((now - kickoff) / 60000);
  return Math.max(0, Math.min(elapsed, 90));
}

function mapMatch(dto: BackendMatchDTO): Match {
  const status = mapStatus(dto.statut);
  const phaseName = dto.phase?.nom;
  return {
    id: dto.id,
    phase: PHASE_DISPLAY_NAMES[phaseName] ?? phaseName ?? '',
    teamA: {
      id: dto.equipe1?.id,
      name: dto.equipe1?.nom ?? 'TBD',
      flag: dto.equipe1?.drapeauUrl ?? '🏳️',
      score: dto.scoreEquipe1 ?? undefined,
      code: dto.equipe1?.codePays,
    },
    teamB: {
      id: dto.equipe2?.id,
      name: dto.equipe2?.nom ?? 'TBD',
      flag: dto.equipe2?.drapeauUrl ?? '🏳️',
      score: dto.scoreEquipe2 ?? undefined,
      code: dto.equipe2?.codePays,
    },
    date: formatDate(dto.dateHeure),
    time: formatTime(dto.dateHeure),
    stadium: dto.stade?.nom ?? '',
    city: dto.stade?.ville ?? '',
    status,
    currentMinute: status === 'live' ? (dto.tempsReglementaire ?? estimateCurrentMinute(dto.dateHeure)) : undefined,
    events: dto.evenements?.map(e => mapEvent(e, dto.equipe1?.id, dto.equipe2?.id)) ?? [],
  };
}

function mapTeam(dto: BackendTeamDTO): Team {
  return {
    id: dto.id,
    name: dto.nom,
    code: dto.codePays,
    flag: dto.drapeauUrl,
    groupCode: dto.groupeCode,
    fifaRanking: dto.fifaRanking,
    confederation: dto.confederation,
    group: dto.groupe ? {
      id: dto.groupe.id,
      letter: dto.groupe.lettre,
      name: dto.groupe.nom,
    } : null,
  };
}

// ── API client ───────────────────────────────────────────────────────

export const api = {
  getMatches: async (): Promise<Match[]> => {
    const response = await fetch(`${API_BASE_URL}/matches`);
    if (!response.ok) throw new Error('Failed to fetch matches');
    const data: BackendMatchDTO[] = await response.json();
    return data.map(mapMatch);
  },

  getMatchesByPhase: async (phase: string): Promise<Match[]> => {
    const response = await fetch(`${API_BASE_URL}/matches/phase/${encodeURIComponent(phase)}`);
    if (!response.ok) throw new Error('Failed to fetch matches by phase');
    const data: BackendMatchDTO[] = await response.json();
    return data.map(mapMatch);
  },

  getMatchById: async (id: string | number): Promise<Match> => {
    const response = await fetch(`${API_BASE_URL}/matches/${id}`);
    if (!response.ok) throw new Error('Failed to fetch match details');
    const data: BackendMatchDTO = await response.json();
    return mapMatch(data);
  },

  getLiveMatches: async (): Promise<Match[]> => {
    const response = await fetch(`${API_BASE_URL}/matches/live`);
    if (!response.ok) throw new Error('Failed to fetch live matches');
    const data: BackendMatchDTO[] = await response.json();
    return data.map(mapMatch);
  },

  getPhases: async (): Promise<any[]> => {
    const response = await fetch(`${API_BASE_URL}/phases`);
    if (!response.ok) throw new Error('Failed to fetch phases');
    return response.json();
  },

  getTeams: async (): Promise<Team[]> => {
    const response = await fetch(`${API_BASE_URL}/teams`);
    if (!response.ok) throw new Error('Failed to fetch teams');
    const data: BackendTeamDTO[] = await response.json();
    return data.map(mapTeam);
  },

  getTeamById: async (id: string | number): Promise<Team> => {
    const response = await fetch(`${API_BASE_URL}/teams/${id}`);
    if (!response.ok) throw new Error('Failed to fetch team details');
    const data: BackendTeamDTO = await response.json();
    return mapTeam(data);
  },

  getGroups: async (): Promise<Group[]> => {
    const response = await fetch(`${API_BASE_URL}/groups`);
    if (!response.ok) throw new Error('Failed to fetch groups');
    return response.json();
  },

  getGroupById: async (id: string | number): Promise<Group> => {
    const response = await fetch(`${API_BASE_URL}/groups/${id}`);
    if (!response.ok) throw new Error('Failed to fetch group details');
    return response.json();
  },

  getStandings: async (): Promise<Standing[]> => {
    const response = await fetch(`${API_BASE_URL}/standings`);
    if (!response.ok) throw new Error('Failed to fetch standings');
    return response.json();
  },

  getStandingsByGroup: async (groupName: string): Promise<Standing[]> => {
    const response = await fetch(`${API_BASE_URL}/standings/group/${encodeURIComponent(groupName)}`);
    if (!response.ok) throw new Error('Failed to fetch group standings');
    return response.json();
  }
};
