package io.nqa.gamedev.configuration;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.getSessionCookieConfig().setSecure(true);
        servletContext.getSessionCookieConfig().setHttpOnly(false);
    }
}
