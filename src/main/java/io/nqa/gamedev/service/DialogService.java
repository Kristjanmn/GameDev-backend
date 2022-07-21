package io.nqa.gamedev.service;

import io.nqa.gamedev.entity.Dialog;
import io.nqa.gamedev.entity.DialogLine;
import io.nqa.gamedev.entity.Project;
import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.repository.DialogLineRepository;
import io.nqa.gamedev.repository.DialogRepository;
import io.nqa.gamedev.service.global.GUIDGenerator;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DialogService implements IDialogService {

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private DialogLineRepository dialogLineRepository;

    @Autowired
    private IProjectService projectService;

    @Override
    public String generateGUID() {
        String guid = GUIDGenerator.generate();
        while (this.dialogRepository.findById(guid).isPresent())
            guid = GUIDGenerator.generate();
        return guid;
    }

    @Override
    public String generateGUID_line() {
        String guid = GUIDGenerator.generate();
        while (this.dialogLineRepository.findById(guid).isPresent())
            guid = GUIDGenerator.generate();
        return guid;
    }

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

    @Override
    public CustomResponse getByProjectId(String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Invalid project", null);
        return new CustomResponse(project.getDialogs());
    }

    @Override
    public CustomResponse saveDialog(Dialog dialog, String projectId) {
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return new CustomResponse("Project not found");
        if (GlobalService.isBlank(dialog.getId()))
            dialog.setId(this.generateGUID());
        if (GlobalService.isNull(dialog.getLines()))
            dialog.setLines(new ArrayList<>());
        if (!this.doesDialogLineExist(dialog, "INIT")) {
            DialogLine initLine = new DialogLine(this.generateGUID_line(), "INIT", "",
                    new ArrayList<>(), null, 0, "", new ArrayList<>(), false,
                    "This line is always first and is primarily used for scripts.", 0);
            List<DialogLine> lines = dialog.getLines();
            lines.add(0, initLine);
            dialog.setLines(lines);
        }

        List<Dialog> dialogs = new ArrayList<>();
        if (GlobalService.notNull(project.getDialogs()))
            dialogs = project.getDialogs();
        if (!dialogs.contains(dialog))
            dialogs.add(dialog);
        project.setDialogs(dialogs);
        //this.dialogLineRepository.saveAll(dialog.getLines());
        projectService.saveProject(project);
        return new CustomResponse(dialog);
    }

    @Override
    public Dialog saveDialog(Dialog dialog) {
        return this.dialogRepository.save(dialog);
    }

    @Override
    public boolean isDialogIdAvailable(String dialogId, String projectId) {
        if (GlobalService.isBlank(dialogId, projectId))
            return false;
        Project project = this.projectService.getByProjectId(projectId);
        if (GlobalService.isNull(project))
            return false;
        if (GlobalService.isNull(project.getDialogs()))
            return true;
        for (Dialog dialog : project.getDialogs()) {
            if (dialog.getDialogId().equalsIgnoreCase(dialogId))
                return false;
        }
        return true;
    }

    @Override
    public boolean doesDialogLineExist(Dialog dialog, String lineId) {
        for (DialogLine line : dialog.getLines()) {
            if (line.getLineId().equalsIgnoreCase(lineId)) return true;
        }
        return false;
    }
}
