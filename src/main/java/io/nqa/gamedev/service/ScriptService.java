package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScriptService implements IScriptService {

    /**
     * Create global scripts for basic functionality.
     * This system does NOT work without global scripts.
     */
    @Override
    public void setupGlobalScripts() {
        //
    }

    @Override
    public List<Script> getGlobalScripts() {
        return null;
    }

    @Override
    public List<Script> getProjectScripts(String projectId) {
        return null;
    }

    @Override
    public CustomResponse getScripts(String projectId) {
        return null;
    }

    @Override
    public CustomResponse getScript(String scriptId) {
        return null;
    }

    @Override
    public CustomResponse saveScript(String projectId, Script script) {
        return null;
    }
}
