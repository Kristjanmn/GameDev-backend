package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Quest;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.QuestRepository;
import io.nqa.gamedev.service.global.GUIDGenerator;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestService implements IQuestService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private IProjectService projectService;

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
        return new CustomResponse(project.getQuests());
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
        if (GlobalService.isNull(quest.getPhases()))
            quest.setPhases(new ArrayList<>());
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
}
