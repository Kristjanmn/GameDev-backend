package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;

import java.util.List;

public interface IScriptService {

    void setupGlobalScripts();

    List<Script> getGlobalScripts();

    List<Script> getProjectScripts(String projectId);

    CustomResponse getScripts(String projectId);    // List<Script>

    CustomResponse getScript(String scriptId);      // Script

    CustomResponse saveScript(String projectId, Script script);
}
