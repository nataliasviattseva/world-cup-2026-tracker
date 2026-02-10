package com.worldcup.tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldcup.tracker.model.ClassementGroupe;

public interface ClassementGroupeRepository extends JpaRepository<ClassementGroupe, Long> {

    List<ClassementGroupe> findByGroupeIdOrderByPointsDescDifferenceButsDesc(Long groupeId);

    Optional<ClassementGroupe> findByGroupeIdAndEquipeId(Long groupeId, Long equipeId);

}
