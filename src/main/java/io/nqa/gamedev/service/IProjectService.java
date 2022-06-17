package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ProjectDTO;

import javax.servlet.http.HttpServletResponse;

public interface IProjectService {
    Project getById(String databaseId);

    Project getByProjectId(String projectId);

    CustomResponse getProjectById(String databaseId);

    CustomResponse getProjectByProjectId(String projectId, HttpServletResponse response);

    CustomResponse saveProject(ProjectDTO projectDTO);

    boolean hasScript(String projectId, Script script);

    void saveScripts(String projectId, Script ... scripts);

    boolean isProjectIdAvailable(String projectId);
}
