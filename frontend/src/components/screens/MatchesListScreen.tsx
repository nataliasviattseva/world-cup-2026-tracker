import { useEffect, useState } from 'react';
import { ArrowLeft, Calendar, MapPin, Clock } from 'lucide-react';
import { Screen } from '../../App';
import { Match } from '../../data/matches';
import { api } from '../../services/api';
import { Header } from '../Header';

interface MatchesListScreenProps {
  phase: string;
  onNavigate: (screen: Screen) => void;
}

export function MatchesListScreen({ phase, onNavigate }: MatchesListScreenProps) {
  const [phaseMatches, setPhaseMatches] = useState<Match[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMatches = async () => {
      try {
        const matches = await api.getMatchesByPhase(phase);
        setPhaseMatches(matches);
      } catch (error) {
        console.error("Failed to load matches for phase:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMatches();
  }, [phase]);

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'live':
        return (
          <div className="flex items-center space-x-2 bg-red-500 text-white px-4 py-1.5 rounded-full text-sm font-bold shadow-lg">
            <span className="relative flex h-2 w-2">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-white opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-white"></span>
            </span>
            <span>EN DIRECT</span>
          </div>
        );
      case 'finished':
        return (
          <div className="bg-gray-500 text-white px-4 py-1.5 rounded-full text-sm font-bold">
            TERMINÉ
          </div>
        );
      default:
        return (
          <div className="bg-emerald-500 text-white px-4 py-1.5 rounded-full text-sm font-bold">
            À VENIR
          </div>
        );
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-emerald-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen">
      {/* Header */}
      <Header 
        onNavigate={onNavigate} 
        title={phase}
        subtitle={`${phaseMatches.length} matchs`}
        showBack={true}
        backScreen={{ type: 'phases' }}
        currentScreen="matches"
      />

      {/* Content */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="space-y-4">
          {phaseMatches.map((match) => (
            <button
              key={match.id}
              onClick={() => onNavigate({ type: match.status === 'live' ? 'live' : 'detail', matchId: match.id })}
              className="w-full group bg-white rounded-2xl shadow-lg shadow-emerald-500/5 p-6 hover:shadow-2xl hover:shadow-emerald-500/10 transition-all duration-300 border border-emerald-100 hover:-translate-y-1 text-left"
            >
              {/* Status Badge */}
              <div className="flex items-center justify-between mb-6">
                {getStatusBadge(match.status)}
                {match.status === 'live' && match.currentMinute && (
                  <span className="text-emerald-600 font-bold text-lg">{match.currentMinute}'</span>
                )}
              </div>

              {/* Teams and Score */}
              <div className="flex items-center justify-between mb-6">
                {/* Team A */}
                <div className="flex-1 flex items-center space-x-4">
                  <span className="text-5xl">{match.teamA.flag}</span>
                  <div>
                    <p className="text-xl font-bold text-gray-900">{match.teamA.name}</p>
                  </div>
                </div>

                {/* Score */}
                {(match.status === 'live' || match.status === 'finished') && (
                  <div className="px-8">
                    <div className="flex items-center space-x-4 text-4xl font-bold">
                      <span className={match.teamA.score! > (match.teamB.score || 0) ? 'text-emerald-600' : 'text-gray-900'}>
                        {match.teamA.score}
                      </span>
                      <span className="text-gray-400">-</span>
                      <span className={match.teamB.score! > (match.teamA.score || 0) ? 'text-emerald-600' : 'text-gray-900'}>
                        {match.teamB.score}
                      </span>
                    </div>
                  </div>
                )}

                {match.status === 'upcoming' && (
                  <div className="px-8">
                    <div className="text-3xl font-bold text-gray-300">VS</div>
                  </div>
                )}

                {/* Team B */}
                <div className="flex-1 flex items-center justify-end space-x-4">
                  <div className="text-right">
                    <p className="text-xl font-bold text-gray-900">{match.teamB.name}</p>
                  </div>
                  <span className="text-5xl">{match.teamB.flag}</span>
                </div>
              </div>

              {/* Match Info */}
              <div className="flex flex-wrap gap-4 text-sm text-gray-600 pt-4 border-t border-emerald-100">
                <div className="flex items-center space-x-2">
                  <div className="bg-emerald-100 p-1.5 rounded-lg">
                    <Calendar className="h-4 w-4 text-emerald-600" />
                  </div>
                  <span className="font-semibold">{match.date}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <div className="bg-emerald-100 p-1.5 rounded-lg">
                    <Clock className="h-4 w-4 text-emerald-600" />
                  </div>
                  <span className="font-semibold">{match.time}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <div className="bg-emerald-100 p-1.5 rounded-lg">
                    <MapPin className="h-4 w-4 text-emerald-600" />
                  </div>
                  <span className="font-semibold">{match.stadium}, {match.city}</span>
                </div>
              </div>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}