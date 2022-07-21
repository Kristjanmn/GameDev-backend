package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Quest;
import io.nqa.gamedev.entity.QuestPhase;
import io.nqa.gamedev.model.CustomResponse;

import java.util.List;

public interface IQuestService {

    String generateGUID();

    String generateGUID_phase();

    CustomResponse getById(String questId, String projectId);

    CustomResponse getByProject(String projectDatabaseId);

    CustomResponse getByProjectId(String projectId);

    CustomResponse saveQuest(Quest quest, String projectId);

    Quest saveQuest(Quest quest);

    List<QuestPhase> saveQuestPhases(QuestPhase ... questPhases);

    List<QuestPhase> saveQuestPhases(List<QuestPhase> questPhases);

    boolean isQuestIdAvailable(String questId, String projectId);

    boolean isQuestIdAvailable(String questDatabaseId, String questId, String projectId);

    boolean doesQuestPhaseExist(Quest quest, String phaseId);
}
