import { useEffect, useState } from 'react';
import { Radio, ArrowLeft, RefreshCw } from 'lucide-react';
import { Screen } from '../../App';
import { Match } from '../../data/matches';
import { api } from '../../services/api';

interface LiveMatchesListScreenProps {
  onNavigate: (screen: Screen) => void;
}

export function LiveMatchesListScreen({ onNavigate }: LiveMatchesListScreenProps) {
  const [liveMatches, setLiveMatches] = useState<Match[]>([]);
  const [loading, setLoading] = useState(true);
  const [lastUpdated, setLastUpdated] = useState<Date>(new Date());

  const fetchLiveMatches = async () => {
    try {
      const matches = await api.getMatches();
      setLiveMatches(matches.filter(m => m.status === 'live'));
      setLastUpdated(new Date());
    } catch (error) {
      console.error('Failed to load live matches:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLiveMatches();
    const interval = setInterval(fetchLiveMatches, 5000);
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-emerald-950 to-gray-900">
        <div className="animate-spin rounded-full h-16 w-16 border-t-2 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-emerald-950 to-gray-900">
      {/* Header */}
      <header className="bg-black/40 backdrop-blur-lg border-b border-white/10 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-20">
            <div className="flex items-center space-x-4">
              <button
                onClick={() => onNavigate({ type: 'home' })}
                className="p-2 hover:bg-white/10 rounded-xl transition-colors group"
              >
                <ArrowLeft className="h-6 w-6 text-white group-hover:text-emerald-400 transition-colors" />
              </button>
              <div>
                <span className="text-lg font-bold text-white flex items-center gap-2">
                  <Radio className="h-5 w-5 text-red-500 animate-pulse" />
                  Matchs en direct
                </span>
                <p className="text-xs text-white/60">
                  Mis à jour à {lastUpdated.toLocaleTimeString('fr-FR')}
                </p>
              </div>
            </div>
            <div className="flex items-center space-x-3">
              {liveMatches.length > 0 && (
                <span className="bg-red-500 text-white px-3 py-1 rounded-full text-sm font-bold">
                  {liveMatches.length} en cours
                </span>
              )}
              <button
                onClick={fetchLiveMatches}
                className="p-2 hover:bg-white/10 rounded-xl transition-colors group"
                title="Rafraîchir"
              >
                <RefreshCw className="h-5 w-5 text-white/70 group-hover:text-emerald-400 transition-colors" />
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {liveMatches.length === 0 ? (
          <div className="text-center py-20">
            <h2 className="text-2xl font-bold text-white mb-3">Aucun match en direct</h2>
            <p className="text-white/60 mb-8">Il n'y a pas de match en cours pour le moment.</p>
            <button
              onClick={() => onNavigate({ type: 'phases' })}
              className="px-6 py-3 bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl font-semibold transition-colors"
            >
              Voir le calendrier
            </button>
          </div>
        ) : (
          <div className="space-y-4">
            {liveMatches.map((match) => (
              <div
                key={match.id}
                className="bg-white/10 backdrop-blur-md rounded-2xl border border-white/20 overflow-hidden hover:border-emerald-400/50 transition-all cursor-pointer group"
                onClick={() => onNavigate({ type: 'liveEvents', matchId: match.id })}
              >
                {/* Live badge */}
                <div className="bg-red-500/20 border-b border-red-500/30 px-4 py-2 flex items-center justify-between">
                  <div className="flex items-center space-x-2">
                    <span className="relative flex h-2 w-2">
                      <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                      <span className="relative inline-flex rounded-full h-2 w-2 bg-red-500"></span>
                    </span>
                    <span className="text-red-300 text-xs font-bold uppercase">En direct</span>
                  </div>
                  <span className="text-white/70 text-xs">{match.phase}</span>
                </div>

                {/* Score */}
                <div className="p-6">
                  <div className="flex items-center justify-between">
                    {/* Team A */}
                    <div className="flex-1 text-center">
                      <div className="text-4xl mb-2">{match.teamA.flag}</div>
                      <p className="text-white font-semibold text-sm">{match.teamA.name}</p>
                    </div>

                    {/* Score */}
                    <div className="px-6">
                      <div className="flex items-center space-x-3">
                        <span className="text-4xl font-bold text-white">{match.teamA.score ?? 0}</span>
                        <span className="text-white/40 text-2xl">-</span>
                        <span className="text-4xl font-bold text-white">{match.teamB.score ?? 0}</span>
                      </div>
                      {match.currentMinute && (
                        <div className="text-center mt-2">
                          <span className="bg-emerald-500/30 text-emerald-300 px-3 py-1 rounded-full text-sm font-bold">
                            {match.currentMinute}'
                          </span>
                        </div>
                      )}
                    </div>

                    {/* Team B */}
                    <div className="flex-1 text-center">
                      <div className="text-4xl mb-2">{match.teamB.flag}</div>
                      <p className="text-white font-semibold text-sm">{match.teamB.name}</p>
                    </div>
                  </div>

                  {/* Events preview */}
                  {match.events && match.events.length > 0 && (
                    <div className="mt-4 pt-4 border-t border-white/10">
                      <div className="flex flex-wrap gap-2 justify-center">
                        {match.events.slice(-3).map((event, i) => (
                          <span key={i} className="text-xs text-white/60 bg-white/5 px-2 py-1 rounded-lg">
                            {event.minute}' {event.type === 'goal' ? '⚽' : event.type === 'yellow_card' ? '🟨' : event.type === 'red_card' ? '🟥' : '🔄'} {event.player}
                          </span>
                        ))}
                      </div>
                    </div>
                  )}

                  {/* CTA */}
                  <div className="mt-4 text-center">
                    <span className="text-emerald-400 text-sm font-semibold group-hover:underline">
                      Voir les événements en direct →
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
