package io.nqa.gamedev.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalService implements IGlobalService {
    private static volatile GlobalService globalService;

    private GlobalService() {}

    private static void init() {
        if (globalService == null) globalService = new GlobalService();
    }

    public boolean isNull(Object ... objects) {
        for (Object obj : objects) {
            if (obj == null) return true;
        }
        return false;
    }

    public boolean isBlank(String ... args) {
        for (String arg : args) {
            if (arg == null || arg.trim().isBlank()) return true;
        }
        return false;
    }

    public boolean isEmpty(List ... lists) {
        for (List list : lists) {
            if (list == null || list.isEmpty()) return true;
        }
        return false;
    }
}
