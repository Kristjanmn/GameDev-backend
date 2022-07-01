package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.QuestPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestPhaseRepository extends JpaRepository<QuestPhase, String> {
}
