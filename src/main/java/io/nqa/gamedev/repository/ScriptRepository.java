package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScriptRepository extends JpaRepository<Script, String> {
    Optional<List<Script>> findAllByGlobalIsTrue();

    List<Script> findAllByNameEqualsAndGlobalEquals(String scriptName, boolean isGlobal);
}
