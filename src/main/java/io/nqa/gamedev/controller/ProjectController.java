package io.nqa.gamedev.controller;

import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ProjectDTO;
import io.nqa.gamedev.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @GetMapping(value = "getById/{databaseId}")
    public CustomResponse getById(@PathVariable String databaseId) {
        return this.projectService.getProjectById(databaseId);
    }

    @GetMapping(value = "getByProjectId/{projectId}")
    public CustomResponse getByProjectId(@PathVariable String projectId) {
        return this.projectService.getProjectByProjectId(projectId);
    }

    @PostMapping(value = "saveProject")
    public CustomResponse saveProject(@RequestBody ProjectDTO projectDTO) {
        return this.projectService.saveProject(projectDTO);
    }
}
