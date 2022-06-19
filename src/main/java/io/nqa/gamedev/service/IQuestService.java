package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Quest;
import io.nqa.gamedev.model.CustomResponse;

public interface IQuestService {

    CustomResponse getByProject(String projectDatabaseId);

    CustomResponse getByProjectId(String projectId);

    boolean isQuestIdAvailable(String questId, String projectId);

    CustomResponse saveQuest(Quest quest, String projectId);

    Quest saveQuest(Quest quest);
}
