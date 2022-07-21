package io.nqa.gamedev.controller;

import io.nqa.gamedev.entity.Cue;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.service.ICueService;
import io.nqa.gamedev.service.global.CookieService;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cue")
public class CueController {

    @Autowired
    private ICueService cueService;

    @GetMapping(value = "")
    public CustomResponse getAllByProjectId_Cookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid cookie", null);
        return this.cueService.getByProjectId(projectId);
    }

    @GetMapping(value = "getByProject/{projectDatabaseId}")
    public CustomResponse getAllByProjectDatabaseId(@PathVariable String projectDatabaseId) {
        if (GlobalService.isBlank(projectDatabaseId))
            return new CustomResponse("Invalid parameters", null);
        return this.cueService.getByProject(projectDatabaseId);
    }

    @GetMapping(value = "checkIdAvailable/{cueId}")
    public CustomResponse checkCueIdAvailable(@PathVariable String cueId, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(cueId, projectId))
            return new CustomResponse("Invalid parameters", null);
        if (this.cueService.isCueIdAvailable(cueId, projectId))
            return new CustomResponse("id is available", cueId);
        return new CustomResponse("id not available", null);
    }

    @PostMapping(value = "saveCue")
    public CustomResponse saveCue(@RequestBody Cue cue, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isNull(cue) || GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid parameters", null);
        return this.cueService.saveCue(cue, projectId);
    }
}
