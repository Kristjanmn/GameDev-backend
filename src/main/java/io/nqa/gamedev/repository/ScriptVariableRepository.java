package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.ScriptVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptVariableRepository extends JpaRepository<ScriptVariable, String> {
}
