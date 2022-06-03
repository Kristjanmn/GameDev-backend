package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
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
        this.saveGlobalScript(this.newScript("PassTime", true,
                this.newScriptVar("EPassTime","Method"),
                this.newScriptVar("int", "Hours"),
                this.newScriptVar("int", "Minutes")));

        this.saveGlobalScript(this.newScript("GiveItem", true,
                this.newScriptVar("FName","From"),
                this.newScriptVar("FName", "To"),
                this.newScriptVar("FName", "ItemID")));

        this.saveGlobalScript(this.newScript("GiveMoney", true,
                this.newScriptVar("FName","From"),
                this.newScriptVar("FName", "To"),
                this.newScriptVar("int", "Amount")));

        this.saveGlobalScript(this.newScript("ChangeName", true,
                this.newScriptVar("ACharacter*","Character"),
                this.newScriptVar("FString", "NewName")));

        this.saveGlobalScript(this.newScript("UpdateQuestDescription", true,
                this.newScriptVar("FName","QuestID"),
                this.newScriptVar("FText", "Description")));

        this.saveGlobalScript(this.newScript("SetQuestState", true,
                this.newScriptVar("FName","QuestID"),
                this.newScriptVar("EQuestState", "State")));

        this.saveGlobalScript(this.newScript("SetQuestPhase", true,
                this.newScriptVar("FName","QuestID"),
                this.newScriptVar("FName", "Phase")));

        this.saveGlobalScript(this.newScript("SetDialogLocked", true,
                this.newScriptVar("FName","CharacterID"),
                this.newScriptVar("FName", "LineID"),
                this.newScriptVar("boolean", "Locked")));

        this.saveGlobalScript(this.newScript("SetDialogsLocked", true,
                this.newScriptVar("FName","CharacterID"),
                this.newScriptVar("TArray<FName>", "LineIDs"),
                this.newScriptVar("boolean", "Locked")));

        this.saveGlobalScript(this.newScript("SetNextLine", true,
                this.newScriptVar("FName", "CharacterID"),
                this.newScriptVar("FName", "LineID")));
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

    /**
     * Tries to fetch project's scripts.
     * Returns NULL if project was not found.
     *
     * @param projectId Desired project id
     * @return List of Scripts assigned to project
     */
    @Override
    public List<Script> getProjectScripts(String projectId) {
        Project project = this.projectService.getById(projectId);
        if (GlobalService.isNull(project)) return null;
        return project.getScripts();
    }

    @Override
    public CustomResponse getScripts(String projectId) {
        List<Script> allScripts = this.getGlobalScripts();
        List<Script> projectScripts = this.getProjectScripts(projectId);
        if (GlobalService.notNull(projectScripts)) allScripts.addAll(projectScripts);
        return new CustomResponse(true, "Found " + allScripts.size() + " scripts", allScripts);
    }

    @Override
    public CustomResponse getScript(String scriptId) {
        return null;
    }

    @Override
    public void saveGlobalScript(Script script) {
        if (this.scriptExists(script.getName(), script.isGlobal(), script.getVariables())) return;
        if (GlobalService.notNull(script)) {
            for (ScriptVariable var : script.getVariables()) {
                this.saveScriptVar(var);
            }
            this.scriptRepository.save(script);
        }
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

    /**
     * Construct ScriptVariable from given variables.
     * Returns already created ScriptVariables, if one exists.
     *
     * @param varType Variable type, like FName
     * @param varName Variable name, like ItemID
     * @return ScriptVariable containing these values
     */
    @Override
    public ScriptVariable newScriptVar(String varType, String varName) {
        // Check if such variable already exists.
        Optional<ScriptVariable> optVar = findScriptVarByTypeName(varType, varName);
        if (optVar.isPresent()) return optVar.get();
        // Create new variable if it does not already exist.
        String guid = GUIDGenerator.generate();
        while (this.scriptVariableRepository.findById(guid).isPresent()) guid = GUIDGenerator.generate();
        return new ScriptVariable(guid, varType, varName);
    }

    /**
     * Save new script variable, if it does not exist already.
     *
     * @param var script variable
     */
    @Override
    public void saveScriptVar(ScriptVariable var) {
        if (!this.scriptVariableExists(var.getVariableType(), var.getVariableName()))
            this.scriptVariableRepository.save(var);
    }

    @Override
    public Optional<ScriptVariable> findScriptVarByTypeName(String varType, String varName) {
        return this.scriptVariableRepository.findByVariableTypeEqualsAndVariableNameEquals(varType, varName);
    }

    /**
     * Check if script with same name and all same variables exists.
     *
     * @param scriptName Script name
     * @param isGlobal Is script global
     * @param variables List of ScriptVariable
     * @return Does script exist?
     */
    @Override
    public boolean scriptExists(String scriptName, boolean isGlobal, List<ScriptVariable> variables) {
        Optional<Script> optScript = this.scriptRepository.findByNameEqualsAndGlobalEquals(scriptName, isGlobal);
        return (optScript.isPresent() && optScript.get().getVariables().equals(variables));
    }

    @Override
    public boolean scriptVariableExists(String varType, String varName) {
        return this.findScriptVarByTypeName(varType, varName).isPresent();
    }
}
