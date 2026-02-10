import { useEffect, useState } from 'react';
import { ArrowLeft, Calendar, MapPin, Clock, Play } from 'lucide-react';
import { Screen } from '../../App';
import { Match } from '../../data/matches';
import { api } from '../../services/api';
import { Header } from '../Header';

interface MatchDetailScreenProps {
  matchId: string;
  onNavigate: (screen: Screen) => void;
}

export function MatchDetailScreen({ matchId, onNavigate }: MatchDetailScreenProps) {
  const [match, setMatch] = useState<Match | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMatch = async () => {
      try {
        const data = await api.getMatchById(matchId);
        setMatch(data);
      } catch (error) {
        console.error("Failed to load match details:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMatch();
  }, [matchId]);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-emerald-600"></div>
      </div>
    );
  }

  if (!match) {
    return <div>Match non trouvé</div>;
  }

  const getStatusInfo = () => {
    switch (match.status) {
      case 'live':
        return {
          badge: (
            <div className="flex items-center space-x-2 bg-red-500 text-white px-4 py-2 rounded-full text-sm font-bold shadow-lg">
              <span className="relative flex h-2 w-2">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-white opacity-75"></span>
                <span className="relative inline-flex rounded-full h-2 w-2 bg-white"></span>
              </span>
              <span>EN DIRECT</span>
            </div>
          ),
          label: 'Match en cours',
          color: 'text-red-600',
        };
      case 'finished':
        return {
          badge: <div className="bg-gray-500 text-white px-4 py-2 rounded-full text-sm font-bold">TERMINÉ</div>,
          label: 'Match terminé',
          color: 'text-gray-600',
        };
      default:
        return {
          badge: <div className="bg-emerald-500 text-white px-4 py-2 rounded-full text-sm font-bold">À VENIR</div>,
          label: 'Match à venir',
          color: 'text-emerald-600',
        };
    }
  };

  const statusInfo = getStatusInfo();

  return (
    <div className="min-h-screen">
      {/* Header */}
      <Header 
        onNavigate={onNavigate} 
        title="Détails du match"
        subtitle={match.phase}
        showBack={true}
        backScreen={{ type: 'matches', phase: match.phase }}
        currentScreen="detail"
      />

      {/* Live Button in Header - Alternative position */}
      {match.status === 'live' && (
        <div className="bg-gradient-to-r from-red-500 to-red-600 py-3 px-4">
          <div className="max-w-7xl mx-auto flex justify-center">
            <button
              onClick={() => onNavigate({ type: 'live', matchId: match.id })}
              className="flex items-center space-x-2 bg-white text-red-600 px-6 py-2.5 rounded-xl font-bold hover:bg-red-50 transition-all shadow-lg"
            >
              <Play className="h-5 w-5" />
              <span>Voir le match en direct</span>
            </button>
          </div>
        </div>
      )}

      {/* Content */}
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Status */}
        <div className="text-center mb-8">
          <div className="inline-block mb-4">
            {statusInfo.badge}
          </div>
          {match.status === 'live' && match.currentMinute && (
            <p className="text-3xl font-bold text-red-600">{match.currentMinute}'</p>
          )}
        </div>

        {/* Score Card */}
        <div className="bg-white rounded-2xl shadow-2xl shadow-emerald-500/10 p-8 mb-8 border-2 border-emerald-100">
          <div className="flex items-center justify-between">
            {/* Team A */}
            <div className="flex-1 text-center">
              <div className="text-8xl mb-4">{match.teamA.flag}</div>
              <h2 className="text-3xl font-bold text-gray-900 mb-2">{match.teamA.name}</h2>
              {(match.status === 'live' || match.status === 'finished') && (
                <div className={`text-6xl font-bold ${match.teamA.score! > (match.teamB.score || 0) ? 'text-emerald-600' : 'text-gray-900'}`}>
                  {match.teamA.score}
                </div>
              )}
            </div>

            {/* Separator */}
            <div className="px-8">
              {match.status === 'upcoming' ? (
                <div className="text-5xl font-bold text-gray-300">VS</div>
              ) : (
                <div className="text-4xl font-bold text-gray-400">-</div>
              )}
            </div>

            {/* Team B */}
            <div className="flex-1 text-center">
              <div className="text-8xl mb-4">{match.teamB.flag}</div>
              <h2 className="text-3xl font-bold text-gray-900 mb-2">{match.teamB.name}</h2>
              {(match.status === 'live' || match.status === 'finished') && (
                <div className={`text-6xl font-bold ${match.teamB.score! > (match.teamA.score || 0) ? 'text-emerald-600' : 'text-gray-900'}`}>
                  {match.teamB.score}
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Match Information */}
        <div className="bg-white rounded-2xl shadow-lg shadow-emerald-500/5 p-8 mb-8 border border-emerald-100">
          <h3 className="text-2xl font-bold text-gray-900 mb-6">Informations du match</h3>
          <div className="space-y-4">
            <div className="flex items-start space-x-4 p-4 bg-emerald-50 rounded-xl">
              <div className="bg-emerald-500 p-3 rounded-xl">
                <Calendar className="h-6 w-6 text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-600 font-semibold">Date</p>
                <p className="text-lg font-bold text-gray-900">{match.date}</p>
              </div>
            </div>
            <div className="flex items-start space-x-4 p-4 bg-emerald-50 rounded-xl">
              <div className="bg-emerald-500 p-3 rounded-xl">
                <Clock className="h-6 w-6 text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-600 font-semibold">Heure</p>
                <p className="text-lg font-bold text-gray-900">{match.time}</p>
              </div>
            </div>
            <div className="flex items-start space-x-4 p-4 bg-emerald-50 rounded-xl">
              <div className="bg-emerald-500 p-3 rounded-xl">
                <MapPin className="h-6 w-6 text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-600 font-semibold">Stade</p>
                <p className="text-lg font-bold text-gray-900">{match.stadium}</p>
                <p className="text-sm text-gray-600">{match.city}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Events */}
        {match.events && match.events.length > 0 && (
          <div className="bg-white rounded-2xl shadow-lg shadow-emerald-500/5 p-8 border border-emerald-100">
            <h3 className="text-2xl font-bold text-gray-900 mb-6">Événements du match</h3>
            <div className="space-y-3">
              {match.events.map((event, index) => (
                <div
                  key={index}
                  className="flex items-center space-x-4 p-4 bg-gray-50 rounded-xl hover:bg-emerald-50 transition-colors"
                >
                  <div className="bg-emerald-600 text-white rounded-full w-12 h-12 flex items-center justify-center font-bold">
                    {event.minute}'
                  </div>
                  <div className="flex-1">
                    <div className="flex items-center space-x-2 mb-1">
                      {event.type === 'goal' && <span className="text-2xl">⚽</span>}
                      {event.type === 'yellow_card' && <span className="text-2xl">🟨</span>}
                      {event.type === 'red_card' && <span className="text-2xl">🟥</span>}
                      {event.type === 'substitution' && <span className="text-2xl">🔄</span>}
                      <p className="font-bold text-gray-900">{event.player}</p>
                    </div>
                    <p className="text-sm text-gray-600">{event.description}</p>
                  </div>
                  <div className="text-3xl">
                    {event.team === 'A' ? match.teamA.flag : match.teamB.flag}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}