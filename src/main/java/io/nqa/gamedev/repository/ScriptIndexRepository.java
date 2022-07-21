package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.ScriptIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptIndexRepository extends JpaRepository<ScriptIndex, String> {
}
