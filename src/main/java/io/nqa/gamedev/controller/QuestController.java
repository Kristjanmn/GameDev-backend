package io.nqa.gamedev.controller;

import io.nqa.gamedev.entity.Quest;
import io.nqa.gamedev.model.CustomResponse;
import io.nqa.gamedev.service.IQuestService;
import io.nqa.gamedev.service.global.CookieService;
import io.nqa.gamedev.service.global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/quest")
public class QuestController {

    @Autowired
    private IQuestService questService;

    @GetMapping(value = "")
    public CustomResponse getAllByProjectId_Cookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid cookie", null);
        return this.questService.getByProjectId(projectId);
    }

    @GetMapping(value = "getByProject/{projectDatabaseId}")
    public CustomResponse getAllByProjectDatabaseId(@PathVariable String projectDatabaseId) {
        return questService.getByProject(projectDatabaseId);
    }

    @GetMapping(value = "getById/{questDatabaseId}")
    public CustomResponse getByQuestDatabaseId(@PathVariable String questDatabaseId, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(questDatabaseId, projectId))
            return new CustomResponse("Invalid parameters", null);
        return questService.getById(questDatabaseId, projectId);
    }

    @GetMapping(value = "checkIdAvailable/{questId}")
    public CustomResponse checkQuestIdAvailable(@PathVariable String questId, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(questId, projectId))
            return new CustomResponse("Invalid parameters", null);
        boolean isAvailable = this.questService.isQuestIdAvailable(questId, projectId);
        if (isAvailable) return new CustomResponse("questId is available", questId);
        return new CustomResponse("questId not available", null);
    }

    @GetMapping(value = "checkIdAvailable/{questDatabaseId}/{questId}")
    public CustomResponse checkQuestIdAvailable(@PathVariable String questDatabaseId, @PathVariable String questId, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isBlank(questDatabaseId, questId, projectId))
            return new CustomResponse("Invalid parameters", null);
        boolean isAvailable = this.questService.isQuestIdAvailable(questDatabaseId, questId, projectId);
        if (isAvailable) return new CustomResponse("questId is available", questId);
        return new CustomResponse("questId not available", null);
    }

    @PostMapping(value = "saveQuest")
    public CustomResponse saveQuest(@RequestBody Quest quest, HttpServletRequest request) {
        if (request.getCookies() == null)
            return CookieService.noCookiesInRequest();
        String projectId = CookieService.getCookieByName(request.getCookies(), CookieService.COOKIE_PROJECT_ID).getValue();
        if (GlobalService.isNull(quest) || GlobalService.isBlank(projectId))
            return new CustomResponse("Invalid parameters", null);
        return this.questService.saveQuest(quest, projectId);
    }
}
