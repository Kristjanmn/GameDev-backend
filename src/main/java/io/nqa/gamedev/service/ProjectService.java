package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ProjectDTO;
import io.nqa.gamedev.repository.ProjectRepository;
import io.nqa.gamedev.service.global.CookieService;
import io.nqa.gamedev.service.global.GUIDGenerator;
import io.nqa.gamedev.service.global.GlobalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Next 4 functions look like they could be just 2 functions,
    // but they need to be separate like that,
    // because getById and getByProjectId are used also elsewhere.

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
            return new CustomResponse("project databaseId was not included in request", null);
        Project project = this.getById(databaseId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Could not get project " + databaseId, null);
        return new CustomResponse(new ModelMapper().map(project, ProjectDTO.class));
    }

    /**
     * Get Project by projectId for frontend
     *
     * @param projectId Desired project's projectId
     * @return CustomResponse with Project as Object
     */
    @Override
    public CustomResponse getProjectByProjectId(String projectId, HttpServletResponse response) {
        if (GlobalService.isBlank(projectId))
            return new CustomResponse("projectId was not included in request", null);
        Project project = this.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Could not get project " + projectId, null);
        response.addCookie(CookieService.createProjectCookie(projectId));
        return new CustomResponse(new ModelMapper().map(project, ProjectDTO.class));
    }

    @Override
    public CustomResponse saveProject(ProjectDTO projectDTO) {
        if (GlobalService.isNull(projectDTO))
            return new CustomResponse("Provided project is NULL", null);
        if (GlobalService.isBlank(projectDTO.getId())) {
            // New project
            if (GlobalService.isBlank(projectDTO.getTitle()))
                return new CustomResponse("Provided project lacks title", null);
            // Set default values if they are not provided
            Project project = new Project();
            // Id
            project.setId(GUIDGenerator.generate());
            // ProjectId
            if (GlobalService.isBlank(projectDTO.getProjectId()))
                project.setProjectId(project.getId());
            else project.setProjectId(projectDTO.getProjectId());
            // Title
            project.setTitle(projectDTO.getTitle());
            // Description
            if (GlobalService.isBlank(projectDTO.getDescription()))
                project.setDescription("");
            else project.setDescription(projectDTO.getDescription());
            return new CustomResponse(new ModelMapper().map(this.projectRepository.save(project), ProjectDTO.class));
        } else {
            // Existing project
            Optional<Project> optProject = this.projectRepository.findById(projectDTO.getId());
            if (optProject.isEmpty())
                return new CustomResponse("Could not find project", null);
            Project project = optProject.get();
            // ProjectId
            if (!GlobalService.isBlank(projectDTO.getProjectId()))
                project.setProjectId(projectDTO.getProjectId());
            // Title
            if (!GlobalService.isBlank(projectDTO.getTitle()))
                project.setTitle(projectDTO.getTitle());
            // Description
            if (!GlobalService.isBlank(projectDTO.getDescription()))
                project.setDescription(projectDTO.getDescription());
            return new CustomResponse(new ModelMapper().map(this.projectRepository.save(project), ProjectDTO.class));
        }
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

    @Override
    public boolean isProjectIdAvailable(String projectId) {
        if (GlobalService.isBlank(projectId) ||
                GlobalService.equalsAnyString(projectId, GlobalService.reservedIds)) return true;
        return this.projectRepository.findByProjectIdEquals(projectId).isEmpty();
    }
}
