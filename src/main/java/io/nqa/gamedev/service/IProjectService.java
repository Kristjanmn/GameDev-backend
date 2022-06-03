package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;

public interface IProjectService {
    Project getById(String projectId);

    CustomResponse getProjectById(String projectId);

    boolean hasScript(String projectId, Script script);

    void saveScripts(String projectId, Script ... scripts);
}
