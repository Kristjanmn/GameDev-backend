package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Quest;
import io.nqa.gamedev.entity.QuestPhase;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.QuestPhaseRepository;
import io.nqa.gamedev.repository.QuestRepository;
import io.nqa.gamedev.service.global.GUIDGenerator;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class QuestService implements IQuestService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestPhaseRepository questPhaseRepository;

    @Autowired
    private IProjectService projectService;

    @Override
    public String generateGUID() {
        String guid = GUIDGenerator.generate();
        while (this.questRepository.findById(guid).isPresent())
            guid = GUIDGenerator.generate();
        return guid;
    }

    @Override
    public String generateGUID_phase() {
        String guid = GUIDGenerator.generate();
        while (this.questPhaseRepository.findById(guid).isPresent())
            guid = GUIDGenerator.generate();
        return guid;
    }

    @Override
    public CustomResponse getById(String questId, String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        // May cause potential problems, as project.getQuests might be NULL
        // (even tho everything leading up to this point makes sure it can't be a NULL).
        if (GlobalService.isNull(project, project.getQuests()))
            return new CustomResponse("Invalid project", null);
        for (Quest quest : project.getQuests()) {
            if (questId.contentEquals(quest.getId())){
                List<QuestPhase> phases = quest.getPhases();
                phases.sort(Comparator.comparingInt(QuestPhase::getZOrder));
                quest.setPhases(phases);
                return new CustomResponse(quest);
            }
        }
        return new CustomResponse("No such quest was found", null);
    }

    @Override
    public CustomResponse getByProject(String projectDatabaseId) {
        if (GlobalService.isBlank(projectDatabaseId))
            return new CustomResponse("Invalid project database ID");
        Project project = this.projectService.getById(projectDatabaseId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project database ID", null);
        List<Quest> quests = project.getQuests();
        if (GlobalService.isNull(quests))
            return new CustomResponse("Project exists, but quests was null", null);
        return new CustomResponse(quests);
    }

    @Override
    public CustomResponse getByProjectId(String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);
        // Sort QuestPhases for quests
        List<Quest> quests = new ArrayList<>();
        for (Quest quest : project.getQuests()) {
            List<QuestPhase> phases = quest.getPhases();
            phases.sort(Comparator.comparingInt(QuestPhase::getZOrder));
            quest.setPhases(phases);
            quests.add(quest);
        }
        return new CustomResponse(quests);
    }

    @Override
    public CustomResponse saveQuest(Quest quest, String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);

        // Init potential blank/null variables
        if (GlobalService.isBlank(quest.getId())) {
            String guid = GUIDGenerator.generate();
            while (this.questRepository.findById(guid).isPresent())
                guid = GUIDGenerator.generate();
            quest.setId(guid);
        }

        List<QuestPhase> phases = new ArrayList<>();
        if (GlobalService.notNull(quest.getPhases()))
            phases = quest.getPhases();
        quest.setPhases(this.saveQuestPhases(phases));
        /*if (GlobalService.isNull(quest.getPhases())){
            QuestPhase initPhase = new QuestPhase();
            quest.setPhases(new ArrayList<>());
        }*/
        if (GlobalService.isNull(quest.getComment()))
            quest.setComment("");

        List<Quest> quests = new ArrayList<>();
        if (GlobalService.notNull(project.getQuests()))
            quests = project.getQuests();
        quest = this.saveQuest(quest);
        if (!quests.contains(quest))
            quests.add(quest);
        project.setQuests(quests);
        projectService.saveProject(project);
        return new CustomResponse(quest);
    }

    @Override
    public Quest saveQuest(Quest quest) {
        return this.questRepository.save(quest);
    }

    @Override
    public List<QuestPhase> saveQuestPhases(QuestPhase ... questPhases) {
        List<QuestPhase> phases = new ArrayList<>(Arrays.asList(questPhases));
        return this.saveQuestPhases(phases);
    }

    @Override
    public List<QuestPhase> saveQuestPhases(List<QuestPhase> questPhases) {
        List<QuestPhase> savedPhases = new ArrayList<>();
        boolean containsInit = false;
        // Start from 1 so that INIT zOrder can
        // be set to 0 to ensure it being on top.
        int index = 1;
        for (QuestPhase phase : questPhases) {
            // Make sure there is only one INIT, must be first!
            if (containsInit && phase.getPhaseId().equalsIgnoreCase("INIT"))
                phase.setPhaseId("INIT_DUPLICATE");
            if (phase.getPhaseId().equalsIgnoreCase("INIT")) {
                containsInit = true;
                phase.setZOrder(0);
            } else phase.setZOrder(index);
            if (GlobalService.isBlank(phase.getId()))
                phase.setId(this.generateGUID_phase());
            savedPhases.add(phase);
            index++;
        }

        // Ensure that INIT line is present
        if (!containsInit) {
            QuestPhase initPhase = new QuestPhase();
            initPhase.setId(this.generateGUID_phase());
            initPhase.setPhaseId("INIT");
            initPhase.setDescription("");
            initPhase.setScript(new ArrayList<>());
            initPhase.setComment("INIT must be first line and is only used for running scripts.");
            initPhase.setZOrder(0);
            savedPhases.add(0, initPhase);
        }
        return this.questPhaseRepository.saveAll(savedPhases);
    }

    @Override
    public boolean isQuestIdAvailable(String questId, String projectId) {
        if (GlobalService.isBlank(questId, projectId))
            return false;
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return false;
        if (GlobalService.isNull(project.getQuests()))
            return true;
        for (Quest quest : project.getQuests()) {
            if (quest.getQuestId().equalsIgnoreCase(questId))
                return false;
        }
        return true;
    }

    @Override
    public boolean isQuestIdAvailable(String questDatabaseId, String questId, String projectId) {
        if (GlobalService.isBlank(questDatabaseId, questId, projectId))
            return false;
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return false;
        if (GlobalService.isNull(project.getQuests()))
            return true;
        for (Quest quest : project.getQuests()) {
            if (!GlobalService.equalsAnyString(questDatabaseId, quest.getId()) &&
                    GlobalService.equalsAnyString(questId, quest.getQuestId()))
                return false;
        }
        return true;
    }

    @Override
    public boolean doesQuestPhaseExist(Quest quest, String phaseId) {
        for (QuestPhase phase : quest.getPhases()) {
            if (phase.getPhaseId().equalsIgnoreCase(phaseId)) return true;
        }
        return false;
    }
}
