package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.global.GlobalScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalScriptRepository extends JpaRepository<GlobalScript, String> {
}
