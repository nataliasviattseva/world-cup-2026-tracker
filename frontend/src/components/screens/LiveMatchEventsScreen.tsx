import { useEffect, useState, useRef } from 'react';
import { ArrowLeft, Radio, MessageSquare, RefreshCw } from 'lucide-react';
import { Screen } from '../../App';
import { Match } from '../../data/matches';
import { api } from '../../services/api';

interface LiveMatchEventsScreenProps {
  matchId: string | number;
  onNavigate: (screen: Screen) => void;
}

export function LiveMatchEventsScreen({ matchId, onNavigate }: LiveMatchEventsScreenProps) {
  const [match, setMatch] = useState<Match | null>(null);
  const [loading, setLoading] = useState(true);
  const [lastUpdated, setLastUpdated] = useState<Date>(new Date());
  const eventsEndRef = useRef<HTMLDivElement>(null);
  const [autoScroll, setAutoScroll] = useState(true);

  const fetchMatch = async () => {
    try {
      const data = await api.getMatchById(matchId);
      setMatch(data);
      setLastUpdated(new Date());
    } catch (error) {
      console.error('Failed to load match:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMatch();
    const interval = setInterval(fetchMatch, 5000);
    return () => clearInterval(interval);
  }, [matchId]);

  // Auto-scroll to latest event
  useEffect(() => {
    if (autoScroll && eventsEndRef.current) {
      eventsEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [match?.events?.length, autoScroll]);

  const getEventIcon = (type: string) => {
    switch (type) {
      case 'goal': return '⚽';
      case 'yellow_card': return '🟨';
      case 'red_card': return '🟥';
      case 'substitution': return '🔄';
      default: return '📝';
    }
  };

  const getEventLabel = (type: string) => {
    switch (type) {
      case 'goal': return 'But';
      case 'yellow_card': return 'Carton jaune';
      case 'red_card': return 'Carton rouge';
      case 'substitution': return 'Remplacement';
      default: return 'Événement';
    }
  };

  const getEventColor = (type: string) => {
    switch (type) {
      case 'goal': return 'border-yellow-400 bg-yellow-500/20';
      case 'yellow_card': return 'border-yellow-300 bg-yellow-400/10';
      case 'red_card': return 'border-red-400 bg-red-500/20';
      case 'substitution': return 'border-blue-400 bg-blue-500/10';
      default: return 'border-white/20 bg-white/5';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-emerald-950 to-gray-900">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  if (!match) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
        Match non trouvé
      </div>
    );
  }

  const sortedEvents = match.events
    ? [...match.events].sort((a, b) => a.minute - b.minute)
    : [];

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-emerald-950 to-gray-900 flex flex-col">
      {/* Sticky Header */}
      <header className="bg-black/60 backdrop-blur-lg border-b border-white/10 sticky top-0 z-50">
        <div className="max-w-3xl mx-auto px-4 sm:px-6">
          {/* Nav row */}
          <div className="flex items-center justify-between h-14">
            <button
              onClick={() => onNavigate({ type: 'liveMatches' })}
              className="p-2 hover:bg-white/10 rounded-xl transition-colors group"
            >
              <ArrowLeft className="h-5 w-5 text-white group-hover:text-emerald-400" />
            </button>
            <div className="flex items-center space-x-2">
              <Radio className="h-4 w-4 text-red-500 animate-pulse" />
              <span className="text-white text-sm font-bold">Événements en direct</span>
            </div>
            <button
              onClick={fetchMatch}
              className="p-2 hover:bg-white/10 rounded-xl transition-colors group"
            >
              <RefreshCw className="h-4 w-4 text-white/70 group-hover:text-emerald-400" />
            </button>
          </div>

          {/* Compact scoreboard */}
          <div className="flex items-center justify-center py-3 space-x-4">
            <div className="flex items-center space-x-2">
              <span className="text-2xl">{match.teamA.flag}</span>
              <span className="text-white font-semibold text-sm">{match.teamA.name}</span>
            </div>
            <div className="bg-white/10 rounded-xl px-4 py-2 flex items-center space-x-2">
              <span className="text-2xl font-bold text-white">{match.teamA.score ?? 0}</span>
              <span className="text-white/40">-</span>
              <span className="text-2xl font-bold text-white">{match.teamB.score ?? 0}</span>
            </div>
            <div className="flex items-center space-x-2">
              <span className="text-white font-semibold text-sm">{match.teamB.name}</span>
              <span className="text-2xl">{match.teamB.flag}</span>
            </div>
          </div>

          {/* Match info */}
          <div className="flex items-center justify-center pb-3 space-x-4 text-xs text-white/50">
            <span>{match.phase}</span>
            <span>•</span>
            <span>{match.stadium}</span>
            {match.currentMinute && (
              <>
                <span>•</span>
                <span className="text-emerald-400 font-bold">{match.currentMinute}'</span>
              </>
            )}
          </div>
        </div>
      </header>

      {/* Events timeline */}
      <div className="flex-1 overflow-y-auto">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 py-6">
          {/* Match start marker */}
          <div className="flex items-center space-x-3 mb-6">
            <div className="flex-1 h-px bg-white/10"></div>
            <span className="text-white/40 text-xs font-semibold uppercase tracking-wider">Coup d'envoi</span>
            <div className="flex-1 h-px bg-white/10"></div>
          </div>

          {sortedEvents.length === 0 ? (
            <div className="text-center py-16">
              <MessageSquare className="h-12 w-12 text-white/20 mx-auto mb-4" />
              <p className="text-white/50 text-lg">Aucun événement pour le moment</p>
              <p className="text-white/30 text-sm mt-2">Les événements apparaîtront ici en temps réel</p>
            </div>
          ) : (
            <div className="space-y-3">
              {sortedEvents.map((event, index) => (
                <div
                  key={index}
                  className={`flex items-start space-x-4 p-4 rounded-xl border-l-4 transition-all ${getEventColor(event.type)} ${
                    index === sortedEvents.length - 1 ? 'animate-pulse-once ring-1 ring-emerald-400/30' : ''
                  }`}
                >
                  {/* Minute */}
                  <div className="flex-shrink-0 bg-emerald-600/80 text-white rounded-lg w-12 h-12 flex items-center justify-center font-bold text-sm shadow-lg">
                    {event.minute}'
                  </div>

                  {/* Event content */}
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center space-x-2 mb-1">
                      <span className="text-2xl">{getEventIcon(event.type)}</span>
                      <span className="text-white font-bold">{event.player}</span>
                    </div>
                    <div className="flex items-center space-x-2">
                      <span className="text-xs font-semibold text-white/50 uppercase tracking-wider">
                        {getEventLabel(event.type)}
                      </span>
                      {event.description && (
                        <>
                          <span className="text-white/30">•</span>
                          <span className="text-sm text-white/60">{event.description}</span>
                        </>
                      )}
                    </div>
                  </div>

                  {/* Team flag */}
                  <div className="flex-shrink-0 text-3xl">
                    {event.team === 'A' ? match.teamA.flag : match.teamB.flag}
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* Auto-scroll anchor */}
          <div ref={eventsEndRef} />

          {/* Half-time / Full-time markers would appear here based on match state */}
          {match.status === 'live' && sortedEvents.length > 0 && (
            <div className="mt-6 flex items-center space-x-3">
              <div className="flex-1 h-px bg-white/10"></div>
              <div className="flex items-center space-x-2 text-white/40 text-xs">
                <span className="relative flex h-2 w-2">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
                </span>
                <span>En attente du prochain événement...</span>
              </div>
              <div className="flex-1 h-px bg-white/10"></div>
            </div>
          )}

          {match.status === 'finished' && (
            <div className="mt-6 flex items-center space-x-3">
              <div className="flex-1 h-px bg-white/20"></div>
              <span className="text-white/50 text-xs font-semibold uppercase tracking-wider">Fin du match</span>
              <div className="flex-1 h-px bg-white/20"></div>
            </div>
          )}
        </div>
      </div>

      {/* Bottom bar */}
      <div className="bg-black/40 backdrop-blur-lg border-t border-white/10 sticky bottom-0">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 py-3 flex items-center justify-between">
          <div className="flex items-center space-x-2 text-white/50 text-xs">
            <span>Dernière mise à jour: {lastUpdated.toLocaleTimeString('fr-FR')}</span>
          </div>
          <div className="flex items-center space-x-3">
            <button
              onClick={() => setAutoScroll(!autoScroll)}
              className={`text-xs px-3 py-1.5 rounded-lg transition-colors ${
                autoScroll
                  ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30'
                  : 'bg-white/5 text-white/50 border border-white/10'
              }`}
            >
              {autoScroll ? '⬇ Auto-scroll ON' : '⬇ Auto-scroll OFF'}
            </button>
            <button
              onClick={() => onNavigate({ type: 'live', matchId: match.id })}
              className="text-xs px-3 py-1.5 rounded-lg bg-white/10 text-white/70 border border-white/10 hover:bg-white/20 transition-colors"
            >
              Vue complète →
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
