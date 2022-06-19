package io.nqa.gamedev.controller;

import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ProjectDTO;
import io.nqa.gamedev.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @GetMapping(value = "getById/{databaseId}")
    public CustomResponse getById(@PathVariable String databaseId, HttpServletResponse response) {
        return this.projectService.getProjectById(databaseId, response);
    }

    @GetMapping(value = "getByProjectId/{projectId}")
    public CustomResponse getByProjectId(@PathVariable String projectId, HttpServletResponse response) {
        return this.projectService.getProjectByProjectId(projectId, response);
    }

    @PostMapping(value = "saveProject")
    public CustomResponse saveProject(@RequestBody ProjectDTO projectDTO) {
        return this.projectService.saveProject(projectDTO);
    }

    @GetMapping(value = "checkIdAvailable/{projectId}")
    public CustomResponse checkProjectIdAvailable(@PathVariable String projectId) {
        if (this.projectService.isProjectIdAvailable(projectId))
            return new CustomResponse("is available", projectId);
        return new CustomResponse("not available", null);
    }
}
