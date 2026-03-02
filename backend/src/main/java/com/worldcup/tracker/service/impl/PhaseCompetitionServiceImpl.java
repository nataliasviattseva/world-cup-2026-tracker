package com.worldcup.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.PhaseCompetition;
import com.worldcup.tracker.model.PhaseNomEnum;
import com.worldcup.tracker.repository.PhaseCompetitionRepository;

@Service
public class PhaseCompetitionServiceImpl implements IPhaseCompetitionService {

    private final PhaseCompetitionRepository phaseRepository;

    public PhaseCompetitionServiceImpl(PhaseCompetitionRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    @Override
    public PhaseCompetition getEntityById(Long id) {
        return phaseRepository.findById(id).orElse(null);
    }

    @Override
    public List<PhaseCompetition> getAll() {
        return phaseRepository.findAll();
    }

    @Override
    public PhaseCompetition getByNom(PhaseNomEnum nom) {
        return phaseRepository.findByNom(nom).orElse(null);
    }

    @Override
    public PhaseCompetition getCurrentPhase() {
        return phaseRepository.findAllByOrderByOrdreAsc().stream().findFirst().orElse(null);
    }

    @Override
    public List<PhaseCompetition> getOrderedBySequence() {
        return phaseRepository.findAllByOrderByOrdreAsc();
    }

    @Override
    public PhaseCompetition save(PhaseCompetition phase) {
        return phaseRepository.save(phase);
    }

    @Override
    public void delete(Long id) {
        phaseRepository.deleteById(id);
    }

    @Override
    public void activatePhase(Long phaseId) {
        PhaseCompetition targetPhase = getEntityById(phaseId);
        if (targetPhase != null) {
            save(targetPhase);
        }
    }

    @Override
    public PhaseCompetition getNextPhase(Long currentPhaseId) {
        PhaseCompetition currentPhase = getEntityById(currentPhaseId);
        if (currentPhase != null) {
            return phaseRepository.findByOrdreGreaterThanOrderByOrdreAsc(currentPhase.getOrdre()).orElse(null);
        }
        return null;
    }

    @Override
    public boolean isPhaseComplete(Long phaseId) {
        PhaseCompetition phase = getEntityById(phaseId);
        return phase != null && phase.getDateFin() != null;
    }
}