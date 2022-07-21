package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.entity.ScriptIndex;
import io.nqa.gamedev.entity.ScriptVariable;
import io.nqa.gamedev.entity.ScriptVariableIndex;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.model.ScriptDTO;
import io.nqa.gamedev.model.ScriptVariableDTO;

import java.util.List;
import java.util.Optional;

public interface IScriptService {

    String generateGUID();

    void setupGlobalScripts();

    List<Script> getGlobalScripts();

    List<Script> getProjectScripts(String projectId);

    CustomResponse getScripts();

    CustomResponse getByProject(String projectDatabaseId);    // List<Script>

    CustomResponse getByProjectId(String projectId);

    CustomResponse getByScriptId(String scriptId);      // Script

    ScriptDTO getScriptDTO(Script script);

    void saveGlobalScript(Script script);

    CustomResponse saveScript(Script script, String projectId);

    Script saveScript(Script script);

    Script newScript(String scriptName, boolean isGlobal, ScriptVariableIndex ... vars);

    ScriptVariable newScriptVar(String varType, String varName);

    ScriptVariableIndex newScriptVar(int index, String varType, String varName);

    void saveScriptVar(ScriptVariable variable);

    void saveScriptVar(ScriptVariableIndex variable);

    Optional<ScriptVariable> findScriptVarByTypeName(String varType, String varName);

    Optional<ScriptVariableIndex> findScriptVarByVariable(ScriptVariable variable);

    boolean scriptExists(String scriptName, boolean isGlobal, List<ScriptVariableIndex> variables);

    boolean scriptVariableExists(String varType, String varName);

    boolean isScriptNameAvailable(String scriptName, String projectId);

    List<ScriptVariable> sortIndexedVariables(List<ScriptVariableIndex> indexedVariables);

    List<ScriptVariableDTO> sortIndexedVariables_DTO(List<ScriptVariableIndex> indexedVariables);
}
