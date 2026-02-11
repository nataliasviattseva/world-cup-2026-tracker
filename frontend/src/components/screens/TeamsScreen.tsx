import { useEffect, useState } from 'react';
import { Users, Search, MapPin } from 'lucide-react';
import { Screen } from '../../App';
import { Header } from '../Header';
import { api, Team } from '../../services/api';

interface TeamsScreenProps {
  onNavigate: (screen: Screen) => void;
}

export function TeamsScreen({ onNavigate }: TeamsScreenProps) {
  const [teams, setTeams] = useState<Team[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedGroup, setSelectedGroup] = useState<string>('all');

  useEffect(() => {
    const fetchTeams = async () => {
      try {
        const data = await api.getTeams();
        setTeams(data);
      } catch (error) {
        console.error('Failed to load teams:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTeams();
  }, []);

  const filteredTeams = teams.filter(team => {
    const matchesSearch = team.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         team.code.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesGroup = selectedGroup === 'all' || team.groupCode === selectedGroup;
    return matchesSearch && matchesGroup;
  });

  const groups = Array.from(new Set(teams.map(t => t.groupCode).filter(Boolean))).sort();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-emerald-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-white to-green-50">
      <Header
        onNavigate={onNavigate}
        title="Équipes"
        currentScreen="teams"
      />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            Toutes les équipes
          </h1>
          <p className="text-lg text-gray-600">
            {teams.length} équipes participantes à la Coupe du Monde 2026
          </p>
        </div>

        {/* Filters */}
        <div className="mb-8 space-y-4">
          <div className="relative">
            <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5" />
            <input
              type="text"
              placeholder="Rechercher une équipe..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-12 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-transparent"
            />
          </div>

          <div className="flex items-center gap-2 overflow-x-auto pb-2">
            <button
              onClick={() => setSelectedGroup('all')}
              className={`px-4 py-2 rounded-lg font-semibold whitespace-nowrap transition-colors ${
                selectedGroup === 'all'
                  ? 'bg-emerald-600 text-white'
                  : 'bg-white text-gray-700 hover:bg-gray-100'
              }`}
            >
              Tous les groupes
            </button>
            {groups.map(group => (
              <button
                key={group}
                onClick={() => setSelectedGroup(group)}
                className={`px-4 py-2 rounded-lg font-semibold whitespace-nowrap transition-colors ${
                  selectedGroup === group
                    ? 'bg-emerald-600 text-white'
                    : 'bg-white text-gray-700 hover:bg-gray-100'
                }`}
              >
                Groupe {group}
              </button>
            ))}
          </div>
        </div>

        {/* Teams Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {filteredTeams.map((team) => (
            <button
              key={team.id}
              onClick={() => onNavigate({ type: 'teamDetail', teamId: team.id })}
              className="group bg-white rounded-xl shadow-lg hover:shadow-2xl transition-all duration-300 overflow-hidden border border-emerald-100 hover:-translate-y-1"
            >
              <div className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <div className="text-5xl">{team.flag}</div>
                  <div className="bg-emerald-50 text-emerald-700 px-3 py-1 rounded-full text-sm font-bold">
                    #{team.fifaRanking}
                  </div>
                </div>

                <h3 className="text-xl font-bold text-gray-900 mb-2 group-hover:text-emerald-600 transition-colors">
                  {team.name}
                </h3>

                <div className="space-y-2 text-sm text-gray-600">
                  <div className="flex items-center gap-2">
                    <Users className="h-4 w-4" />
                    <span>Groupe {team.groupCode}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <MapPin className="h-4 w-4" />
                    <span>{team.confederation}</span>
                  </div>
                </div>
              </div>

              <div className="bg-gradient-to-r from-emerald-500 to-green-500 p-3 text-center">
                <span className="text-white font-semibold text-sm">
                  Voir les détails →
                </span>
              </div>
            </button>
          ))}
        </div>

        {filteredTeams.length === 0 && (
          <div className="text-center py-12">
            <Users className="h-16 w-16 text-gray-300 mx-auto mb-4" />
            <p className="text-gray-500 text-lg">Aucune équipe trouvée</p>
          </div>
        )}
      </div>
    </div>
  );
}
