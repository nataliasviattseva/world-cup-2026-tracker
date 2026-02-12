package com.worldcup.tracker.utils;

public class InputSanitizer {

    public static String clean(String input) {
        if (input == null) return null;

        return input
                .trim()
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }
}
