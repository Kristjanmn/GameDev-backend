package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ProjectDTO;
import io.nqa.gamedev.repository.ProjectRepository;
import io.nqa.gamedev.service.global.GlobalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Try to get project by database Id
     *
     * @param databaseId Database Id of the desired Project to find
     * @return Project if one was found
     */
    @Override
    public Project getById(String databaseId) {
        Optional<Project> optProject = this.projectRepository.findById(databaseId);
        return optProject.orElse(null);
    }

    /**
     * Try to get project by projectId
     *
     * @param projectId Id which can be defined by user
     * @return Project if one was found
     */
    @Override
    public Project getByProjectId(String projectId) {
        Optional<Project> optProject = this.projectRepository.findByProjectIdEquals(projectId);
        return optProject.orElse(null);
    }

    /**
     * Used only for communicating with frontend.
     *
     * @param databaseId Desired project's database ID
     * @return CustomResponse with Project as Object
     */
    @Override
    public CustomResponse getProjectById(String databaseId) {
        if (GlobalService.isBlank(databaseId))
            return new CustomResponse(false, "project databaseId was not included in request", null);
        Project project = this.getById(databaseId);
        if (GlobalService.isNull(project))
            return new CustomResponse(false, "Could not get project " + databaseId, null);
        return new CustomResponse(project);
    }

    /**
     * Get Project by projectId for frontend
     *
     * @param projectId Desired project's projectId
     * @return CustomResponse with Project as Object
     */
    @Override
    public CustomResponse getProjectByProjectId(String projectId) {
        if (GlobalService.isBlank(projectId))
            return new CustomResponse(false, "projectId was not included in request", null);
        Project project = this.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse(false, "Could not get project " + projectId, null);
        return new CustomResponse(new ModelMapper().map(project, ProjectDTO.class));
    }

    @Override
    public boolean hasScript(String projectId, Script script) {
        return this.projectRepository.findByIdAndScriptsContains(projectId, script).isPresent();
    }

    @Override
    public void saveScripts(String projectId, Script ... scripts) {
        Optional<Project> optProject = this.projectRepository.findById(projectId);
        if (optProject.isEmpty()) return;
        Project project = optProject.get();
        List<Script> projectScripts = project.getScripts();
        for (Script script : scripts) {
            if (!projectScripts.contains(script)) projectScripts.add(script);
        }
        project.setScripts(projectScripts);
        this.projectRepository.save(project);
    }
}
