package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScriptService implements IScriptService {

    @Autowired
    private ScriptRepository scriptRepository;

    /**
     * Create global scripts for basic functionality.
     * This system does NOT work without global scripts.
     */
    @Override
    public void setupGlobalScripts() {
        //
    }

    /**
     * Get global scripts.
     *
     * @return List of global scripts
     */
    @Override
    public List<Script> getGlobalScripts() {
        Optional<List<Script>> optScripts = this.scriptRepository.findAllByGlobalIsTrue();
        return optScripts.orElseGet(ArrayList::new);
    }

    @Override
    public List<Script> getProjectScripts(String projectId) {
        return null;
    }

    @Override
    public CustomResponse getScripts(String projectId) {
        this.getGlobalScripts();
        return new CustomResponse(false, "FUNCTION NOT COMPLETE", this.getGlobalScripts());
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
