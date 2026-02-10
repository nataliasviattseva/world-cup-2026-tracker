import { useEffect, useState } from 'react';
import { Trophy, Users, Target, Award, Medal } from 'lucide-react';
import { Screen } from '../../App';
import { Header } from '../Header';
import { ImageWithFallback } from '../figma/ImageWithFallback';
import { api } from '../../services/api';

interface PhasesScreenProps {
  onNavigate: (screen: Screen) => void;
}

// Static definition for UI presentation (icons, colors, images)
// We will merge this with dynamic data (match counts)
const PHASE_DEFINITIONS: Record<string, any> = {
  'PHASE_GROUPES': {
    icon: Users,
    color: 'from-blue-500 to-blue-600',
    image: 'https://images.unsplash.com/photo-1765046804547-06375f9a707b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxzb2NjZXIlMjB0ZWFtJTIwZ3JvdXAlMjBwbGF5ZXJzfGVufDF8fHx8MTc3MDcxMjkxNnww&ixlib=rb-4.1.0&q=80&w=1080',
    displayName: 'Phase de groupes',
    description: '48 équipes · 12 groupes'
  },
  'HUITIEMES_FINALE': {
    icon: Target,
    color: 'from-emerald-500 to-green-600',
    image: 'https://images.unsplash.com/photo-1625187538367-6a8483a79cc2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxzb2NjZXIlMjBiYWxsJTIwZ3Jhc3MlMjBmaWVsZHxlbnwxfHx8fDE3NzA2MTI5MDl8MA&ixlib=rb-4.1.0&q=80&w=1080',
    displayName: 'Huitièmes de finale',
    description: '16 équipes qualifiées'
  },
  'QUARTS_FINALE': {
    icon: Award,
    color: 'from-orange-500 to-orange-600',
    image: 'https://images.unsplash.com/photo-1757031298215-5041017293ae?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxzb2NjZXIlMjBwbGF5ZXJzJTIwY2VsZWJyYXRpbmclMjBnb2FsfGVufDF8fHx8MTc3MDcxMjkxNnww&ixlib=rb-4.1.0&q=80&w=1080',
    displayName: 'Quarts de finale',
    description: '8 équipes en compétition'
  },
  'DEMI_FINALES': {
    icon: Medal,
    color: 'from-purple-500 to-purple-600',
    image: 'https://images.unsplash.com/photo-1677119966332-8c6e9fb0efab?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxmb290YmFsbCUyMGdvYWxrZWVwZXIlMjBhY3Rpb258ZW58MXx8fHwxNzcwNzEyOTE4fDA&ixlib=rb-4.1.0&q=80&w=1080',
    displayName: 'Demi-finales',
    description: '4 meilleures équipes'
  },
  'PETITE_FINALE': {
    icon: Medal,
    color: 'from-amber-600 to-yellow-600',
    image: 'https://images.unsplash.com/photo-1767884162073-b0e3741aad63?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxmb290YmFsbCUyMHN0YWRpdW0lMjBuaWdodCUyMGxpZ2h0c3xlbnwxfHx8fDE3NzA2NTgxNTV8MA&ixlib=rb-4.1.0&q=80&w=1080',
    displayName: 'Petite finale',
    description: 'Match pour la 3ème place'
  },
  'FINALE': {
    icon: Trophy,
    color: 'from-yellow-500 to-amber-500',
    image: 'https://images.unsplash.com/photo-1620756634852-2190ad694c8f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxmb290YmFsbCUyMHRyb3BoeSUyMGdvbGRlbnxlbnwxfHx8fDE3NzA2Nzg0ODl8MA&ixlib=rb-4.1.0&q=80&w=1080',
    displayName: 'Finale',
    description: 'Match pour le titre mondial'
  }
};

export function PhasesScreen({ onNavigate }: PhasesScreenProps) {
  const [phases, setPhases] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPhases = async () => {
      try {
        const data = await api.getPhases();
        // Merge API data with static definitions
        const enrichedPhases = data.map((phase: any) => ({
          ...phase,
          ...(PHASE_DEFINITIONS[phase.name] || {
            icon: Users,
            color: 'from-gray-500 to-gray-600',
            image: '',
            displayName: phase.name,
            description: phase.description
          })
        }));
        setPhases(enrichedPhases);
      } catch (error) {
        console.error("Failed to load phases:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchPhases();
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
      {/* Header */}
      <Header
        onNavigate={onNavigate}
        title="Phases de compétition"
        currentScreen="phases"
      />

      {/* Content */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            Sélectionnez une phase
          </h1>
          <p className="text-lg text-gray-600">
            Consultez les matchs de chaque phase du tournoi
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {phases.map((phase) => {
            const Icon = phase.icon;
            return (
              <button
                key={phase.id}
                onClick={() => onNavigate({ type: 'matches', phase: phase.displayName })}
                className="group bg-white rounded-2xl shadow-lg shadow-emerald-500/5 overflow-hidden hover:shadow-2xl hover:shadow-emerald-500/10 transition-all duration-300 border border-emerald-100 hover:-translate-y-2 text-left"
              >
                {/* Image Background */}
                <div className="relative h-48 overflow-hidden">
                  <ImageWithFallback
                    src={phase.image}
                    alt={phase.displayName}
                    className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                  />
                  <div className={`absolute inset-0 bg-gradient-to-br ${phase.color} opacity-80 mix-blend-multiply`}></div>
                  <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-black/20 to-transparent"></div>

                  {/* Content over image */}
                  <div className="absolute inset-0 p-6 flex flex-col justify-end">
                    <div className="bg-white/20 backdrop-blur-sm p-3 rounded-2xl inline-block mb-3 border border-white/30 w-fit">
                      <Icon className="h-8 w-8 text-white" />
                    </div>
                    <h3 className="text-2xl font-bold text-white mb-2 drop-shadow-lg">
                      {phase.displayName}
                    </h3>
                    <p className="text-white/90 text-sm drop-shadow-md">
                      {phase.description}
                    </p>
                  </div>
                </div>

                {/* Footer */}
                <div className="p-6 bg-white">
                  <div className="flex items-center justify-between">
                    <span className="text-gray-600 font-semibold">
                      {phase.matchCount} {phase.matchCount === 1 ? 'match' : 'matchs'} disponible{phase.matchCount > 1 ? 's' : ''}
                    </span>
                    <div className="text-emerald-600 group-hover:translate-x-2 transition-transform">
                      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                      </svg>
                    </div>
                  </div>
                </div>
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}
