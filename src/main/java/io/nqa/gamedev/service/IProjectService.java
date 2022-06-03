package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;

public interface IProjectService {
    Project getById(String projectId);

    boolean hasScript(String projectId, Script script);

    void saveScripts(String projectId, Script ... scripts);
}
