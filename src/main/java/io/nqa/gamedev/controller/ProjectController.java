package io.nqa.gamedev.controller;

import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "getProjectById/{projectId}")
    public CustomResponse getProjectById(@PathVariable String projectId) {
        return this.projectService.getProjectById(projectId);
    }
}
