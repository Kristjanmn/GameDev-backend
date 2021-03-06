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
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid cookie", null);
        return this.scriptService.getByProjectId(projectId);
    }

    @GetMapping(value = "getByProject/{projectDatabaseId}")
    public CustomResponse getAllByProjectDatabaseId(@PathVariable String projectDatabaseId) {
        return this.scriptService.getByProject(projectDatabaseId);
    }

    @GetMapping(value = "checkIdAvailable/{scriptName}")
    public CustomResponse checkScriptNameAvailable(@PathVariable String scriptName, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(scriptName, projectId))
            return new CustomResponse("Invalid parameters", null);
        boolean isAvailable = this.scriptService.isScriptNameAvailable(scriptName, projectId);
        if (isAvailable) return new CustomResponse("script name is available", scriptName);
        return new CustomResponse("script name not available", null);
    }

    @PostMapping(value = "saveScript")
    public CustomResponse saveScript(@RequestBody Script script, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isNull(script) || GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid parameters", null);
        return this.scriptService.saveScript(script, projectId);
    }
}
