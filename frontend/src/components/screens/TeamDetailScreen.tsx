import { useEffect, useState } from 'react';
import { Trophy, Users, Award, ArrowLeft } from 'lucide-react';
import { Screen } from '../../App';
import { Header } from '../Header';
import { api, Team } from '../../services/api';
import { Match } from '../../data/matches';

interface TeamDetailScreenProps {
  teamId: number;
  onNavigate: (screen: Screen) => void;
}

export function TeamDetailScreen({ teamId, onNavigate }: TeamDetailScreenProps) {
  const [team, setTeam] = useState<Team | null>(null);
  const [matches, setMatches] = useState<Match[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchTeamDetails = async () => {
      try {
        const teamData = await api.getTeamById(teamId);
        setTeam(teamData);

        // Fetch all matches and filter by team
        const allMatches = await api.getMatches();
        const teamMatches = allMatches.filter(
          m => m.teamA.id === teamId || m.teamB.id === teamId
        );
        setMatches(teamMatches);
      } catch (error) {
        console.error('Failed to load team details:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTeamDetails();
  }, [teamId]);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-emerald-600"></div>
      </div>
    );
  }

  if (!team) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <p className="text-xl text-gray-600">Équipe non trouvée</p>
          <button
            onClick={() => onNavigate({ type: 'teams' })}
            className="mt-4 text-emerald-600 hover:text-emerald-700"
          >
            Retour aux équipes
          </button>
        </div>
      </div>
    );
  }

  const wins = matches.filter(m => 
    (m.teamA.id === teamId && (m.teamA.score ?? 0) > (m.teamB.score ?? 0)) ||
    (m.teamB.id === teamId && (m.teamB.score ?? 0) > (m.teamA.score ?? 0))
  ).length;

  const draws = matches.filter(m => 
    m.status === 'finished' && m.teamA.score === m.teamB.score
  ).length;

  const losses = matches.filter(m => 
    (m.teamA.id === teamId && (m.teamA.score ?? 0) < (m.teamB.score ?? 0)) ||
    (m.teamB.id === teamId && (m.teamB.score ?? 0) < (m.teamA.score ?? 0))
  ).length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-white to-green-50">
      <Header
        onNavigate={onNavigate}
        title={team.name}
        currentScreen="teamDetail"
      />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Back Button */}
        <button
          onClick={() => onNavigate({ type: 'teams' })}
          className="flex items-center gap-2 text-emerald-600 hover:text-emerald-700 mb-6"
        >
          <ArrowLeft className="h-5 w-5" />
          <span>Retour aux équipes</span>
        </button>

        {/* Team Header */}
        <div className="bg-white rounded-2xl shadow-xl p-8 mb-8 border border-emerald-100">
          <div className="flex flex-col md:flex-row items-center md:items-start gap-8">
            <div className="text-8xl">{team.flag}</div>
            
            <div className="flex-1 text-center md:text-left">
              <h1 className="text-4xl font-bold text-gray-900 mb-4">{team.name}</h1>
              
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                <div className="bg-emerald-50 p-3 rounded-lg">
                  <div className="text-gray-600 mb-1">Classement FIFA</div>
                  <div className="text-2xl font-bold text-emerald-600">#{team.fifaRanking}</div>
                </div>
                
                <div className="bg-blue-50 p-3 rounded-lg">
                  <div className="text-gray-600 mb-1">Groupe</div>
                  <div className="text-2xl font-bold text-blue-600">{team.groupCode}</div>
                </div>
                
                <div className="bg-purple-50 p-3 rounded-lg">
                  <div className="text-gray-600 mb-1">Confédération</div>
                  <div className="text-xl font-bold text-purple-600">{team.confederation}</div>
                </div>
                
                <div className="bg-orange-50 p-3 rounded-lg">
                  <div className="text-gray-600 mb-1">Code</div>
                  <div className="text-2xl font-bold text-orange-600">{team.code}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Statistics */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-xl shadow-lg p-6 border border-green-100">
            <div className="flex items-center gap-3 mb-3">
              <div className="bg-green-100 p-3 rounded-lg">
                <Trophy className="h-6 w-6 text-green-600" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900">Victoires</h3>
            </div>
            <p className="text-4xl font-bold text-green-600">{wins}</p>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
            <div className="flex items-center gap-3 mb-3">
              <div className="bg-gray-100 p-3 rounded-lg">
                <Users className="h-6 w-6 text-gray-600" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900">Nuls</h3>
            </div>
            <p className="text-4xl font-bold text-gray-600">{draws}</p>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-6 border border-red-100">
            <div className="flex items-center gap-3 mb-3">
              <div className="bg-red-100 p-3 rounded-lg">
                <Award className="h-6 w-6 text-red-600" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900">Défaites</h3>
            </div>
            <p className="text-4xl font-bold text-red-600">{losses}</p>
          </div>
        </div>

        {/* Matches */}
        <div className="bg-white rounded-2xl shadow-xl p-8 border border-emerald-100">
          <h2 className="text-2xl font-bold text-gray-900 mb-6">Matchs</h2>
          
          {matches.length === 0 ? (
            <p className="text-gray-500 text-center py-8">Aucun match disponible</p>
          ) : (
            <div className="space-y-4">
              {matches.map((match) => (
                <button
                  key={match.id}
                  onClick={() => onNavigate({ type: 'detail', matchId: match.id })}
                  className="w-full bg-gray-50 hover:bg-gray-100 rounded-xl p-4 transition-colors"
                >
                  <div className="flex items-center justify-between">
                    <div className="flex-1 text-right">
                      <div className="font-semibold">{match.teamA.name}</div>
                      <div className="text-sm text-gray-500">{match.teamA.flag}</div>
                    </div>
                    
                    <div className="px-6">
                      <div className="text-2xl font-bold text-gray-900">
                        {match.teamA.score ?? '-'} - {match.teamB.score ?? '-'}
                      </div>
                      <div className="text-xs text-gray-500">{match.status === 'live' ? 'EN COURS' : match.status === 'finished' ? 'TERMINÉ' : 'À VENIR'}</div>
                    </div>
                    
                    <div className="flex-1 text-left">
                      <div className="font-semibold">{match.teamB.name}</div>
                      <div className="text-sm text-gray-500">{match.teamB.flag}</div>
                    </div>
                  </div>
                  
                  <div className="text-sm text-gray-500 mt-2 text-center">
                    {match.date} • {match.time} • {match.stadium}
                  </div>
                </button>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
