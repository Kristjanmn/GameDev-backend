package io.nqa.gamedev.controller;

import io.nqa.gamedev.entity.Script;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.service.IScriptService;
import io.nqa.gamedev.service.global.CookieService;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/script")
public class ScriptController {

    @Autowired
    private IScriptService scriptService;

    @GetMapping(value = "")
    public CustomResponse getAllByProjectId_Cookie(HttpServletRequest request) {
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid cookie", null);
        return this.scriptService.getByProjectId(projectId);
    }

    @GetMapping(value = "getByProject/{projectDatabaseId}")
    public CustomResponse getAllByProjectDatabaseId(@PathVariable String projectDatabaseId) {
        return this.scriptService.getByProject(projectDatabaseId);
    }

    @PostMapping(value = "saveScript")
    public CustomResponse saveScript(@RequestBody Script script, HttpServletRequest request) {
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isNull(script) || GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid parameters", null);
        return this.scriptService.saveScript(script, projectId);
    }
}
