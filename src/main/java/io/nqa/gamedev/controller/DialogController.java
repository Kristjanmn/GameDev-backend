package io.nqa.gamedev.controller;

import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.service.IDialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dialog")
public class DialogController {

    @Autowired
    private IDialogService dialogService;

    @GetMapping(value = "getById/{databaseId}")
    public CustomResponse getOneById(@PathVariable String databaseId) {
        return this.dialogService.getById(databaseId);
    }

    @GetMapping(value = "getByDialogId/{dialogId}")
    public CustomResponse getOneByDialogId(@PathVariable String dialogId) {
        return this.dialogService.getByDialogId(dialogId);
    }

    @GetMapping(value = "getByProject/{projectDatabaseId}")
    public CustomResponse getAllByProject(@PathVariable String projectDatabaseId) {
        return this.dialogService.getByProject(projectDatabaseId);
    }
}
