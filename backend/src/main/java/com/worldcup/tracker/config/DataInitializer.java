package com.worldcup.tracker.config;

import com.worldcup.tracker.model.*;
import com.worldcup.tracker.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            EquipeRepository equipeRepository,
            PhaseCompetitionRepository phaseRepository,
            StadeRepository stadeRepository,
            MatchRepository matchRepository,
            EvenementMatchRepository evenementMatchRepository,
            GroupeRepository groupeRepository) {
        return args -> {

            // 1) ALWAYS ensure groups exist
            if (groupeRepository.count() == 0) {
                System.out.println("Seeding 12 groups A–L...");
                String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };
                for (String letter : letters) {
                    groupeRepository.save(Groupe.builder()
                            .lettre(letter)
                            .nom("Groupe " + letter)
                            .build());
                }
            }

            // 2) If matches already exist (LiveScore sync), stop here
            if (matchRepository.count() > 0) {
                System.out.println("Matches already exist. Skipping sample matches initialization.");
                return;
            }

            // 3) Otherwise continue with your sample/demo data init...
            System.out.println("Initializing sample data...");

            // 0. Initialize Groups (12 groups for World Cup 2026)
            Map<String, Groupe> groupes = new HashMap<>();
            String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };
            for (String letter : letters) {
                groupes.put(letter, createGroupe(groupeRepository, letter, "Groupe " + letter));
            }

            // 1. Initialize Phases
            Map<PhaseNomEnum, PhaseCompetition> phases = new HashMap<>();
            phases.put(PhaseNomEnum.PHASE_GROUPES, createPhase(phaseRepository, PhaseNomEnum.PHASE_GROUPES, 1,
                    LocalDate.of(2026, 6, 11), LocalDate.of(2026, 6, 27), "Phase de groupes", 72));
            phases.put(PhaseNomEnum.HUITIEMES_FINALE, createPhase(phaseRepository, PhaseNomEnum.HUITIEMES_FINALE, 3,
                    LocalDate.of(2026, 6, 28), LocalDate.of(2026, 7, 7), "Huitièmes de finale", 16));
            phases.put(PhaseNomEnum.QUARTS_FINALE, createPhase(phaseRepository, PhaseNomEnum.QUARTS_FINALE, 4,
                    LocalDate.of(2026, 7, 9), LocalDate.of(2026, 7, 11), "Quarts de finale", 8));
            phases.put(PhaseNomEnum.DEMI_FINALES, createPhase(phaseRepository, PhaseNomEnum.DEMI_FINALES, 5,
                    LocalDate.of(2026, 7, 14), LocalDate.of(2026, 7, 15), "Demi-finales", 4));
            phases.put(PhaseNomEnum.PETITE_FINALE, createPhase(phaseRepository, PhaseNomEnum.PETITE_FINALE, 6,
                    LocalDate.of(2026, 7, 18), LocalDate.of(2026, 7, 18), "Petite finale", 1));
            phases.put(PhaseNomEnum.FINALE, createPhase(phaseRepository, PhaseNomEnum.FINALE, 7,
                    LocalDate.of(2026, 7, 19), LocalDate.of(2026, 7, 19), "Finale", 1));

            // 2. Initialize Stadiums (all 16 World Cup 2026 venues)
            Map<String, Stade> stades = new HashMap<>();
            stades.put("Estadio Azteca",
                    createStade(stadeRepository, "Estadio Azteca", "Mexico City", "Mexico", 87523));
            stades.put("Estadio BBVA", createStade(stadeRepository, "Estadio BBVA", "Monterrey", "Mexico", 53500));
            stades.put("Estadio Akron", createStade(stadeRepository, "Estadio Akron", "Guadalajara", "Mexico", 49850));
            stades.put("MetLife Stadium", createStade(stadeRepository, "MetLife Stadium", "New York/NJ", "USA", 82500));
            stades.put("SoFi Stadium", createStade(stadeRepository, "SoFi Stadium", "Los Angeles", "USA", 70240));
            stades.put("AT&T Stadium", createStade(stadeRepository, "AT&T Stadium", "Dallas", "USA", 80000));
            stades.put("Hard Rock Stadium", createStade(stadeRepository, "Hard Rock Stadium", "Miami", "USA", 64767));
            stades.put("Mercedes-Benz Stadium",
                    createStade(stadeRepository, "Mercedes-Benz Stadium", "Atlanta", "USA", 71000));
            stades.put("NRG Stadium", createStade(stadeRepository, "NRG Stadium", "Houston", "USA", 72220));
            stades.put("Lincoln Financial Field",
                    createStade(stadeRepository, "Lincoln Financial Field", "Philadelphia", "USA", 69176));
            stades.put("Lumen Field", createStade(stadeRepository, "Lumen Field", "Seattle", "USA", 68740));
            stades.put("Gillette Stadium",
                    createStade(stadeRepository, "Gillette Stadium", "Boston/Foxborough", "USA", 65878));
            stades.put("Arrowhead Stadium",
                    createStade(stadeRepository, "Arrowhead Stadium", "Kansas City", "USA", 76416));
            stades.put("Levi's Stadium", createStade(stadeRepository, "Levi's Stadium", "San Francisco", "USA", 68500));
            stades.put("BMO Field", createStade(stadeRepository, "BMO Field", "Toronto", "Canada", 45736));
            stades.put("BC Place", createStade(stadeRepository, "BC Place", "Vancouver", "Canada", 54500));

            // 3. Initialize Teams
            Map<String, Equipe> equipes = new HashMap<>();
            equipes.put("Mexico", createEquipeWithGroupe(equipeRepository, "Mexico", "MEX", "🇲🇽", "A", 15, "CONCACAF",
                    groupes.get("A")));
            equipes.put("Costa Rica", createEquipeWithGroupe(equipeRepository, "Costa Rica", "CRC", "🇨🇷", "A", 42,
                    "CONCACAF", groupes.get("A")));
            equipes.put("Spain",
                    createEquipeWithGroupe(equipeRepository, "Spain", "ESP", "🇪🇸", "B", 8, "UEFA", groupes.get("B")));
            equipes.put("Portugal", createEquipeWithGroupe(equipeRepository, "Portugal", "POR", "🇵🇹", "B", 9, "UEFA",
                    groupes.get("B")));
            equipes.put("USA", createEquipeWithGroupe(equipeRepository, "USA", "USA", "🇺🇸", "C", 13, "CONCACAF",
                    groupes.get("C")));
            equipes.put("Canada", createEquipeWithGroupe(equipeRepository, "Canada", "CAN", "🇨🇦", "C", 40, "CONCACAF",
                    groupes.get("C")));
            equipes.put("Argentina", createEquipeWithGroupe(equipeRepository, "Argentina", "ARG", "🇦🇷", "D", 1,
                    "CONMEBOL", groupes.get("D")));
            equipes.put("Uruguay", createEquipeWithGroupe(equipeRepository, "Uruguay", "URU", "🇺🇾", "D", 14,
                    "CONMEBOL", groupes.get("D")));
            equipes.put("Brazil", createEquipeWithGroupe(equipeRepository, "Brazil", "BRA", "🇧🇷", "E", 4, "CONMEBOL",
                    groupes.get("E")));
            equipes.put("Chile", createEquipeWithGroupe(equipeRepository, "Chile", "CHI", "🇨🇱", "E", 38, "CONMEBOL",
                    groupes.get("E")));
            equipes.put("England", createEquipeWithGroupe(equipeRepository, "England", "ENG", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", "F", 5,
                    "UEFA", groupes.get("F")));
            equipes.put("Belgium", createEquipeWithGroupe(equipeRepository, "Belgium", "BEL", "🇧🇪", "F", 3, "UEFA",
                    groupes.get("F")));
            equipes.put("France", createEquipeWithGroupe(equipeRepository, "France", "FRA", "🇫🇷", "G", 2, "UEFA",
                    groupes.get("G")));
            equipes.put("Germany", createEquipeWithGroupe(equipeRepository, "Germany", "GER", "🇩🇪", "G", 11, "UEFA",
                    groupes.get("G")));
            equipes.put("TBD_A", createEquipe(equipeRepository, "TBD", "TBA", "🏳️"));
            equipes.put("TBD_B", createEquipe(equipeRepository, "TBD", "TBB", "🏳️"));

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
                    LocalDateTime.of(2026, Month.JUNE, 12, 20, 0), // Use today or recent date if we want it to simulate
                                                                   // nicely
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

            // =============================================
            // 5. Knockout Stage Matches (TBD teams)
            // =============================================

            Equipe tbdA = equipes.get("TBD_A");
            Equipe tbdB = equipes.get("TBD_B");

            // --- HUITIÈMES DE FINALE (Round of 16) - 16 matches ---
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("MetLife Stadium"), LocalDateTime.of(2026, Month.JUNE, 28, 14, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("SoFi Stadium"), LocalDateTime.of(2026, Month.JUNE, 28, 18, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("AT&T Stadium"), LocalDateTime.of(2026, Month.JUNE, 29, 14, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Hard Rock Stadium"), LocalDateTime.of(2026, Month.JUNE, 29, 18, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Mercedes-Benz Stadium"), LocalDateTime.of(2026, Month.JUNE, 30, 14, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("NRG Stadium"), LocalDateTime.of(2026, Month.JUNE, 30, 18, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Arrowhead Stadium"), LocalDateTime.of(2026, Month.JULY, 1, 14, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Lincoln Financial Field"), LocalDateTime.of(2026, Month.JULY, 1, 18, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Lumen Field"), LocalDateTime.of(2026, Month.JULY, 2, 14, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Gillette Stadium"), LocalDateTime.of(2026, Month.JULY, 2, 18, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Levi's Stadium"), LocalDateTime.of(2026, Month.JULY, 3, 14, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB, stades.get("BMO Field"),
                    LocalDateTime.of(2026, Month.JULY, 3, 18, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB, stades.get("BC Place"),
                    LocalDateTime.of(2026, Month.JULY, 4, 14, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Estadio Azteca"), LocalDateTime.of(2026, Month.JULY, 4, 18, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Estadio BBVA"), LocalDateTime.of(2026, Month.JULY, 5, 14, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.HUITIEMES_FINALE), tbdA, tbdB,
                    stades.get("Estadio Akron"), LocalDateTime.of(2026, Month.JULY, 5, 18, 0), null, null,
                    StatutMatchEnum.A_VENIR);

            // --- QUARTS DE FINALE (Quarter-finals) - 8 matches ---
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB,
                    stades.get("MetLife Stadium"), LocalDateTime.of(2026, Month.JULY, 9, 16, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB, stades.get("SoFi Stadium"),
                    LocalDateTime.of(2026, Month.JULY, 9, 20, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB, stades.get("AT&T Stadium"),
                    LocalDateTime.of(2026, Month.JULY, 10, 16, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB,
                    stades.get("Hard Rock Stadium"), LocalDateTime.of(2026, Month.JULY, 10, 20, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB, stades.get("NRG Stadium"),
                    LocalDateTime.of(2026, Month.JULY, 11, 16, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB,
                    stades.get("Mercedes-Benz Stadium"), LocalDateTime.of(2026, Month.JULY, 11, 20, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB,
                    stades.get("Arrowhead Stadium"), LocalDateTime.of(2026, Month.JULY, 12, 16, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.QUARTS_FINALE), tbdA, tbdB,
                    stades.get("Lincoln Financial Field"), LocalDateTime.of(2026, Month.JULY, 12, 20, 0), null, null,
                    StatutMatchEnum.A_VENIR);

            // --- DEMI-FINALES (Semi-finals) - 4 matches ---
            createMatch(matchRepository, phases.get(PhaseNomEnum.DEMI_FINALES), tbdA, tbdB,
                    stades.get("MetLife Stadium"), LocalDateTime.of(2026, Month.JULY, 14, 20, 0), null, null,
                    StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.DEMI_FINALES), tbdA, tbdB, stades.get("AT&T Stadium"),
                    LocalDateTime.of(2026, Month.JULY, 15, 20, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.DEMI_FINALES), tbdA, tbdB, stades.get("SoFi Stadium"),
                    LocalDateTime.of(2026, Month.JULY, 16, 20, 0), null, null, StatutMatchEnum.A_VENIR);
            createMatch(matchRepository, phases.get(PhaseNomEnum.DEMI_FINALES), tbdA, tbdB,
                    stades.get("Hard Rock Stadium"), LocalDateTime.of(2026, Month.JULY, 17, 20, 0), null, null,
                    StatutMatchEnum.A_VENIR);

            // --- PETITE FINALE (3rd place play-off) - 1 match ---
            createMatch(matchRepository, phases.get(PhaseNomEnum.PETITE_FINALE), tbdA, tbdB,
                    stades.get("Hard Rock Stadium"), LocalDateTime.of(2026, Month.JULY, 18, 16, 0), null, null,
                    StatutMatchEnum.A_VENIR);

            // --- FINALE - 1 match ---
            createMatch(matchRepository, phases.get(PhaseNomEnum.FINALE), tbdA, tbdB, stades.get("MetLife Stadium"),
                    LocalDateTime.of(2026, Month.JULY, 19, 15, 0), null, null, StatutMatchEnum.A_VENIR);

            // Assign official World Cup 2026 teams to groups
            assignWorldCupTeamsToGroups(equipeRepository, groupes);

            System.out.println("Data initialization completed.");
        };
    }

    private void assignWorldCupTeamsToGroups(EquipeRepository equipeRepository, Map<String, Groupe> groupes) {
        Map<String, String> teamGroupMap = new HashMap<>();
        teamGroupMap.put("Mexico", "A");
        teamGroupMap.put("South Africa", "A");
        teamGroupMap.put("South Korea", "A");
        teamGroupMap.put("Canada", "B");
        teamGroupMap.put("Qatar", "B");
        teamGroupMap.put("Switzerland", "B");
        teamGroupMap.put("Brazil", "C");
        teamGroupMap.put("Morocco", "C");
        teamGroupMap.put("Haiti", "C");
        teamGroupMap.put("Scotland", "C");
        teamGroupMap.put("USA", "D");
        teamGroupMap.put("Paraguay", "D");
        teamGroupMap.put("Australia", "D");
        teamGroupMap.put("Germany", "E");
        teamGroupMap.put("Curacao", "E");
        teamGroupMap.put("Ivory Coast", "E");
        teamGroupMap.put("Ecuador", "E");
        teamGroupMap.put("Netherlands", "F");
        teamGroupMap.put("Japan", "F");
        teamGroupMap.put("Tunisia", "F");
        teamGroupMap.put("Belgium", "G");
        teamGroupMap.put("Egypt", "G");
        teamGroupMap.put("Iran", "G");
        teamGroupMap.put("New Zealand", "G");
        teamGroupMap.put("Spain", "H");
        teamGroupMap.put("Cape Verde", "H");
        teamGroupMap.put("Saudi Arabia", "H");
        teamGroupMap.put("Uruguay", "H");
        teamGroupMap.put("France", "I");
        teamGroupMap.put("Senegal", "I");
        teamGroupMap.put("Norway", "I");
        teamGroupMap.put("Argentina", "J");
        teamGroupMap.put("Algeria", "J");
        teamGroupMap.put("Austria", "J");
        teamGroupMap.put("Jordan", "J");
        teamGroupMap.put("Portugal", "K");
        teamGroupMap.put("Uzbekistan", "K");
        teamGroupMap.put("Colombia", "K");
        teamGroupMap.put("England", "L");
        teamGroupMap.put("Croatia", "L");
        teamGroupMap.put("Ghana", "L");
        teamGroupMap.put("Panama", "L");

        List<Equipe> allTeams = equipeRepository.findAll();
        int assigned = 0;
        for (Equipe equipe : allTeams) {
            String groupLetter = teamGroupMap.get(equipe.getNom());
            if (groupLetter != null && equipe.getGroupe() == null) {
                Groupe groupe = groupes.get(groupLetter);
                equipe.setGroupe(groupe);
                equipe.setGroupeCode(groupLetter);
                equipeRepository.save(equipe);
                assigned++;
                log.info("Assigned {} to Group {}", equipe.getNom(), groupLetter);
            }
        }
        log.info("World Cup 2026 team assignment complete: {} teams assigned", assigned);
    }

    private PhaseCompetition createPhase(PhaseCompetitionRepository repository, PhaseNomEnum nom, int ordre,
            LocalDate debut, LocalDate fin, String desc, int nbMatchs) {
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

    private Equipe createEquipeWithDetails(EquipeRepository repository, String nom, String code, String drapeau,
            String groupeCode, Integer fifaRanking, String confederation) {
        return repository.save(Equipe.builder()
                .nom(nom)
                .codePays(code)
                .drapeauUrl(drapeau)
                .groupeCode(groupeCode)
                .fifaRanking(fifaRanking)
                .confederation(confederation)
                .build());
    }

    private Equipe createEquipeWithGroupe(EquipeRepository repository, String nom, String code, String drapeau,
            String groupeCode, Integer fifaRanking, String confederation, Groupe groupe) {
        return repository.save(Equipe.builder()
                .nom(nom)
                .codePays(code)
                .drapeauUrl(drapeau)
                .groupeCode(groupeCode)
                .fifaRanking(fifaRanking)
                .confederation(confederation)
                .groupe(groupe)
                .build());
    }

    private Groupe createGroupe(GroupeRepository repository, String lettre, String nom) {
        return repository.save(Groupe.builder()
                .lettre(lettre)
                .nom(nom)
                .build());
    }

    private Match createMatch(MatchRepository repository, PhaseCompetition phase, Equipe eq1, Equipe eq2, Stade stade,
            LocalDateTime date, Integer score1, Integer score2, StatutMatchEnum statut) {
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

    private void createEvent(EvenementMatchRepository repository, Match match, Equipe equipe, String type, int minute,
            String joueur) {
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