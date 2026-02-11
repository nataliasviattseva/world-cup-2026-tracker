import { useEffect, useState } from 'react';
import { Trophy, TrendingUp } from 'lucide-react';
import { Screen } from '../../App';
import { Header } from '../Header';
import { api, Standing, Group } from '../../services/api';

interface StandingsScreenProps {
  onNavigate: (screen: Screen) => void;
}

export function StandingsScreen({ onNavigate }: StandingsScreenProps) {
  const [groups, setGroups] = useState<Group[]>([]);
  const [standingsByGroup, setStandingsByGroup] = useState<Map<string, Standing[]>>(new Map());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStandings = async () => {
      try {
        const groupsData = await api.getGroups();
        setGroups(groupsData);

        // Fetch standings for each group
        const standingsMap = new Map<string, Standing[]>();
        for (const group of groupsData) {
          try {
            const standings = await api.getStandingsByGroup(group.nom);
            // Sort by position
            standings.sort((a, b) => a.position - b.position);
            standingsMap.set(group.nom, standings);
          } catch (error) {
            console.error(`Failed to load standings for group ${group.nom}:`, error);
            standingsMap.set(group.nom, []);
          }
        }
        setStandingsByGroup(standingsMap);
      } catch (error) {
        console.error('Failed to load groups:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStandings();
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
        title="Classements"
        currentScreen="standings"
      />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            Classements des groupes
          </h1>
          <p className="text-lg text-gray-600">
            Consultez le classement de chaque groupe de la phase de groupes
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {groups.map((group) => {
            const standings = standingsByGroup.get(group.nom) || [];
            
            return (
              <div key={group.id} className="bg-white rounded-2xl shadow-xl border border-emerald-100 overflow-hidden">
                <div className="bg-gradient-to-r from-emerald-500 to-green-500 p-6">
                  <h2 className="text-2xl font-bold text-white">
                    Groupe {group.lettre}
                  </h2>
                  <p className="text-emerald-50">{group.nom}</p>
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-gray-50 border-b-2 border-gray-200">
                      <tr>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Pos</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Équipe</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">J</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">V</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">N</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">D</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">BP</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">BC</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">Diff</th>
                        <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">Pts</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                      {standings.length === 0 ? (
                        <tr>
                          <td colSpan={10} className="px-4 py-8 text-center text-gray-500">
                            Aucune donnée de classement disponible
                          </td>
                        </tr>
                      ) : (
                        standings.map((standing, index) => {
                          const isQualified = index < 2; // Top 2 teams qualify
                          
                          return (
                            <tr
                              key={standing.id}
                              className={`hover:bg-gray-50 transition-colors ${
                                isQualified ? 'bg-green-50/30' : ''
                              }`}
                            >
                              <td className="px-4 py-4">
                                <div className="flex items-center gap-2">
                                  <span className={`font-bold ${
                                    isQualified ? 'text-green-600' : 'text-gray-700'
                                  }`}>
                                    {standing.position}
                                  </span>
                                  {isQualified && (
                                    <TrendingUp className="h-4 w-4 text-green-600" />
                                  )}
                                </div>
                              </td>
                              <td className="px-4 py-4">
                                <button
                                  onClick={() => onNavigate({ type: 'teamDetail', teamId: standing.equipe.id })}
                                  className="flex items-center gap-3 hover:text-emerald-600 transition-colors"
                                >
                                  <span className="text-2xl">{standing.equipe.drapeauUrl}</span>
                                  <div>
                                    <div className="font-semibold text-gray-900">{standing.equipe.nom}</div>
                                    <div className="text-xs text-gray-500">{standing.equipe.codePays}</div>
                                  </div>
                                </button>
                              </td>
                              <td className="px-4 py-4 text-center text-gray-700">{standing.matchsJoues}</td>
                              <td className="px-4 py-4 text-center text-green-600 font-semibold">{standing.victoires}</td>
                              <td className="px-4 py-4 text-center text-gray-600">{standing.nuls}</td>
                              <td className="px-4 py-4 text-center text-red-600 font-semibold">{standing.defaites}</td>
                              <td className="px-4 py-4 text-center text-gray-700">{standing.butsPour}</td>
                              <td className="px-4 py-4 text-center text-gray-700">{standing.butsContre}</td>
                              <td className={`px-4 py-4 text-center font-semibold ${
                                standing.differenceButs > 0 ? 'text-green-600' :
                                standing.differenceButs < 0 ? 'text-red-600' :
                                'text-gray-600'
                              }`}>
                                {standing.differenceButs > 0 ? '+' : ''}{standing.differenceButs}
                              </td>
                              <td className="px-4 py-4 text-center">
                                <span className="inline-flex items-center justify-center bg-emerald-600 text-white font-bold rounded-full w-8 h-8">
                                  {standing.points}
                                </span>
                              </td>
                            </tr>
                          );
                        })
                      )}
                    </tbody>
                  </table>
                </div>

                {standings.length > 0 && (
                  <div className="bg-gray-50 px-6 py-3 border-t border-gray-200">
                    <div className="flex items-center gap-2 text-sm text-gray-600">
                      <TrendingUp className="h-4 w-4 text-green-600" />
                      <span>Les 2 premières équipes se qualifient pour la phase suivante</span>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>

        {groups.length === 0 && (
          <div className="text-center py-12">
            <Trophy className="h-16 w-16 text-gray-300 mx-auto mb-4" />
            <p className="text-gray-500 text-lg">Aucun groupe disponible</p>
          </div>
        )}
      </div>
    </div>
  );
}
