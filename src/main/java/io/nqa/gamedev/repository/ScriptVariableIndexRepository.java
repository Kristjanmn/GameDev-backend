package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.ScriptVariable;
import io.nqa.gamedev.entity.ScriptVariableIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScriptVariableIndexRepository extends JpaRepository<ScriptVariableIndex, String> {

    Optional<ScriptVariableIndex> findByVariableEquals(ScriptVariable variable);
}
