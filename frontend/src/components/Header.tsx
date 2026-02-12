import { Trophy, Home, Calendar, ArrowLeft, Users, Award, Layers, Radio } from 'lucide-react';
import { Screen } from '../App';

interface HeaderProps {
  onNavigate: (screen: Screen) => void;
  title?: string;
  subtitle?: string;
  showBack?: boolean;
  backScreen?: Screen;
  currentScreen?: string;
}

export function Header({ 
  onNavigate, 
  title, 
  subtitle, 
  showBack = false, 
  backScreen,
  currentScreen = 'home'
}: HeaderProps) {
  return (
    <header className="bg-white/80 backdrop-blur-lg shadow-lg shadow-emerald-500/5 sticky top-0 z-50 border-b border-emerald-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-20">
          {/* Left side - Back button or Logo */}
          <div className="flex items-center space-x-4">
            {showBack && backScreen ? (
              <button
                onClick={() => onNavigate(backScreen)}
                className="p-2 hover:bg-emerald-50 rounded-xl transition-colors group"
              >
                <ArrowLeft className="h-6 w-6 text-gray-600 group-hover:text-emerald-600 transition-colors" />
              </button>
            ) : null}
            
            <button
              onClick={() => onNavigate({ type: 'home' })}
              className="flex items-center space-x-3 hover:opacity-80 transition-opacity"
            >
              <div className="bg-gradient-to-br from-emerald-500 to-green-600 p-2 rounded-2xl shadow-lg shadow-emerald-500/30">
                <Trophy className="h-7 w-7 text-white" />
              </div>
              <div className="hidden sm:block">
                {title ? (
                  <>
                    <span className="text-xl font-bold bg-gradient-to-r from-emerald-600 to-green-600 bg-clip-text text-transparent block">
                      {title}
                    </span>
                    {subtitle && (
                      <p className="text-xs text-gray-500">{subtitle}</p>
                    )}
                  </>
                ) : (
                  <>
                    <span className="text-2xl font-bold bg-gradient-to-r from-emerald-600 to-green-600 bg-clip-text text-transparent block">
                      World Cup 2026
                    </span>
                    <p className="text-xs text-gray-500">United 2026</p>
                  </>
                )}
              </div>
            </button>
          </div>

          {/* Right side - Navigation */}
          <nav className="flex items-center space-x-2">
            <button
              onClick={() => onNavigate({ type: 'home' })}
              className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl transition-all duration-200 ${
                currentScreen === 'home'
                  ? 'bg-gradient-to-r from-emerald-500 to-green-600 text-white shadow-lg shadow-emerald-500/30'
                  : 'text-gray-600 hover:bg-emerald-50 hover:text-emerald-700'
              }`}
            >
              <Home className="h-4 w-4" />
              <span className="hidden md:inline font-semibold">Accueil</span>
            </button>

            <button
              onClick={() => onNavigate({ type: 'liveMatches' })}
              className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl transition-all duration-200 ${
                currentScreen === 'liveMatches' || currentScreen === 'liveEvents'
                  ? 'bg-gradient-to-r from-red-500 to-red-600 text-white shadow-lg shadow-red-500/30'
                  : 'text-gray-600 hover:bg-red-50 hover:text-red-600'
              }`}
            >
              <Radio className="h-4 w-4" />
              <span className="hidden md:inline font-semibold">En direct</span>
            </button>

            <button
              onClick={() => onNavigate({ type: 'phases' })}
              className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl transition-all duration-200 ${
                currentScreen === 'phases' || currentScreen === 'matches' || currentScreen === 'detail' || currentScreen === 'live'
                  ? 'bg-gradient-to-r from-emerald-500 to-green-600 text-white shadow-lg shadow-emerald-500/30'
                  : 'text-gray-600 hover:bg-emerald-50 hover:text-emerald-700'
              }`}
            >
              <Calendar className="h-4 w-4" />
              <span className="hidden md:inline font-semibold">Matchs</span>
            </button>

            <button
              onClick={() => onNavigate({ type: 'teams' })}
              className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl transition-all duration-200 ${
                currentScreen === 'teams' || currentScreen === 'teamDetail'
                  ? 'bg-gradient-to-r from-emerald-500 to-green-600 text-white shadow-lg shadow-emerald-500/30'
                  : 'text-gray-600 hover:bg-emerald-50 hover:text-emerald-700'
              }`}
            >
              <Users className="h-4 w-4" />
              <span className="hidden md:inline font-semibold">Équipes</span>
            </button>

            <button
              onClick={() => onNavigate({ type: 'standings' })}
              className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl transition-all duration-200 ${
                currentScreen === 'standings'
                  ? 'bg-gradient-to-r from-emerald-500 to-green-600 text-white shadow-lg shadow-emerald-500/30'
                  : 'text-gray-600 hover:bg-emerald-50 hover:text-emerald-700'
              }`}
            >
              <Award className="h-4 w-4" />
              <span className="hidden md:inline font-semibold">Classements</span>
            </button>

            <button
              onClick={() => onNavigate({ type: 'worldcupgroups' })}
              className={`flex items-center space-x-2 px-4 py-2.5 rounded-xl transition-all duration-200 ${
                currentScreen === 'worldcupgroups'
                  ? 'bg-gradient-to-r from-emerald-500 to-green-600 text-white shadow-lg shadow-emerald-500/30'
                  : 'text-gray-600 hover:bg-emerald-50 hover:text-emerald-700'
              }`}
            >
              <Layers className="h-4 w-4" />
              <span className="hidden md:inline font-semibold">Groupes</span>
            </button>
          </nav>
        </div>
      </div>
    </header>
  );
}
