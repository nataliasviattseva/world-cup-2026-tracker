package com.worldcup.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.Groupe;
import com.worldcup.tracker.repository.GroupeRepository;

@Service
public class GroupeServiceImpl implements GroupeService {

    private final GroupeRepository groupeRepository;

    public GroupeServiceImpl(GroupeRepository groupeRepository) {
        this.groupeRepository = groupeRepository;
    }

    @Override
    public Groupe getEntityById(Long id) {
        return groupeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Groupe> getAll() {
        return groupeRepository.findAll();
    }

    @Override
    public Groupe getByNom(String nom) {
        return groupeRepository.findByNom(nom).orElse(null);
    }

    @Override
    public List<Groupe> getByPhase(Long phaseId) {
        return groupeRepository.findAllByOrderByNomAsc();
    }

    @Override
    public Groupe save(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    @Override
    public void delete(Long id) {
        groupeRepository.deleteById(id);
    }

    @Override
    public List<Groupe> getAllOrderedByNom() {
        return groupeRepository.findAllByOrderByNomAsc();
    }

    @Override
    public boolean existsByNom(String nom) {
        return groupeRepository.existsByNom(nom);
    }

    @Override
    public void updateGroupeStatistics(String groupeNom) {
        // Logic to recalculate groupe statistics based on matches
        Groupe groupe = getByNom(groupeNom);
        if (groupe != null) {
            // Update group standings logic here
            save(groupe);
        }
    }
}