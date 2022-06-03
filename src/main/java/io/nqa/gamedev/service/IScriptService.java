package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.entity.ScriptVariable;
import io.nqa.gamedev.model.CustomResponse;

import java.util.List;
import java.util.Optional;

public interface IScriptService {

    void setupGlobalScripts();

    List<Script> getGlobalScripts();

    List<Script> getProjectScripts(String projectId);

    CustomResponse getScripts();

    CustomResponse getScripts(String projectId);    // List<Script>

    CustomResponse getScript(String scriptId);      // Script

    void saveGlobalScript(Script script);

    CustomResponse saveScript(String projectId, Script script);

    Script newScript(String scriptName, boolean isGlobal, ScriptVariable ... vars);

    ScriptVariable newScriptVar(String varType, String varName);

    void saveScriptVar(ScriptVariable variable);

    Optional<ScriptVariable> findScriptVarByTypeName(String varType, String varName);

    boolean scriptExists(String scriptName, boolean isGlobal, List<ScriptVariable> variables);

    boolean scriptVariableExists(String varType, String varName);
}
