package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepository extends JpaRepository<Quest, String> {
}
