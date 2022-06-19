package io.nqa.gamedev.controller;

import io.nqa.gamedev.entity.Dialog;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.service.IDialogService;
import io.nqa.gamedev.service.global.CookieService;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/dialog")
public class DialogController {

    @Autowired
    private IDialogService dialogService;

    @GetMapping(value = "")
    public CustomResponse getAllByProjectId_Cookie(HttpServletRequest request) {
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid cookie", null);
        return this.dialogService.getByProjectId(projectId);
    }

    @GetMapping(value = "getById/{databaseId}")
    public CustomResponse getOneById(@PathVariable String databaseId) {
        return this.dialogService.getById(databaseId);
    }

    @GetMapping(value = "getByDialogId/{dialogId}")
    public CustomResponse getOneByDialogId(@PathVariable String dialogId) {
        return this.dialogService.getByDialogId(dialogId);
    }

    @GetMapping(value = "getByProject/{projectDatabaseId}")
    public CustomResponse getAllByProjectDatabaseId(@PathVariable String projectDatabaseId) {
        if (GlobalService.isBlank(projectDatabaseId))
            return new CustomResponse("Invalid parameters", null);
        return this.dialogService.getByProject(projectDatabaseId);
    }

    @PostMapping(value = "saveDialog")
    public CustomResponse saveDialog(@RequestBody Dialog dialog, HttpServletRequest request) {
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isNull(dialog) || GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid parameters", null);
        return this.dialogService.saveDialog(dialog, projectId);
    }

    @GetMapping(value = "checkIdAvailable/{dialogId}")
    public CustomResponse checkDialogIdAvailable(@PathVariable String dialogId, HttpServletRequest request) {
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(dialogId, projectId))
            return new CustomResponse("Invalid parameters", null);
        if (this.dialogService.isDialogIdAvailable(dialogId, projectId))
            return new CustomResponse("dialogId is available", dialogId);
        return new CustomResponse("dialogId not available", null);
    }
}
