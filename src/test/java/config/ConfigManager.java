package config;

import exceptions.FrameworkInitializationException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    // ============================================================
    // Fields
    // ============================================================
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    private static Dotenv dotenv;

    private ConfigManager() {}

    // ============================================================
    // Static Initialization (Load .env + config.properties)
    // ============================================================
    static {
        loadDotEnv();
        loadProperties();
    }

    // ============================================================
    // Load .env File (Optional)
    // ============================================================
    private static void loadDotEnv() {
        try {
            dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
        } catch (Exception e) {
            throw new FrameworkInitializationException("Failed to load .env file.", e);
        }
    }

    // ============================================================
    // Load config.properties
    // ============================================================
    private static void loadProperties() {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new FrameworkInitializationException(
                        "Unable to find " + CONFIG_FILE + " on classpath."
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new FrameworkInitializationException("Failed to load " + CONFIG_FILE, e);
        }
    }

    // ============================================================
    // Unified Getter (Order: System → .env → config.properties)
    // ============================================================
    private static String get(String key) {

        // 1. System property override
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }

        // 2. .env override
        if (dotenv != null) {
            String envValue = dotenv.get(key);
            if (envValue != null) {
                return envValue;
            }
        }

        // 3. config.properties fallback
        return properties.getProperty(key);
    }

    private static String getRequired(String key) {
        String value = get(key);
        if (value == null) {
            throw new FrameworkInitializationException(
                    "Required configuration key '" + key + "' is missing."
            );
        }
        return value;
    }

    private static int getRequiredInt(String key) {
        String value = getRequired(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new FrameworkInitializationException(
                    "Configuration key '" + key + "' must be an integer. Found: " + value, e
            );
        }
    }

    // ============================================================
    // Environment Handling
    // ============================================================
    public static String getEnvironment() {
        return getRequired("env").toLowerCase();
    }

    // ============================================================
    // Base URLs (Option C)
    // ============================================================

    /**
     * Base URL for PracticeTestAutomation.com
     * Uses env-based keys: local.base.url, stage.base.url, prod.base.url
     */
    public static String getPracticeBaseUrl() {
        String env = getEnvironment(); // local, stage, prod
        String key = env + ".base.url";
        return getRequired(key);
    }

    /**
     * Base URL for The-Internet Herokuapp
     */
    public static String getHerokuBaseUrl() {
        return getRequired("heroku.base.url");
    }

    // ============================================================
    // Browser Settings
    // ============================================================
    public static String getBrowser() {
        String browser = get("browser");
        return browser != null ? browser.toLowerCase() : "chrome";
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    // ============================================================
    // Timeout Settings
    // ============================================================
    public static int getExplicitWait() {
        return getRequiredInt("explicit.wait");
    }

    public static int getPageLoadTimeout() {
        return getRequiredInt("page.load.timeout");
    }

    public static int getShortWait() {
        return getRequiredInt("short.wait");
    }

    // ============================================================
    // Credentials (Optional)
    // ============================================================
    public static String getUsername() {
        return get("username");
    }

    public static String getPassword() {
        return get("password");
    }

    // ============================================================
    // Driver Paths (Optional)
    // ============================================================
    public static String getEdgeDriverPath() {
        return get("edge.driver.path");
    }

    public static String getGeckoDriverPath() {
        return get("gecko.driver.path");
    }
}