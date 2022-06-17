package io.nqa.gamedev.service.global;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class CookieService {
    private static volatile CookieService cookieService;
    private static final String COOKIE_PROJECT_ID = "PROJECTID";

    private CookieService() {}

    private static void init() {
        if (cookieService == null)
            cookieService = new CookieService();
    }

    public Cookie getCookieByName(Cookie[] cookies, String cookieName) {
        init();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) return cookie;
        }
        return null;
    }

    public Cookie createProjectCookie(String projectId) {
        init();
        Cookie cookie = new Cookie(COOKIE_PROJECT_ID, projectId);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setMaxAge(-1);
        cookie.setPath("/");        // Very important
        return cookie;
    }

    public Cookie deleteProjectCookie() {
        init();
        Cookie cookie = new Cookie(COOKIE_PROJECT_ID, null);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        return cookie;
    }
}
