package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Dialog;
import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.DialogRepository;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DialogService implements IDialogService {

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private IProjectService projectService;

    @Override
    public CustomResponse getById(String databaseId) {
        if (GlobalService.isBlank(databaseId))
            return new CustomResponse("Invalid database ID", null);
        Optional<Dialog> optDialog = this.dialogRepository.findById(databaseId);
        if (optDialog.isEmpty())
            return new CustomResponse("Could not find dialog", null);
        return new CustomResponse(optDialog.get());
    }

    @Override
    public CustomResponse getByDialogId(String dialogId) {
        if (GlobalService.isBlank(dialogId))
            return new CustomResponse("Invalid ID", null);
        Optional<Dialog> optDialog = this.dialogRepository.findByDialogIdEquals(dialogId);
        if (optDialog.isEmpty())
            return new CustomResponse("Could not find dialog", null);
        return new CustomResponse(optDialog.get());
    }

    @Override
    public CustomResponse getByProject(String projectDatabaseId) {
        Project project = this.projectService.getById(projectDatabaseId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project database ID", null);
        List<Dialog> dialogs = project.getDialogs();
        if (GlobalService.isNull(dialogs))
            return new CustomResponse("Project exists, but dialogs was null", null);
        return new CustomResponse(dialogs);
    }
}
