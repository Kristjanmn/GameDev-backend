package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.ProjectRepository;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Pass Optional<Project> to another service.
     *
     * @param projectId Id of the desired Project to find
     * @return Optional<Project>
     */
    @Override
    public Project getById(String projectId) {
        Optional<Project> optProject = this.projectRepository.findById(projectId);
        return optProject.orElse(null);
    }

    /**
     * Used only for communicating with frontend.
     *
     * @param projectId Desired project's ID
     * @return CustomResponse with Project as Object
     */
    @Override
    public CustomResponse getProjectById(String projectId) {
        if (GlobalService.isBlank(projectId))
            return new CustomResponse(false, "projectId was not included in request", null);
        Project project = this.getById(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse(false, "Could not get project " + projectId, null);
        return new CustomResponse(true, "success", project);
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
