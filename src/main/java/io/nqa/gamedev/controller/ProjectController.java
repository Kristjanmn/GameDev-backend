package io.nqa.gamedev.controller;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ProjectDTO;
import io.nqa.gamedev.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "getById/{databaseId}")
    public CustomResponse getProjectById(@PathVariable String databaseId) {
        return this.projectService.getProjectById(databaseId);
    }

    @GetMapping(value = "getByProjectId/{projectId}")
    public CustomResponse getProjectByProjectId(@PathVariable String projectId) {
        return this.projectService.getProjectByProjectId(projectId);
    }

    @PostMapping(value = "saveProject")
    public CustomResponse saveProject(@RequestBody ProjectDTO projectDTO) {
        return this.projectService.saveProject(projectDTO);
    }
}
