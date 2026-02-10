package com.worldcup.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.worldcup.tracker.model.ClassementGroupe;
import com.worldcup.tracker.repository.ClassementGroupeRepository;

@Service
public class ClassementGroupeServiceImpl implements ClassementGroupeService {

    private final ClassementGroupeRepository classementRepository;

    public ClassementGroupeServiceImpl(ClassementGroupeRepository classementRepository) {
        this.classementRepository = classementRepository;
    }

    @Override
    public ClassementGroupe getEntityById(Long id) {
        return classementRepository.findById(id).orElse(null);
    }

    @Override
    public List<ClassementGroupe> getAll() {
        return classementRepository.findAll();
    }

    @Override
    public List<ClassementGroupe> getByGroupe(String groupeNom) {
        return classementRepository.findByGroupeNomOrderByPointsDescDifferenceButsDesc(groupeNom);
    }

    @Override
    public ClassementGroupe getByGroupeAndEquipe(String groupeNom, Long equipeId) {
        return classementRepository.findByGroupeNomAndEquipeId(groupeNom, equipeId).orElse(null);
    }

    @Override
    public ClassementGroupe save(ClassementGroupe classement) {
        return classementRepository.save(classement);
    }

    @Override
    public void delete(Long id) {
        classementRepository.deleteById(id);
    }

    @Override
    public void updateClassement(String groupeNom) {
        List<ClassementGroupe> classements = getByGroupe(groupeNom);
        
        // Sort by points, goal difference, goals scored
        classements.sort((c1, c2) -> {
            int pointsCompare = Integer.compare(c2.getPoints(), c1.getPoints());
            if (pointsCompare != 0) return pointsCompare;
            
            int diffCompare = Integer.compare(c2.getDifferenceButs(), c1.getDifferenceButs());
            if (diffCompare != 0) return diffCompare;
            
            return Integer.compare(c2.getButsPour(), c1.getButsPour());
        });
        
        // Update positions
        for (int i = 0; i < classements.size(); i++) {
            classements.get(i).setPosition(i + 1);
            save(classements.get(i));
        }
    }

    @Override
    public List<ClassementGroupe> getByGroupeOrderedByPosition(String groupeNom) {
        return classementRepository.findByGroupeNomOrderByPositionAsc(groupeNom);
    }

    @Override
    public List<ClassementGroupe> getQualifiedTeamsFromGroupe(String groupeNom, int qualifiedCount) {
        List<ClassementGroupe> orderedClassement = getByGroupeOrderedByPosition(groupeNom);
        return orderedClassement.subList(0, Math.min(qualifiedCount, orderedClassement.size()));
    }
}