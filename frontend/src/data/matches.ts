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

export const matches: Match[] = [
  // Phase de groupes
  {
    id: 'match-1',
    phase: 'Phase de groupes',
    teamA: { name: 'Mexico', flag: '🇲🇽', score: 2 },
    teamB: { name: 'Costa Rica', flag: '🇨🇷', score: 1 },
    date: '11 juin 2026',
    time: '17:00',
    stadium: 'Estadio Azteca',
    city: 'Mexico City',
    status: 'finished',
    events: [
      { minute: 23, type: 'goal', team: 'A', player: 'H. Lozano', description: 'But de H. Lozano' },
      { minute: 45, type: 'yellow_card', team: 'B', player: 'K. Waston', description: 'Carton jaune' },
      { minute: 67, type: 'goal', team: 'B', player: 'J. Campbell', description: 'But de J. Campbell' },
      { minute: 82, type: 'goal', team: 'A', player: 'R. Jiménez', description: 'But de R. Jiménez' },
    ],
  },
  {
    id: 'match-1b',
    phase: 'Phase de groupes',
    teamA: { name: 'Spain', flag: '🇪🇸', score: 3 },
    teamB: { name: 'Portugal', flag: '🇵🇹', score: 2 },
    date: '11 juin 2026',
    time: '20:00',
    stadium: 'Arrowhead Stadium',
    city: 'Kansas City',
    status: 'finished',
    events: [
      { minute: 15, type: 'goal', team: 'A', player: 'Morata', description: 'But de Morata' },
      { minute: 28, type: 'goal', team: 'B', player: 'Cristiano Ronaldo', description: 'But de Cristiano Ronaldo' },
      { minute: 41, type: 'goal', team: 'A', player: 'Pedri', description: 'But de Pedri' },
      { minute: 58, type: 'yellow_card', team: 'B', player: 'Pepe', description: 'Carton jaune' },
      { minute: 72, type: 'goal', team: 'B', player: 'Bruno Fernandes', description: 'But de Bruno Fernandes' },
      { minute: 85, type: 'goal', team: 'A', player: 'Gavi', description: 'But de Gavi' },
    ],
  },
  {
    id: 'match-2',
    phase: 'Phase de groupes',
    teamA: { name: 'USA', flag: '🇺🇸', score: 1 },
    teamB: { name: 'Canada', flag: '🇨🇦', score: 1 },
    date: '12 juin 2026',
    time: '20:00',
    stadium: 'SoFi Stadium',
    city: 'Los Angeles',
    status: 'live',
    currentMinute: 67,
    events: [
      { minute: 34, type: 'goal', team: 'A', player: 'C. Pulisic', description: 'But de C. Pulisic' },
      { minute: 56, type: 'goal', team: 'B', player: 'A. Davies', description: 'But de A. Davies' },
      { minute: 62, type: 'yellow_card', team: 'A', player: 'W. McKennie', description: 'Carton jaune' },
    ],
  },
  {
    id: 'match-3',
    phase: 'Phase de groupes',
    teamA: { name: 'Argentina', flag: '🇦🇷' },
    teamB: { name: 'Uruguay', flag: '🇺🇾' },
    date: '13 juin 2026',
    time: '15:00',
    stadium: 'MetLife Stadium',
    city: 'New York/NJ',
    status: 'upcoming',
  },
  {
    id: 'match-4',
    phase: 'Phase de groupes',
    teamA: { name: 'Brazil', flag: '🇧🇷' },
    teamB: { name: 'Chile', flag: '🇨🇱' },
    date: '14 juin 2026',
    time: '18:00',
    stadium: 'Hard Rock Stadium',
    city: 'Miami',
    status: 'upcoming',
  },
  {
    id: 'match-5',
    phase: 'Phase de groupes',
    teamA: { name: 'England', flag: '🏴󠁧󠁢󠁥󠁮󠁧󠁿' },
    teamB: { name: 'Belgium', flag: '🇧🇪' },
    date: '14 juin 2026',
    time: '21:00',
    stadium: 'AT&T Stadium',
    city: 'Dallas',
    status: 'upcoming',
  },
  {
    id: 'match-6',
    phase: 'Phase de groupes',
    teamA: { name: 'France', flag: '🇫🇷' },
    teamB: { name: 'Germany', flag: '🇩🇪' },
    date: '15 juin 2026',
    time: '12:00',
    stadium: 'Mercedes-Benz Stadium',
    city: 'Atlanta',
    status: 'upcoming',
  },
  
  // Huitièmes de finale
  {
    id: 'match-7',
    phase: 'Huitièmes de finale',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '28 juin 2026',
    time: '16:00',
    stadium: 'MetLife Stadium',
    city: 'New York/NJ',
    status: 'upcoming',
  },
  {
    id: 'match-8',
    phase: 'Huitièmes de finale',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '28 juin 2026',
    time: '20:00',
    stadium: 'SoFi Stadium',
    city: 'Los Angeles',
    status: 'upcoming',
  },
  
  // Quarts de finale
  {
    id: 'match-9',
    phase: 'Quarts de finale',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '9 juillet 2026',
    time: '17:00',
    stadium: 'Hard Rock Stadium',
    city: 'Miami',
    status: 'upcoming',
  },
  {
    id: 'match-10',
    phase: 'Quarts de finale',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '10 juillet 2026',
    time: '17:00',
    stadium: 'AT&T Stadium',
    city: 'Dallas',
    status: 'upcoming',
  },
  
  // Demi-finales
  {
    id: 'match-11',
    phase: 'Demi-finales',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '14 juillet 2026',
    time: '20:00',
    stadium: 'AT&T Stadium',
    city: 'Dallas',
    status: 'upcoming',
  },
  {
    id: 'match-12',
    phase: 'Demi-finales',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '15 juillet 2026',
    time: '20:00',
    stadium: 'Mercedes-Benz Stadium',
    city: 'Atlanta',
    status: 'upcoming',
  },
  
  // Petite finale
  {
    id: 'match-13',
    phase: 'Petite finale',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '18 juillet 2026',
    time: '16:00',
    stadium: 'Hard Rock Stadium',
    city: 'Miami',
    status: 'upcoming',
  },
  
  // Finale
  {
    id: 'match-14',
    phase: 'Finale',
    teamA: { name: 'TBD', flag: '🏳️' },
    teamB: { name: 'TBD', flag: '🏳️' },
    date: '19 juillet 2026',
    time: '15:00',
    stadium: 'MetLife Stadium',
    city: 'New York/NJ',
    status: 'upcoming',
  },
];