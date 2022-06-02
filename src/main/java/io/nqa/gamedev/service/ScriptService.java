package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.entity.ScriptVariable;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.ScriptRepository;
import io.nqa.gamedev.repository.ScriptVariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScriptService implements IScriptService {

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private ScriptVariableRepository scriptVariableRepository;

    @Autowired
    private IProjectService projectService;

    /**
     * Create global scripts for basic functionality.
     * This system does NOT work without global scripts.
     */
    @Override
    public void setupGlobalScripts() {
        List<ScriptVariable> variables = new ArrayList<>();
        variables.add(this.newScriptVar("FName", "CharacterID"));
        variables.add(this.newScriptVar("FName", "LineID"));
        for (ScriptVariable scriptVariable : variables) {
            this.scriptVariableRepository.save(scriptVariable);
        }

        List<Script> scripts = new ArrayList<>();
        scripts.add(new Script(GUIDGenerator.generate(), true, "SetNextLine", variables));
        for (Script script : scripts) {
            this.saveGlobalScript(script);
        }
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
    public void saveGlobalScript(Script script) {
        this.scriptRepository.save(script);
    }

    @Override
    public CustomResponse saveScript(String projectId, Script script) {
        if (!this.projectService.hasScript(projectId, script)) this.projectService.saveScripts(projectId, script);
        this.scriptRepository.save(script);
        return null;
    }

    @Override
    public Script newScript(String scriptName, boolean isGlobal, ScriptVariable ... vars) {
        String guid = GUIDGenerator.generate();
        while (this.scriptRepository.findById(guid).isPresent()) guid = GUIDGenerator.generate();
        List<ScriptVariable> variables = new ArrayList<>();
        for (ScriptVariable var : vars) {
            if (var != null) variables.add(var);
        }
        return new Script(guid, isGlobal, scriptName, variables);
    }

    @Override
    public ScriptVariable newScriptVar(String varType, String varName) {
        String guid = GUIDGenerator.generate();
        while (this.scriptVariableRepository.findById(guid).isPresent()) guid = GUIDGenerator.generate();
        return new ScriptVariable(guid, varType, varName);
    }

    @Override
    public boolean scriptVariableExists(String varType, String varName) {
        Optional<ScriptVariable> optVar = this.scriptVariableRepository.findByVariableTypeEqualsAndVariableNameEquals(varType, varName);
        return optVar.isPresent();
    }
}
