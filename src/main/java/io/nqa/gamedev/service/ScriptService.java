package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.*;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ScriptDTO;
import io.nqa.gamedev.model.ScriptVariableDTO;
import io.nqa.gamedev.repository.ScriptIndexRepository;
import io.nqa.gamedev.repository.ScriptRepository;
import io.nqa.gamedev.repository.ScriptVariableIndexRepository;
import io.nqa.gamedev.repository.ScriptVariableRepository;
import io.nqa.gamedev.service.global.GUIDGenerator;
import io.nqa.gamedev.service.global.GlobalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ScriptService implements IScriptService {

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private ScriptIndexRepository scriptIndexRepository;

    @Autowired
    private ScriptVariableRepository scriptVariableRepository;

    @Autowired
    private ScriptVariableIndexRepository scriptVariableIndexRepository;

    @Autowired
    private IProjectService projectService;

    @Override
    public String generateGUID() {
        String guid = GUIDGenerator.generate();
        while (this.scriptRepository.findById(guid).isPresent())
            guid = GUIDGenerator.generate();
        return guid;
    }

    /**
     * Create global scripts for basic functionality.
     * This system does NOT work without global scripts.
     */
    @Override
    public void setupGlobalScripts() {
        this.saveGlobalScript(this.newScript("PassTime", true,
                this.newScriptVar(0, "EPassTime","Method"),
                this.newScriptVar(1, "int", "Hours"),
                this.newScriptVar(2, "int", "Minutes")));

        this.saveGlobalScript(this.newScript("GiveItem", true,
                this.newScriptVar(0, "FName","From"),
                this.newScriptVar(1, "FName", "To"),
                this.newScriptVar(2, "FName", "ItemID")));

        this.saveGlobalScript(this.newScript("GiveMoney", true,
                this.newScriptVar(0, "FName","From"),
                this.newScriptVar(1, "FName", "To"),
                this.newScriptVar(2, "int", "Amount")));

        this.saveGlobalScript(this.newScript("ChangeName", true,
                this.newScriptVar(0, "ACharacter*","Character"),
                this.newScriptVar(1, "FString", "NewName")));

        this.saveGlobalScript(this.newScript("UpdateQuestDescription", true,
                this.newScriptVar(0, "FName","QuestID"),
                this.newScriptVar(1, "FText", "Description")));

        this.saveGlobalScript(this.newScript("SetQuestState", true,
                this.newScriptVar(0, "FName","QuestID"),
                this.newScriptVar(1, "EQuestState", "State")));

        this.saveGlobalScript(this.newScript("SetQuestPhase", true,
                this.newScriptVar(0, "FName","QuestID"),
                this.newScriptVar(1, "FName", "Phase")));

        this.saveGlobalScript(this.newScript("SetDialogLocked", true,
                this.newScriptVar(0, "FName","CharacterID"),
                this.newScriptVar(1, "FName", "LineID"),
                this.newScriptVar(2, "boolean", "Locked")));

        this.saveGlobalScript(this.newScript("SetDialogsLocked", true,
                this.newScriptVar(0, "FName","CharacterID"),
                this.newScriptVar(1, "TArray<FName>", "LineIDs"),
                this.newScriptVar(2, "boolean", "Locked")));

        this.saveGlobalScript(this.newScript("SetNextLine", true,
                this.newScriptVar(0, "FName", "CharacterID"),
                this.newScriptVar(1, "FName", "LineID")));
    }

    /**
     * Get global scripts.
     *
     * @return List of global scripts
     */
    @Override
    public List<Script> getGlobalScripts() {
        // Just in case, to make sure they exist..
        this.setupGlobalScripts();

        Optional<List<Script>> optScripts = this.scriptRepository.findAllByGlobalIsTrue();
        return optScripts.orElseGet(ArrayList::new);
    }

    /**
     * Tries to fetch project's scripts.
     * Returns NULL if project was not found.
     *
     * @param projectDatabaseId Desired project id
     * @return List of Scripts assigned to project
     */
    @Override
    public List<Script> getProjectScripts(String projectDatabaseId) {
        Project project = this.projectService.getById(projectDatabaseId);
        if (GlobalService.isNull(project)) return null;
        return project.getScripts();
    }

    @Override
    public CustomResponse getScripts() {
        return new CustomResponse(true, "success", this.getGlobalScripts());
    }

    @Override
    public CustomResponse getById(String scriptId, String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project, project.getScripts()))
            return new CustomResponse("Invalid project", null);
        for (Script script : project.getScripts()) {
            if (scriptId.contentEquals(script.getId())) {
                return new CustomResponse(this.convertScriptToScriptDTO(script));
            }
        }
        return new CustomResponse("No such script was found", null);
    }

    @Override
    public CustomResponse getByProject(String projectDatabaseId) {
        if (GlobalService.isBlank(projectDatabaseId)) return this.getScripts();
        List<Script> allScripts = this.getGlobalScripts();
        List<Script> projectScripts = this.getProjectScripts(projectDatabaseId);
        if (GlobalService.notNull(projectScripts)) allScripts.addAll(projectScripts);
        return new CustomResponse(true, "Found " + allScripts.size() + " scripts", allScripts);
    }

    @Override
    public CustomResponse getByProjectId(String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);
        List<Script> scripts = this.getGlobalScripts();
        scripts.addAll(project.getScripts());
        // Turn scripts into DTO format
        List<ScriptDTO> scriptDTOs = new ArrayList<>();
        for (Script script : scripts) {
            scriptDTOs.add(this.convertScriptToScriptDTO(script));
        }
        return new CustomResponse(scriptDTOs);
    }

    @Override
    public CustomResponse getByScriptId(String scriptId) {
        if (GlobalService.isBlank(scriptId))
            return new CustomResponse(false, "scriptId was not included in request", null);
        Optional<Script> optScript = this.scriptRepository.findById(scriptId);
        if (optScript.isEmpty())
            return new CustomResponse(false, "Could not find script: " + scriptId, null);
        return new CustomResponse(true, "success", optScript.get());
    }

    @Override
    public ScriptDTO convertScriptToScriptDTO(Script script) {
        ScriptDTO scriptDTO = new ModelMapper().map(script, ScriptDTO.class);
        scriptDTO.setVariables(this.sortIndexedVariables_DTO(script.getVariables()));
        return scriptDTO;
    }

    @Override
    public Script convertScriptDTOToScript(ScriptDTO scriptDTO) {
        Script script = new ModelMapper().map(scriptDTO, Script.class);
        List<ScriptVariableIndex> scriptVariables = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (int i = 0; i < scriptDTO.getVariables().size(); i++) {
            ScriptVariableIndex indexedVariable = modelMapper.map(scriptDTO.getVariables().get(i), ScriptVariableIndex.class);
            indexedVariable.setZOrder(i);
            scriptVariables.add(modelMapper.map(indexedVariable, ScriptVariableIndex.class));
        }
        script.setVariables(scriptVariables);
        return script;
    }

    @Override
    public void saveGlobalScript(Script script) {
        if (this.scriptExists(script.getName(), script.isGlobal(), script.getVariables())) return;
        if (GlobalService.notNull(script)) {
            for (ScriptVariableIndex var : script.getVariables()) {
                this.saveScriptVar(var);
            }
            this.scriptRepository.save(script);
        }
    }

    @Override
    public CustomResponse saveScript(ScriptDTO scriptDTO, String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);

        // Init potential blank/null variables
        if (GlobalService.isBlank(scriptDTO.getId())) {
            String guid = GUIDGenerator.generate();
            while (this.scriptIndexRepository.findById(guid).isPresent())
                guid = GUIDGenerator.generate();
            scriptDTO.setId(guid);
        }

        // Convert ScriptDTO -> Script and save
        Script script = this.saveScript(
                this.convertScriptDTOToScript(scriptDTO)
        );
        if (!this.projectService.hasScript(projectId, script))
            this.projectService.saveScripts(projectId, script);
        return new CustomResponse(this.scriptRepository.save(script));
    }

    /**
     * Save Script and all it's variables.
     *
     * @param script Script to save
     * @return saved Script
     */
    @Override
    public Script saveScript(Script script) {
        List<ScriptVariable> variables = new ArrayList<>();
        for (ScriptVariableIndex var : script.getVariables()) {
            variables.add(var.getVariable());
        }
        this.scriptVariableRepository.saveAll(variables);
        return this.scriptRepository.save(script);
    }

    @Override
    public Script newScript(String scriptName, boolean isGlobal, ScriptVariableIndex ... vars) {
        String guid = GUIDGenerator.generate();
        while (this.scriptRepository.findById(guid).isPresent()) guid = GUIDGenerator.generate();
        List<ScriptVariableIndex> variables = new ArrayList<>();
        for (ScriptVariableIndex var : vars) {
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

    @Override
    public ScriptVariableIndex newScriptVar(int index, String varType, String varName) {
        String guid = GUIDGenerator.generate();
        ScriptVariable scriptVariable = this.newScriptVar(varType, varName);
        // Check if such variable already exists.
        Optional<ScriptVariableIndex> optVarIndex = findScriptVarByVariable(scriptVariable);
        if (optVarIndex.isPresent())
            return optVarIndex.get();
        // Create new variable if it does not already exist.
        while (this.scriptVariableRepository.findById(guid).isPresent()) guid = GUIDGenerator.generate();
        return new ScriptVariableIndex(guid, scriptVariable, index);
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
    public void saveScriptVar(ScriptVariableIndex var) {
        this.saveScriptVar(var.getVariable());
        this.scriptVariableIndexRepository.save(var);
    }

    @Override
    public Optional<ScriptVariable> findScriptVarByTypeName(String varType, String varName) {
        return this.scriptVariableRepository.findByVariableTypeEqualsAndVariableNameEquals(varType, varName);
    }

    @Override
    public Optional<ScriptVariableIndex> findScriptVarByVariable(ScriptVariable variable) {
        return this.scriptVariableIndexRepository.findByVariableEquals(variable);
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
    public boolean scriptExists(String scriptName, boolean isGlobal, List<ScriptVariableIndex> variables) {
        // Getting every function with same name and global variables, as repository does not like to compare arrays.
        List<Script> scripts = this.scriptRepository.findAllByNameEqualsAndGlobalEquals(scriptName, isGlobal);
        for (Script script : scripts) {
            // Very odd case below..
            //            // variables.equals(script.getVariables())  returns true
            //            // script.getVariables().equals(variables)  returns false
            if (variables.equals(script.getVariables())) return true;
        }
        return false;
    }

    @Override
    public boolean scriptVariableExists(String varType, String varName) {
        return this.findScriptVarByTypeName(varType, varName).isPresent();
    }

    @Override
    public boolean isScriptNameAvailable(String scriptName, String projectId) {
        if (GlobalService.isBlank(scriptName, projectId))
            return false;
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return false;
        if (GlobalService.isNull(project.getQuests()))
            return true;
        for (Script script : project.getScripts()) {
            if (script.getName().equalsIgnoreCase(scriptName))
                return false;
        }
        return true;
    }

    /**
     * Make sure variables are in correct order.
     *
     * @param indexedVariables List of ScriptVariableIndex
     * @return List of ScriptVariable in correct order
     */
    @Override
    public List<ScriptVariable> sortIndexedVariables(List<ScriptVariableIndex> indexedVariables) {
        indexedVariables.sort(Comparator.comparingInt(ScriptVariableIndex::getZOrder));
        List<ScriptVariable> sortedVariables = new ArrayList<>();
        for (ScriptVariableIndex iVar : indexedVariables) {
            sortedVariables.add(iVar.getVariable());
        }
        return sortedVariables;
    }

    /**
     * Similar to regular sortIndexedVariables,
     * except this returns list of ScriptVariableDTO.
     *
     * @param indexedVariables List of ScriptVariableIndex
     * @return List of ScriptVariableDTO in correct order
     */
    @Override
    public List<ScriptVariableDTO> sortIndexedVariables_DTO(List<ScriptVariableIndex> indexedVariables) {
        indexedVariables.sort(Comparator.comparingInt(ScriptVariableIndex::getZOrder));
        List<ScriptVariableDTO> sortedVariableDTOs = new ArrayList<>();
        for (ScriptVariableIndex iVar : indexedVariables) {
            sortedVariableDTOs.add(
                    new ModelMapper().map(iVar.getVariable(), ScriptVariableDTO.class)
            );
        }
        return sortedVariableDTOs;
    }
}
