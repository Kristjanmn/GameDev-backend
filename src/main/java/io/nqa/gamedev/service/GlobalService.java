package io.nqa.gamedev.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalService {
    private static volatile GlobalService globalService;

    private GlobalService() {}

    private static void init() {
        if (globalService == null) globalService = new GlobalService();
    }

    public static boolean isNull(Object ... objects) {
        init();
        for (Object obj : objects) {
            if (obj == null) return true;
        }
        return false;
    }

    public static boolean isBlank(String ... args) {
        init();
        for (String arg : args) {
            if (arg == null || arg.trim().isBlank()) return true;
        }
        return false;
    }

    public static boolean isEmpty(List ... lists) {
        init();
        for (List list : lists) {
            if (list == null || list.isEmpty()) return true;
        }
        return false;
    }
}
