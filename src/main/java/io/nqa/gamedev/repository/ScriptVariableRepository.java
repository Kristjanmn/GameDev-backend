package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.ScriptVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScriptVariableRepository extends JpaRepository<ScriptVariable, String> {

    Optional<ScriptVariable> findByVariableTypeEqualsAndVariableNameEquals(String varType, String varName);
}
