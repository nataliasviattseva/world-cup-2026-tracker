package com.worldcup.tracker.config;

import com.worldcup.tracker.model.*;
import com.worldcup.tracker.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            EquipeRepository equipeRepository,
            PhaseCompetitionRepository phaseRepository,
            StadeRepository stadeRepository,
            MatchRepository matchRepository,
            EvenementMatchRepository evenementMatchRepository) {
        return args -> {
            // Check if data already exists
            if (matchRepository.count() > 0) {
                return;
            }

            System.out.println("Initializing sample data...");

            // 1. Initialize Phases
            Map<PhaseNomEnum, PhaseCompetition> phases = new HashMap<>();
            phases.put(PhaseNomEnum.PHASE_GROUPES, createPhase(phaseRepository, PhaseNomEnum.PHASE_GROUPES, 1, LocalDate.of(2026, 6, 11), LocalDate.of(2026, 6, 27), "Phase de groupes", 72));
            phases.put(PhaseNomEnum.HUITIEMES_FINALE, createPhase(phaseRepository, PhaseNomEnum.HUITIEMES_FINALE, 3, LocalDate.of(2026, 6, 28), LocalDate.of(2026, 7, 7), "Huitièmes de finale", 16));
            phases.put(PhaseNomEnum.QUARTS_FINALE, createPhase(phaseRepository, PhaseNomEnum.QUARTS_FINALE, 4, LocalDate.of(2026, 7, 9), LocalDate.of(2026, 7, 11), "Quarts de finale", 8));
            phases.put(PhaseNomEnum.DEMI_FINALES, createPhase(phaseRepository, PhaseNomEnum.DEMI_FINALES, 5, LocalDate.of(2026, 7, 14), LocalDate.of(2026, 7, 15), "Demi-finales", 4));
            phases.put(PhaseNomEnum.PETITE_FINALE, createPhase(phaseRepository, PhaseNomEnum.PETITE_FINALE, 6, LocalDate.of(2026, 7, 18), LocalDate.of(2026, 7, 18), "Petite finale", 1));
            phases.put(PhaseNomEnum.FINALE, createPhase(phaseRepository, PhaseNomEnum.FINALE, 7, LocalDate.of(2026, 7, 19), LocalDate.of(2026, 7, 19), "Finale", 1));

            // 2. Initialize Stadiums
            Map<String, Stade> stades = new HashMap<>();
            stades.put("Estadio Azteca", createStade(stadeRepository, "Estadio Azteca", "Mexico City", "Mexico", 87523));
            stades.put("Arrowhead Stadium", createStade(stadeRepository, "Arrowhead Stadium", "Kansas City", "USA", 76416));
            stades.put("SoFi Stadium", createStade(stadeRepository, "SoFi Stadium", "Los Angeles", "USA", 70240));
            stades.put("MetLife Stadium", createStade(stadeRepository, "MetLife Stadium", "New York/NJ", "USA", 82500));
            stades.put("Hard Rock Stadium", createStade(stadeRepository, "Hard Rock Stadium", "Miami", "USA", 64767));
            stades.put("AT&T Stadium", createStade(stadeRepository, "AT&T Stadium", "Dallas", "USA", 80000));
            stades.put("Mercedes-Benz Stadium", createStade(stadeRepository, "Mercedes-Benz Stadium", "Atlanta", "USA", 71000));

            // 3. Initialize Teams
            Map<String, Equipe> equipes = new HashMap<>();
            equipes.put("Mexico", createEquipe(equipeRepository, "Mexico", "MEX", "🇲🇽"));
            equipes.put("Costa Rica", createEquipe(equipeRepository, "Costa Rica", "CRC", "🇨🇷"));
            equipes.put("Spain", createEquipe(equipeRepository, "Spain", "ESP", "🇪🇸"));
            equipes.put("Portugal", createEquipe(equipeRepository, "Portugal", "POR", "🇵🇹"));
            equipes.put("USA", createEquipe(equipeRepository, "USA", "USA", "🇺🇸"));
            equipes.put("Canada", createEquipe(equipeRepository, "Canada", "CAN", "🇨🇦"));
            equipes.put("Argentina", createEquipe(equipeRepository, "Argentina", "ARG", "🇦🇷"));
            equipes.put("Uruguay", createEquipe(equipeRepository, "Uruguay", "URU", "🇺🇾"));
            equipes.put("Brazil", createEquipe(equipeRepository, "Brazil", "BRA", "🇧🇷"));
            equipes.put("Chile", createEquipe(equipeRepository, "Chile", "CHI", "🇨🇱"));
            equipes.put("England", createEquipe(equipeRepository, "England", "ENG", "🏴󠁧󠁢󠁥󠁮󠁧󠁿"));
            equipes.put("Belgium", createEquipe(equipeRepository, "Belgium", "BEL", "🇧🇪"));
            equipes.put("France", createEquipe(equipeRepository, "France", "FRA", "🇫🇷"));
            equipes.put("Germany", createEquipe(equipeRepository, "Germany", "GER", "🇩🇪"));
            equipes.put("TBD", createEquipe(equipeRepository, "TBD", "TBD", "🏳️"));

            // 4. Initialize Matches

            // Match 1: Mexico vs Costa Rica (Finished)
            Match match1 = createMatch(matchRepository, phases.get(PhaseNomEnum.PHASE_GROUPES),
                    equipes.get("Mexico"), equipes.get("Costa Rica"),
                    stades.get("Estadio Azteca"),
                    LocalDateTime.of(2026, Month.JUNE, 11, 17, 0),
                    2, 1, StatutMatchEnum.TERMINE);

            createEvent(evenementMatchRepository, match1, equipes.get("Mexico"), "goal", 23, "H. Lozano");
            createEvent(evenementMatchRepository, match1, equipes.get("Costa Rica"), "yellow_card", 45, "K. Waston");
            createEvent(evenementMatchRepository, match1, equipes.get("Costa Rica"), "goal", 67, "J. Campbell");
            createEvent(evenementMatchRepository, match1, equipes.get("Mexico"), "goal", 82, "R. Jiménez");

            // Match 1b: Spain vs Portugal (Finished)
            Match match1b = createMatch(matchRepository, phases.get(PhaseNomEnum.PHASE_GROUPES),
                    equipes.get("Spain"), equipes.get("Portugal"),
                    stades.get("Arrowhead Stadium"),
                    LocalDateTime.of(2026, Month.JUNE, 11, 20, 0),
                    3, 2, StatutMatchEnum.TERMINE);

            createEvent(evenementMatchRepository, match1b, equipes.get("Spain"), "goal", 15, "Morata");
            createEvent(evenementMatchRepository, match1b, equipes.get("Portugal"), "goal", 28, "Cristiano Ronaldo");
            createEvent(evenementMatchRepository, match1b, equipes.get("Spain"), "goal", 41, "Pedri");
            createEvent(evenementMatchRepository, match1b, equipes.get("Portugal"), "yellow_card", 58, "Pepe");
            createEvent(evenementMatchRepository, match1b, equipes.get("Portugal"), "goal", 72, "Bruno Fernandes");
            createEvent(evenementMatchRepository, match1b, equipes.get("Spain"), "goal", 85, "Gavi");

            // Match 2: USA vs Canada (Live)
            Match match2 = createMatch(matchRepository, phases.get(PhaseNomEnum.PHASE_GROUPES),
                    equipes.get("USA"), equipes.get("Canada"),
                    stades.get("SoFi Stadium"),
                    LocalDateTime.of(2026, Month.JUNE, 12, 20, 0), // Use today or recent date if we want it to simulate nicely
                    1, 1, StatutMatchEnum.EN_COURS);

            createEvent(evenementMatchRepository, match2, equipes.get("USA"), "goal", 34, "C. Pulisic");
            createEvent(evenementMatchRepository, match2, equipes.get("Canada"), "goal", 56, "A. Davies");
            createEvent(evenementMatchRepository, match2, equipes.get("USA"), "yellow_card", 62, "W. McKennie");

            // Match 3: Argentina vs Uruguay (Upcoming)
            createMatch(matchRepository, phases.get(PhaseNomEnum.PHASE_GROUPES),
                    equipes.get("Argentina"), equipes.get("Uruguay"),
                    stades.get("MetLife Stadium"),
                    LocalDateTime.of(2026, Month.JUNE, 13, 15, 0),
                    null, null, StatutMatchEnum.A_VENIR);

            // Match 4: Brazil vs Chile (Upcoming)
            createMatch(matchRepository, phases.get(PhaseNomEnum.PHASE_GROUPES),
                    equipes.get("Brazil"), equipes.get("Chile"),
                    stades.get("Hard Rock Stadium"),
                    LocalDateTime.of(2026, Month.JUNE, 14, 18, 0),
                    null, null, StatutMatchEnum.A_VENIR);

            // Match 5: England vs Belgium (Upcoming)
            createMatch(matchRepository, phases.get(PhaseNomEnum.PHASE_GROUPES),
                    equipes.get("England"), equipes.get("Belgium"),
                    stades.get("AT&T Stadium"),
                    LocalDateTime.of(2026, Month.JUNE, 14, 21, 0),
                    null, null, StatutMatchEnum.A_VENIR);

            // Match 6: France vs Germany (Upcoming)
            createMatch(matchRepository, phases.get(PhaseNomEnum.PHASE_GROUPES),
                    equipes.get("France"), equipes.get("Germany"),
                    stades.get("Mercedes-Benz Stadium"),
                    LocalDateTime.of(2026, Month.JUNE, 15, 12, 0),
                    null, null, StatutMatchEnum.A_VENIR);

            // TBD Matches
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), equipes.get("TBD"), equipes.get("TBD"), stades.get("MetLife Stadium"), LocalDateTime.of(2026, Month.JUNE, 28, 16, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), equipes.get("TBD"), equipes.get("TBD"), stades.get("SoFi Stadium"), LocalDateTime.of(2026, Month.JUNE, 28, 20, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), equipes.get("TBD"), equipes.get("TBD"), stades.get("Hard Rock Stadium"), LocalDateTime.of(2026, Month.JULY, 9, 17, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), equipes.get("TBD"), equipes.get("TBD"), stades.get("AT&T Stadium"), LocalDateTime.of(2026, Month.JULY, 10, 17, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.DEMI_FINALES), equipes.get("TBD"), equipes.get("TBD"), stades.get("AT&T Stadium"), LocalDateTime.of(2026, Month.JULY, 14, 20, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.DEMI_FINALES), equipes.get("TBD"), equipes.get("TBD"), stades.get("Mercedes-Benz Stadium"), LocalDateTime.of(2026, Month.JULY, 15, 20, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.PETITE_FINALE), equipes.get("TBD"), equipes.get("TBD"), stades.get("Hard Rock Stadium"), LocalDateTime.of(2026, Month.JULY, 18, 16, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.FINALE), equipes.get("TBD"), equipes.get("TBD"), stades.get("MetLife Stadium"), LocalDateTime.of(2026, Month.JULY, 19, 15, 0), null, null, StatutMatchEnum.A_VENIR);

            System.out.println("Data initialization completed.");
        };
    }

    private PhaseCompetition createPhase(PhaseCompetitionRepository repository, PhaseNomEnum nom, int ordre, LocalDate debut, LocalDate fin, String desc, int nbMatchs) {
        return repository.save(PhaseCompetition.builder()
                .nom(nom)
                .ordre(ordre)
                .dateDebut(debut)
                .dateFin(fin)
                .description(desc)
                .nombreMatchs(nbMatchs)
                .build());
    }

    private Stade createStade(StadeRepository repository, String nom, String ville, String pays, int capacite) {
        return repository.save(Stade.builder()
                .nom(nom)
                .ville(ville)
                .pays(pays)
                .capacite(capacite)
                .build());
    }

    private Equipe createEquipe(EquipeRepository repository, String nom, String code, String drapeau) {
        return repository.save(Equipe.builder()
                .nom(nom)
                .codePays(code)
                .drapeauUrl(drapeau)
                .build());
    }

    private Match createMatch(MatchRepository repository, PhaseCompetition phase, Equipe eq1, Equipe eq2, Stade stade, LocalDateTime date, Integer score1, Integer score2, StatutMatchEnum statut) {
        return repository.save(Match.builder()
                .phase(phase)
                .equipe1(eq1)
                .equipe2(eq2)
                .stade(stade)
                .dateHeure(date)
                .scoreEquipe1(score1)
                .scoreEquipe2(score2)
                .statut(statut)
                .build());
    }

    private void createEvent(EvenementMatchRepository repository, Match match, Equipe equipe, String type, int minute, String joueur) {
        repository.save(EvenementMatch.builder()
                .match(match)
                .equipe(equipe)
                .typeEvenement(type)
                .minute(minute)
                .joueurNom(joueur)
                .description(type + " by " + joueur)
                .build());
    }
}
