import { useEffect, useState } from 'react';
import { Calendar, Play, Users, Award } from 'lucide-react';
import { Screen } from '../../App';
import { ImageWithFallback } from '../figma/ImageWithFallback';
import { Header } from '../Header';
import { Match } from '../../data/matches';
import { api } from '../../services/api';

interface HomeScreenProps {
  onNavigate: (screen: Screen) => void;
}

export function HomeScreen({ onNavigate }: HomeScreenProps) {
  const [liveMatches, setLiveMatches] = useState<Match[]>([]);
  const [upcomingMatches, setUpcomingMatches] = useState<Match[]>([]);
  const [recentMatches, setRecentMatches] = useState<Match[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const matches = await api.getMatches();

        // Filter matches locally or use specific endpoints if preferred
        setLiveMatches(matches.filter(m => m.status === 'live'));
        setUpcomingMatches(matches.filter(m => m.status === 'upcoming').slice(0, 3));
        setRecentMatches(matches.filter(m => m.status === 'finished').slice(0, 2));
      } catch (error) {
        console.error("Failed to load matches:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-emerald-50 to-green-50">
        <div className="animate-spin rounded-full h-16 w-16 border-t-2 border-b-2 border-emerald-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen">
      {/* Header */}
      <Header onNavigate={onNavigate} currentScreen="home" />

      {/* Hero Section */}
      <div className="relative bg-gradient-to-br from-emerald-600 via-green-600 to-teal-700 text-white overflow-hidden">
        <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImdyaWQiIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTSAxMCAwIEwgMCAwIDAgMTAiIGZpbGw9Im5vbmUiIHN0cm9rZT0id2hpdGUiIHN0cm9rZS13aWR0aD0iMC41IiBvcGFjaXR5PSIwLjEiLz48L3BhdHRlcm4+PC9kZWZzPjxyZWN0IHdpZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiIGZpbGw9InVybCgjZ3JpZCkiLz48L3N2Zz4=')] opacity-30"></div>
        <div className="absolute inset-0 opacity-20">
          <ImageWithFallback
            src="https://images.unsplash.com/photo-1705593973313-75de7bf95b56?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b3JsZCUyMGN1cCUyMHN0YWRpdW0lMjBjcm93ZHxlbnwxfHx8fDE3NzA3MTI5MTV8MA&ixlib=rb-4.1.0&q=80&w=1080"
            alt="Stadium background"
            className="w-full h-full object-cover"
          />
        </div>
        <div className="absolute inset-0 bg-gradient-to-t from-emerald-900/50 via-transparent to-transparent"></div>
        
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 text-center">
          <div className="inline-block mb-4 px-4 py-1.5 bg-white/20 backdrop-blur-sm rounded-full text-sm font-semibold">
            🏆 The Biggest World Cup Ever
          </div>
          <h1 className="text-5xl sm:text-6xl font-bold mb-4 tracking-tight">
            FIFA World Cup 2026
          </h1>
          <p className="text-xl mb-2 font-light">United 2026</p>
          <p className="text-base mb-10 text-emerald-100">🇺🇸 USA · 🇨🇦 Canada · 🇲🇽 Mexico</p>
          
          {/* Featured Matches */}
          <div className="max-w-4xl mx-auto space-y-4 mb-8">
            {/* Live Match */}
            {liveMatches.length > 0 && (
              <div className="bg-white/10 backdrop-blur-md rounded-2xl p-6 border border-white/20 shadow-xl">
                <div className="flex items-center justify-center space-x-2 mb-4">
                  <span className="relative flex h-3 w-3">
                    <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                    <span className="relative inline-flex rounded-full h-3 w-3 bg-red-500"></span>
                  </span>
                  <span className="text-red-400 font-bold text-sm uppercase">En Direct</span>
                  <span className="text-white font-bold text-lg">{liveMatches[0].currentMinute}'</span>
                </div>
                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-3 flex-1">
                    <span className="text-4xl">{liveMatches[0].teamA.flag}</span>
                    <span className="text-xl font-bold text-white">{liveMatches[0].teamA.name}</span>
                  </div>
                  <div className="px-6">
                    <div className="text-4xl font-bold text-white">
                      {liveMatches[0].teamA.score} - {liveMatches[0].teamB.score}
                    </div>
                  </div>
                  <div className="flex items-center justify-end space-x-3 flex-1">
                    <span className="text-xl font-bold text-white">{liveMatches[0].teamB.name}</span>
                    <span className="text-4xl">{liveMatches[0].teamB.flag}</span>
                  </div>
                </div>
              </div>
            )}

            {/* Recent Matches */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {recentMatches.map((match) => (
                <div key={match.id} className="bg-white/10 backdrop-blur-md rounded-xl p-4 border border-white/20">
                  <div className="text-xs text-emerald-200 mb-2 font-semibold">Terminé</div>
                  <div className="flex items-center justify-between text-sm">
                    <div className="flex items-center space-x-2">
                      <span className="text-2xl">{match.teamA.flag}</span>
                      <span className="text-white font-semibold">{match.teamA.name}</span>
                    </div>
                    <div className="text-xl font-bold text-white px-4">
                      {match.teamA.score} - {match.teamB.score}
                    </div>
                    <div className="flex items-center space-x-2">
                      <span className="text-white font-semibold">{match.teamB.name}</span>
                      <span className="text-2xl">{match.teamB.flag}</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
          
          <div className="flex flex-col sm:flex-row justify-center gap-4 text-base font-semibold">
            <div className="bg-white/10 backdrop-blur-md px-6 py-3 rounded-xl border border-white/20">
              ⚽ Opening: 11 juin 2026
            </div>
            <div className="bg-white/10 backdrop-blur-md px-6 py-3 rounded-xl border border-white/20">
              🏆 Final: 19 juillet 2026
            </div>
          </div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Prochains matchs */}
        <div className="mb-12">
          <h2 className="text-3xl font-bold bg-gradient-to-r from-emerald-600 to-green-600 bg-clip-text text-transparent mb-6">
            Prochains matchs
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {upcomingMatches.map((match) => (
              <button
                key={match.id}
                onClick={() => onNavigate({ type: 'detail', matchId: match.id })}
                className="bg-white rounded-2xl shadow-lg shadow-emerald-500/5 p-5 hover:shadow-xl hover:shadow-emerald-500/10 transition-all border border-emerald-100 hover:-translate-y-1 text-left group"
              >
                <div className="flex items-center justify-between mb-3">
                  <span className="text-xs font-semibold text-emerald-600 bg-emerald-50 px-3 py-1 rounded-full">
                    {match.phase}
                  </span>
                  <span className="text-xs text-gray-500">{match.date}</span>
                </div>
                <div className="space-y-2 mb-3">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                      <span className="text-2xl">{match.teamA.flag}</span>
                      <span className="font-semibold text-gray-900">{match.teamA.name}</span>
                    </div>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                      <span className="text-2xl">{match.teamB.flag}</span>
                      <span className="font-semibold text-gray-900">{match.teamB.name}</span>
                    </div>
                  </div>
                </div>
                <div className="text-xs text-gray-500 pt-2 border-t border-gray-100">
                  {match.time} · {match.city}
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* Navigation Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
          <button
            onClick={() => onNavigate({ type: 'phases' })}
            className="group bg-white rounded-2xl shadow-lg shadow-emerald-500/5 p-8 hover:shadow-2xl hover:shadow-emerald-500/10 transition-all duration-300 border border-emerald-100 hover:-translate-y-1 text-left"
          >
            <div className="flex items-center justify-between mb-4">
              <div className="bg-gradient-to-br from-emerald-500 to-green-600 p-4 rounded-2xl shadow-lg shadow-emerald-500/30">
                <Calendar className="h-8 w-8 text-white" />
              </div>
              <div className="text-emerald-600 group-hover:translate-x-2 transition-transform">
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
            <h3 className="text-xl font-bold text-gray-900 mb-2">Matchs</h3>
            <p className="text-gray-600 text-sm">Tous les matchs par phase</p>
          </button>

          <button
            onClick={() => onNavigate({ type: 'teams' })}
            className="group bg-white rounded-2xl shadow-lg shadow-blue-500/5 p-8 hover:shadow-2xl hover:shadow-blue-500/10 transition-all duration-300 border border-blue-100 hover:-translate-y-1 text-left"
          >
            <div className="flex items-center justify-between mb-4">
              <div className="bg-gradient-to-br from-blue-500 to-blue-600 p-4 rounded-2xl shadow-lg shadow-blue-500/30">
                <Users className="h-8 w-8 text-white" />
              </div>
              <div className="text-blue-600 group-hover:translate-x-2 transition-transform">
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
            <h3 className="text-xl font-bold text-gray-900 mb-2">Équipes</h3>
            <p className="text-gray-600 text-sm">Voir toutes les équipes</p>
          </button>

          <button
            onClick={() => onNavigate({ type: 'standings' })}
            className="group bg-white rounded-2xl shadow-lg shadow-purple-500/5 p-8 hover:shadow-2xl hover:shadow-purple-500/10 transition-all duration-300 border border-purple-100 hover:-translate-y-1 text-left"
          >
            <div className="flex items-center justify-between mb-4">
              <div className="bg-gradient-to-br from-purple-500 to-purple-600 p-4 rounded-2xl shadow-lg shadow-purple-500/30">
                <Award className="h-8 w-8 text-white" />
              </div>
              <div className="text-purple-600 group-hover:translate-x-2 transition-transform">
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
            <h3 className="text-xl font-bold text-gray-900 mb-2">Classements</h3>
            <p className="text-gray-600 text-sm">Classement des groupes</p>
          </button>

          <button
            onClick={() => {
              if (liveMatches.length > 0) {
                onNavigate({ type: 'live', matchId: liveMatches[0].id });
              }
            }}
            disabled={liveMatches.length === 0}
            className={`group rounded-2xl shadow-lg p-8 hover:shadow-2xl transition-all duration-300 hover:-translate-y-1 text-left ${
              liveMatches.length > 0
                ? 'bg-gradient-to-br from-emerald-500 to-green-600 shadow-emerald-500/20 hover:shadow-emerald-500/30 text-white'
                : 'bg-gray-100 shadow-gray-500/5 text-gray-400 cursor-not-allowed'
            }`}
          >
            <div className="flex items-center justify-between mb-4">
              <div className={`p-4 rounded-2xl ${
                liveMatches.length > 0
                  ? 'bg-white/20 backdrop-blur-sm border border-white/30'
                  : 'bg-gray-200'
              }`}>
                <Play className="h-8 w-8" />
              </div>
              {liveMatches.length > 0 && (
                <div className="flex items-center space-x-2">
                  <span className="relative flex h-3 w-3">
                    <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                    <span className="relative inline-flex rounded-full h-3 w-3 bg-red-500"></span>
                  </span>
                  <span className="text-sm font-semibold">DIRECT</span>
                </div>
              )}
            </div>
            <h3 className="text-xl font-bold mb-2">Match en cours</h3>
            <p className={`text-sm ${liveMatches.length > 0 ? 'text-emerald-100' : 'text-gray-500'}`}>
              {liveMatches.length > 0 ? 'Suivre le match' : 'Aucun match en cours'}
            </p>
          </button>
        </div>

        {/* Stats */}
        <div className="bg-white rounded-2xl shadow-lg shadow-emerald-500/5 p-8 border border-emerald-100">
          <h2 className="text-2xl font-bold bg-gradient-to-r from-emerald-600 to-green-600 bg-clip-text text-transparent mb-6 text-center">
            Informations du tournoi
          </h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            <div className="text-center">
              <div className="bg-gradient-to-br from-emerald-500 to-green-600 text-white rounded-2xl p-6 mb-3 shadow-lg shadow-emerald-500/20">
                <p className="text-4xl font-bold">48</p>
              </div>
              <p className="text-sm font-semibold text-gray-600">Équipes</p>
            </div>
            <div className="text-center">
              <div className="bg-gradient-to-br from-emerald-500 to-green-600 text-white rounded-2xl p-6 mb-3 shadow-lg shadow-emerald-500/20">
                <p className="text-4xl font-bold">104</p>
              </div>
              <p className="text-sm font-semibold text-gray-600">Matchs</p>
            </div>
            <div className="text-center">
              <div className="bg-gradient-to-br from-emerald-500 to-green-600 text-white rounded-2xl p-6 mb-3 shadow-lg shadow-emerald-500/20">
                <p className="text-4xl font-bold">16</p>
              </div>
              <p className="text-sm font-semibold text-gray-600">Villes</p>
            </div>
            <div className="text-center">
              <div className="bg-gradient-to-br from-emerald-500 to-green-600 text-white rounded-2xl p-6 mb-3 shadow-lg shadow-emerald-500/20">
                <p className="text-4xl font-bold">39</p>
              </div>
              <p className="text-sm font-semibold text-gray-600">Jours</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}