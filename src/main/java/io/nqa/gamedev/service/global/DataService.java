package io.nqa.gamedev.service.global;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DataService {
    private static volatile DataService dataService;
    private static final String DATA_BASE_DIR = "data";

    private DataService() {}

    private static void init() {
        if (dataService == null) dataService = new DataService();
    }

    private static void prepare() {
        init();
        File baseDir = new File(DATA_BASE_DIR);
        if (!baseDir.exists() || !baseDir.isDirectory()) baseDir.mkdir();
        if (!baseDir.canRead() || !baseDir.canWrite()) throw new RuntimeException(DATA_BASE_DIR + " lacks permissions!");
    }

    public static void saveCue(File cue) {
        prepare();
    }
}
