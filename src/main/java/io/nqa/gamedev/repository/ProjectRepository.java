package io.nqa.gamedev.repository;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    Optional<Project> findByProjectIdEquals(String projectId);

    Optional<Project> findByIdAndScriptsContains(String projectId, Script script);
}
