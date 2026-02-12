import { useState } from 'react';
import { HomeScreen } from './components/screens/HomeScreen';
import { PhasesScreen } from './components/screens/PhasesScreen';
import { MatchesListScreen } from './components/screens/MatchesListScreen';
import { MatchDetailScreen } from './components/screens/MatchDetailScreen';
import { LiveMatchScreen } from './components/screens/LiveMatchScreen';
import { LiveMatchesListScreen } from './components/screens/LiveMatchesListScreen';
import { LiveMatchEventsScreen } from './components/screens/LiveMatchEventsScreen';
import { TeamsScreen } from './components/screens/TeamsScreen';
import { TeamDetailScreen } from './components/screens/TeamDetailScreen';
import { StandingsScreen } from './components/screens/StandingsScreen';
import { WorldCupGroupsScreen } from './components/screens/WorldCupGroupsScreen';

export type Screen = 
  | { type: 'home' }
  | { type: 'phases' }
  | { type: 'matches'; phase: string }
  | { type: 'detail'; matchId: string | number }
  | { type: 'live'; matchId: string | number }
  | { type: 'liveMatches' }
  | { type: 'liveEvents'; matchId: string | number }
  | { type: 'teams' }
  | { type: 'teamDetail'; teamId: number }
  | { type: 'standings' }
  | { type: 'worldcupgroups' };

export default function App() {
  const [currentScreen, setCurrentScreen] = useState<Screen>({ type: 'home' });

  const renderScreen = () => {
    switch (currentScreen.type) {
      case 'home':
        return <HomeScreen onNavigate={setCurrentScreen} />;
      case 'phases':
        return <PhasesScreen onNavigate={setCurrentScreen} />;
      case 'matches':
        return <MatchesListScreen phase={currentScreen.phase} onNavigate={setCurrentScreen} />;
      case 'detail':
        return <MatchDetailScreen matchId={currentScreen.matchId} onNavigate={setCurrentScreen} />;
      case 'live':
        return <LiveMatchScreen matchId={currentScreen.matchId} onNavigate={setCurrentScreen} />;
      case 'liveMatches':
        return <LiveMatchesListScreen onNavigate={setCurrentScreen} />;
      case 'liveEvents':
        return <LiveMatchEventsScreen matchId={currentScreen.matchId} onNavigate={setCurrentScreen} />;
      case 'teams':
        return <TeamsScreen onNavigate={setCurrentScreen} />;
      case 'teamDetail':
        return <TeamDetailScreen teamId={currentScreen.teamId} onNavigate={setCurrentScreen} />;
      case 'standings':
        return <StandingsScreen onNavigate={setCurrentScreen} />;
      case 'worldcupgroups':
        return <WorldCupGroupsScreen onNavigate={setCurrentScreen} />;
      default:
        return <HomeScreen onNavigate={setCurrentScreen} />;
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-white to-green-50">
      {renderScreen()}
    </div>
  );
}
