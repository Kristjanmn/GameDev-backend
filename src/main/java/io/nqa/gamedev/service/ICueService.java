package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Cue;
import io.nqa.gamedev.model.CustomResponse;

public interface ICueService {

    CustomResponse getByProject(String projectDatabaseId);

    CustomResponse getByProjectId(String projectId);

    boolean isCueIdAvailable(String cueId, String projectId);

    CustomResponse saveCue(Cue cue, String projectId);

    Cue saveCue(Cue cue);
}
