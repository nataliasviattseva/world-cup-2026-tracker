import { useEffect, useState } from 'react';
import { ArrowLeft, TrendingUp } from 'lucide-react';
import { Screen } from '../../App';
import { Match } from '../../data/matches';
import { api } from '../../services/api';

interface LiveMatchScreenProps {
  matchId: string | number;
  onNavigate: (screen: Screen) => void;
}

export function LiveMatchScreen({ matchId, onNavigate }: LiveMatchScreenProps) {
  const [match, setMatch] = useState<Match | null>(null);
  const [currentMinute, setCurrentMinute] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMatch = async () => {
      try {
        const data = await api.getMatchById(matchId);
        setMatch(data);
        setCurrentMinute(data.currentMinute || 0);
      } catch (error) {
        console.error("Failed to load live match:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMatch();

    // Poll for updates every 10 seconds (or 2s if simulation needed)
    const pollInterval = setInterval(fetchMatch, 10000);
    return () => clearInterval(pollInterval);
  }, [matchId]);

  // Simulate local minute increment if live
  useEffect(() => {
    if (match?.status !== 'live') return;

    const interval = setInterval(() => {
      setCurrentMinute(prev => {
        const next = prev + 1;
        return next > 90 ? 90 : next;
      });
    }, 60000); // Increment every real minute (or 2000 for demo speed)

    return () => clearInterval(interval);
  }, [match]);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-900">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  if (!match) {
    return <div>Match non trouvé</div>;
  }

  const getEventIcon = (type: string) => {
    switch (type) {
      case 'goal':
        return '⚽';
      case 'yellow_card':
        return '🟨';
      case 'red_card':
        return '🟥';
      case 'substitution':
        return '🔄';
      default:
        return '📝';
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-emerald-950 to-gray-900">
      {/* Header */}
      <header className="bg-black/40 backdrop-blur-lg border-b border-white/10 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-20">
            <div className="flex items-center space-x-4">
              <button
                onClick={() => onNavigate({ type: 'detail', matchId: match.id })}
                className="p-2 hover:bg-white/10 rounded-xl transition-colors group"
              >
                <ArrowLeft className="h-6 w-6 text-white group-hover:text-emerald-400 transition-colors" />
              </button>
              <div>
                <span className="text-lg font-bold text-white">Match en direct</span>
                <p className="text-xs text-white/70">{match.phase}</p>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <div className="flex items-center space-x-2 bg-red-500 text-white px-4 py-2 rounded-full text-sm font-bold shadow-lg">
                <span className="relative flex h-2 w-2">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-white opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-2 w-2 bg-white"></span>
                </span>
                <span>EN DIRECT</span>
              </div>
              <div className="text-white text-2xl font-bold">{currentMinute}'</div>
            </div>
          </div>
        </div>
      </header>

      {/* Live Score */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="bg-gradient-to-br from-emerald-500 to-green-600 rounded-3xl shadow-2xl p-8 mb-8 border-2 border-white/20">
          <div className="text-center mb-4">
            <p className="text-white/90 text-sm font-semibold mb-2">{match.phase}</p>
            <p className="text-white/80 text-xs">{match.stadium}, {match.city}</p>
          </div>
          
          <div className="flex items-center justify-between">
            {/* Team A */}
            <div className="flex-1 text-center">
              <div className="text-7xl mb-4 filter drop-shadow-lg">{match.teamA.flag}</div>
              <h2 className="text-2xl font-bold text-white mb-4">{match.teamA.name}</h2>
              <div className="text-7xl font-bold text-white drop-shadow-2xl">
                {match.teamA.score || 0}
              </div>
            </div>

            {/* Live Indicator */}
            <div className="px-8">
              <div className="relative">
                <div className="absolute inset-0 bg-white/20 blur-xl rounded-full"></div>
                <div className="relative bg-white/30 backdrop-blur-sm rounded-full p-6 border-2 border-white/40">
                  <TrendingUp className="h-12 w-12 text-white animate-pulse" />
                </div>
              </div>
            </div>

            {/* Team B */}
            <div className="flex-1 text-center">
              <div className="text-7xl mb-4 filter drop-shadow-lg">{match.teamB.flag}</div>
              <h2 className="text-2xl font-bold text-white mb-4">{match.teamB.name}</h2>
              <div className="text-7xl font-bold text-white drop-shadow-2xl">
                {match.teamB.score || 0}
              </div>
            </div>
          </div>
        </div>

        {/* Match Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-6 border border-white/20">
            <p className="text-white/70 text-sm mb-2">Possession</p>
            <div className="flex items-center justify-between mb-2">
              <span className="text-2xl font-bold text-white">52%</span>
              <span className="text-2xl font-bold text-white">48%</span>
            </div>
            <div className="h-2 bg-white/20 rounded-full overflow-hidden">
              <div className="h-full bg-gradient-to-r from-emerald-400 to-green-500" style={{ width: '52%' }}></div>
            </div>
          </div>

          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-6 border border-white/20">
            <p className="text-white/70 text-sm mb-2">Tirs</p>
            <div className="flex items-center justify-between">
              <span className="text-3xl font-bold text-white">8</span>
              <span className="text-white/50">⚽</span>
              <span className="text-3xl font-bold text-white">6</span>
            </div>
          </div>

          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-6 border border-white/20">
            <p className="text-white/70 text-sm mb-2">Corners</p>
            <div className="flex items-center justify-between">
              <span className="text-3xl font-bold text-white">5</span>
              <span className="text-white/50">🚩</span>
              <span className="text-3xl font-bold text-white">3</span>
            </div>
          </div>
        </div>

        {/* Live Events Timeline */}
        <div className="bg-white/10 backdrop-blur-md rounded-2xl p-8 border border-white/20">
          <h3 className="text-2xl font-bold text-white mb-6 flex items-center">
            <span className="mr-3">📊</span>
            Événements en direct
          </h3>
          
          <div className="space-y-4">
            {match.events && match.events.length > 0 ? (
              match.events
                .sort((a, b) => b.minute - a.minute)
                .map((event, index) => (
                  <div
                    key={index}
                    className={`flex items-center space-x-4 p-4 rounded-xl transition-all animate-fade-in ${
                      event.type === 'goal'
                        ? 'bg-yellow-500/20 border-2 border-yellow-400'
                        : 'bg-white/5 border border-white/10'
                    }`}
                  >
                    <div className="bg-emerald-500 text-white rounded-full w-14 h-14 flex items-center justify-center font-bold text-lg shadow-lg">
                      {event.minute}'
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center space-x-3 mb-1">
                        <span className="text-3xl">{getEventIcon(event.type)}</span>
                        <p className="font-bold text-white text-lg">{event.player}</p>
                      </div>
                      <p className="text-sm text-white/70">{event.description}</p>
                    </div>
                    <div className="text-4xl">
                      {event.team === 'A' ? match.teamA.flag : match.teamB.flag}
                    </div>
                  </div>
                ))
            ) : (
              <div className="text-center py-8 text-white/50">
                Aucun événement pour le moment...
              </div>
            )}
          </div>

          {/* Latest Update Indicator */}
          <div className="mt-6 pt-6 border-t border-white/10">
            <div className="flex items-center justify-center space-x-2 text-white/60 text-sm">
              <div className="relative flex h-2 w-2">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
              </div>
              <span>Mise à jour en temps réel</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}