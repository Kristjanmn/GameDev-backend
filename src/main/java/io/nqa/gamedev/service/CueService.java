package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Cue;
import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.CueRepository;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CueService implements ICueService {

    @Autowired
    private CueRepository cueRepository;

    @Autowired
    private IProjectService projectService;

    @Override
    public CustomResponse getByProject(String projectDatabaseId) {
        Project project = this.projectService.getById(projectDatabaseId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);
        List<Cue> cues = project.getCues();
        if (GlobalService.isNull(cues))
            return new CustomResponse("Project exists, but cues was null", null);
        return new CustomResponse(cues);
    }

    @Override
    public CustomResponse getByProjectId(String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);
        return new CustomResponse(project.getCues());
    }

    @Override
    public boolean isCueIdAvailable(String cueId, String projectId) {
        if (GlobalService.isBlank(cueId, projectId))
            return false;
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return false;
        if (GlobalService.isNull(project.getCues()))
            return true;
        for (Cue cue : project.getCues()) {
            if (cue.getCueId().equalsIgnoreCase(cueId))
                return false;
        }
        return true;
    }

    @Override
    public CustomResponse saveCue(Cue cue, String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);
        List<Cue> cues = new ArrayList<>();
        if (GlobalService.notNull(project.getCues()))
            cues = project.getCues();
        if (!cues.contains(cue))
            cues.add(cue);
        project.setCues(cues);
        this.projectService.saveProject(project);
        return new CustomResponse(cue);
    }

    @Override
    public Cue saveCue(Cue cue) {
        return this.cueRepository.save(cue);
    }
}
