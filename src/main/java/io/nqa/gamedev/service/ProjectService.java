package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public boolean hasScript(String projectId, Script script) {
        return this.projectRepository.findByIdAndScriptsContains(projectId, script).isPresent();
    }

    @Override
    public void saveScripts(String projectId, Script ... scripts) {
        Optional<Project> optProject = this.projectRepository.findById(projectId);
        if (optProject.isEmpty()) return;
        Project project = optProject.get();
        List<Script> projectScripts = project.getScripts();
        for (Script script : scripts) {
            if (!projectScripts.contains(script)) projectScripts.add(script);
        }
        project.setScripts(projectScripts);
        this.projectRepository.save(project);
    }
}
