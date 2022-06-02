package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Script;

public interface IProjectService {
    boolean hasScript(String projectId, Script script);

    void saveScripts(String projectId, Script ... scripts);
}
