package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.Cue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CueRepository extends JpaRepository<Cue, String> {
}
