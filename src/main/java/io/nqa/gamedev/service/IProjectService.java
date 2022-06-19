package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ProjectDTO;

import javax.servlet.http.HttpServletResponse;

public interface IProjectService {

    Project initProjectArrays(Project project);

    Project getById(String databaseId);

    Project getByProjectId(String projectId);

    CustomResponse getProjectById(String databaseId, HttpServletResponse response);

    CustomResponse getProjectByProjectId(String projectId, HttpServletResponse response);

    CustomResponse saveProject(ProjectDTO projectDTO);

    Project saveProject(Project project);

    boolean hasScript(String projectId, Script script);

    void saveScripts(String projectId, Script ... scripts);

    boolean isProjectIdAvailable(String projectId);
}
