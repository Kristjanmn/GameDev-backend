package io.nqa.gamedev.service;

import org.springframework.stereotype.Service;

@Service
public class GUIDGenerator {
    private static volatile GUIDGenerator guidGenerator;
    private static final int DEFAULT_LENGTH = 16;

    private GUIDGenerator() {}

    private static void init() {
        if (guidGenerator == null) guidGenerator = new GUIDGenerator();
    }

    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    public static String generate(int length) {
        init();
        String guidCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // Get one random character from guidCharacters
            int character = (int) Math.floor(Math.random() * guidCharacters.length());
            sb.append(guidCharacters, character, character+1);
        }
        return sb.toString();
    }
}
