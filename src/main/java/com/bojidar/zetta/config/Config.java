package com.bojidar.zetta.config;

public final class Config {

    private Config() {
    }

    public static String sauceDemoUrl() {

        return get("SAUCE_DEMO_URL", "https://www.saucedemo.com/");
    }

    public static String jsonPlaceholderUrl() {

        return get("JSON_PLACEHOLDER_URL", "https://jsonplaceholder.typicode.com");
    }

    public static String internetPageUrl() {

        return get("INTERNET_PAGE_URL", "https://the-internet.herokuapp.com/");
    }

    public static boolean isHeadless() {

        return Boolean.parseBoolean(get("HEADLESS", "true"));
    }

    public static int defaultTimeoutSeconds() {
        return Integer.parseInt(get("DEFAULT_TIMEOUT_SECONDS", "10"));
    }

    private static String get(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return defaultValue;
    }
}