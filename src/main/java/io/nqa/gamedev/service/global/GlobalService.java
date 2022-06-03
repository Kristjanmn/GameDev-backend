package io.nqa.gamedev.service.global;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalService {
    private static volatile GlobalService globalService;

    private GlobalService() {}

    private static void init() {
        if (globalService == null) globalService = new GlobalService();
    }

    /**
     * Check if all the booleans are true.
     *
     * @param bools List of booleans
     * @return Are all booleans true?
     */
    public static boolean isTrue(boolean ... booleans) {
        init();
        for (boolean bool : booleans) if (isNull(bool) || !bool) return false;
        return true;
    }

    public static boolean isNull(Object ... objects) {
        init();
        for (Object obj : objects) {
            if (obj == null) return true;
        }
        return false;
    }

    public static boolean notNull(Object ... objects) {
        return !isNull(objects);
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
