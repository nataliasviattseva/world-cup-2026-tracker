import { useEffect, useState } from 'react';
import { Users, MapPin, Trophy } from 'lucide-react';
import { Screen } from '../../App';
import { Header } from '../Header';
import { api, Team } from '../../services/api';

interface WorldCupGroupsScreenProps {
  onNavigate: (screen: Screen) => void;
}

interface GroupData {
  letter: string;
  name: string;
  teams: Team[];
}

export function WorldCupGroupsScreen({ onNavigate }: WorldCupGroupsScreenProps) {
  const [groups, setGroups] = useState<GroupData[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchTeamsAndOrganizeGroups = async () => {
      try {
        // Fetch all groups and teams from database
        const [allGroups, allTeams] = await Promise.all([
          api.getGroups(),
          api.getTeams()
        ]);
        
        // Organize teams by group
        const organizedGroups: GroupData[] = allGroups
          .sort((a, b) => a.lettre.localeCompare(b.lettre))
          .map(group => {
            // Find teams that belong to this group
            const groupTeams = allTeams.filter(team => 
              team.group && team.group.letter === group.lettre
            );
            
            return {
              letter: group.lettre,
              name: group.nom,
              teams: groupTeams
            };
          });

        setGroups(organizedGroups);
      } catch (error) {
        console.error('Failed to load teams and groups:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTeamsAndOrganizeGroups();
  }, []);

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
        title="Groupes World Cup 2026"
        currentScreen="worldcupgroups"
      />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            Coupe du Monde FIFA 2026™
          </h1>
          <p className="text-lg text-gray-600">
            Découvrez les 12 groupes et leurs équipes participantes
          </p>
          <div className="flex items-center justify-center gap-2 mt-4 text-sm text-emerald-600">
            <MapPin className="h-4 w-4" />
            <span>Canada, États-Unis, Mexique</span>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {groups.map((group) => (
            <div
              key={group.letter}
              className="bg-white rounded-2xl shadow-xl border border-emerald-100 overflow-hidden hover:shadow-2xl transition-all duration-300"
            >
              {/* Group Header */}
              <div className="bg-gradient-to-r from-emerald-500 to-green-500 p-4 text-center">
                <div className="text-3xl font-bold text-white mb-1">
                  {group.letter}
                </div>
                <div className="text-emerald-100 text-sm">
                  {group.name}
                </div>
              </div>

              {/* Teams List */}
              <div className="p-4">
                <div className="flex items-center gap-2 mb-3 text-gray-600">
                  <Users className="h-4 w-4" />
                  <span className="text-sm font-medium">{group.teams.length} équipes</span>
                </div>

                <div className="space-y-3">
                  {group.teams.map((team, index) => (
                    <div
                      key={team.id}
                      onClick={() => onNavigate({ type: 'teamDetail', teamId: team.id })}
                      className={`flex items-center gap-3 p-3 rounded-lg cursor-pointer transition-all duration-200 ${
                        index < 2
                          ? 'bg-emerald-50 border border-emerald-200 hover:bg-emerald-100'
                          : 'bg-gray-50 border border-gray-200 hover:bg-gray-100'
                      }`}
                    >
                      <div className="text-2xl">{team.flag}</div>
                      <div className="flex-1">
                        <div className="font-medium text-gray-900 text-sm">
                          {team.name}
                        </div>
                        <div className="text-xs text-gray-500">
                          {team.code}
                        </div>
                      </div>
                      {index < 2 && (
                        <Trophy className="h-4 w-4 text-emerald-500" />
                      )}
                    </div>
                  ))}
                </div>

                {group.teams.length === 0 && (
                  <div className="text-center py-4 text-gray-500">
                    <Users className="h-8 w-8 mx-auto mb-2 opacity-50" />
                    <p className="text-sm">Équipes en cours de chargement...</p>
                  </div>
                )}
              </div>

              {/* Group Footer */}
              <div className="px-4 pb-4">
                <button 
                  onClick={() => onNavigate({ type: 'standings' })}
                  className="w-full bg-gray-100 hover:bg-emerald-100 text-gray-700 hover:text-emerald-700 px-3 py-2 rounded-lg text-sm font-medium transition-colors duration-200"
                >
                  Voir les classements
                </button>
              </div>
            </div>
          ))}
        </div>

        {groups.length === 0 && (
          <div className="text-center py-12">
            <Trophy className="h-16 w-16 mx-auto mb-4 text-gray-400" />
            <h3 className="text-lg font-semibold text-gray-600 mb-2">
              Groupes en cours de chargement...
            </h3>
            <p className="text-gray-500">
              Les données des groupes World Cup 2026 seront bientôt disponibles.
            </p>
          </div>
        )}

        {/* Tournament Info */}
        <div className="mt-12 bg-white rounded-2xl shadow-xl border border-emerald-100 p-6">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">
              À propos de la Coupe du Monde 2026
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 text-center">
              <div>
                <div className="text-3xl font-bold text-emerald-600">48</div>
                <div className="text-sm text-gray-600">Équipes participantes</div>
              </div>
              <div>
                <div className="text-3xl font-bold text-emerald-600">12</div>
                <div className="text-sm text-gray-600">Groupes</div>
              </div>
              <div>
                <div className="text-3xl font-bold text-emerald-600">3</div>
                <div className="text-sm text-gray-600">Pays hôtes</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}