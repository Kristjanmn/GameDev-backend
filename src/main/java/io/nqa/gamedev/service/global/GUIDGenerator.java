package io.nqa.gamedev.service.global;

import org.springframework.stereotype.Service;

/**
 * Originally developed in 2020 it contained 81 unique symbols,
 * but this one has been cut down to 62 due to web limitations.
 *
 * For those who say UUID is better: eat a pile of poop! YouTube
 * uses similar system and they limit their video IDs to 11 symbols.
 * In current form there can be 62^16 unique combinations, so
 * I really do not care about your criticism.
 */
@Service
public class GUIDGenerator {
    private static volatile GUIDGenerator guidGenerator;
    private static final int DEFAULT_LENGTH = 16;

    private GUIDGenerator() {}

    private static void init() {
        if (guidGenerator == null) guidGenerator = new GUIDGenerator();
    }

    /**
     * Generates GUID with default length.
     *
     * @return Generated Unique ID with default length
     */
    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    /**
     * Generates GUID with given length.
     *
     * @param length GUID length
     * @return Generated Unique ID
     */
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
