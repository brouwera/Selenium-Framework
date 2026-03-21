package config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.FrameworkInitializationException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.InputStream;

public final class ConfigManager {

    private static final String CONFIG_FILE = "config.json";
    private static JsonNode rootNode;
    private static JsonNode envNode;
    private static Dotenv dotenv;

    private ConfigManager() {}

    // ============================================================
    // Static Initialization
    // ============================================================
    static {
        loadDotEnv();
        loadJsonConfig();
        selectEnvironmentBlock();
    }

    // ============================================================
    // Loaders
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

    private static void loadJsonConfig() {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new FrameworkInitializationException(
                        "Unable to find " + CONFIG_FILE + " on classpath."
                );
            }

            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(input);

        } catch (Exception e) {
            throw new FrameworkInitializationException("Failed to load " + CONFIG_FILE, e);
        }
    }

    private static void selectEnvironmentBlock() {
        String env = getEnvironment();
        envNode = rootNode.get(env);

        if (envNode == null) {
            throw new FrameworkInitializationException(
                    "Environment block '" + env + "' not found in " + CONFIG_FILE
            );
        }
    }

    // ============================================================
    // Unified Override Helper
    // ============================================================
    private static String getOverride(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) return systemValue;

        if (dotenv != null) {
            String envValue = dotenv.get(key);
            if (envValue != null) return envValue;
        }

        return null;
    }

    private static String getConfigValue(String overrideKey, String jsonKey) {
        String override = getOverride(overrideKey);
        if (override != null) return override;

        JsonNode node = envNode.get(jsonKey);
        if (node == null || node.isNull()) {
            throw new FrameworkInitializationException(
                    "Missing required JSON key: " + jsonKey
            );
        }
        return node.asText();
    }

    private static int getConfigInt(String overrideKey, String jsonKey, int defaultValue) {
        String override = getOverride(overrideKey);
        if (override != null) return Integer.parseInt(override);

        JsonNode timeouts = envNode.get("timeouts");
        if (timeouts == null) return defaultValue;

        JsonNode node = timeouts.get(jsonKey);
        if (node == null || !node.isInt()) return defaultValue;

        return node.asInt();
    }

    // ============================================================
    // Environment
    // ============================================================
    public static String getEnvironment() {
        String override = getOverride("env");
        if (override != null) return override.toLowerCase();
        return "qa";   // Default environment
    }

    // ============================================================
    // Base URLs
    // ============================================================
    public static String getPracticeBaseUrl() {
        return getConfigValue("practice.base.url", "practiceBaseUrl");
    }

    public static String getHerokuBaseUrl() {
        return getConfigValue("heroku.base.url", "herokuBaseUrl");
    }

    public static String getApiBaseUrl() {
        return getConfigValue("api.base.url", "apiBaseUrl");
    }

    // ============================================================
    // Browser Settings
    // ============================================================
    public static String getBrowser() {
        return getConfigValue("browser", "browser").toLowerCase();
    }

    public static boolean isHeadless() {
        String override = getOverride("headless");
        if (override != null) return Boolean.parseBoolean(override);

        JsonNode node = envNode.get("headless");
        return node != null && node.asBoolean(false);
    }

    public static boolean isRemote() {
        String override = getOverride("remote");
        if (override != null) return Boolean.parseBoolean(override);

        JsonNode node = envNode.get("remote");
        return node != null && node.asBoolean(false);
    }

    public static String getRemoteUrl() {
        return getConfigValue("remote.url", "remoteUrl");
    }

    // ============================================================
    // Timeout Settings (SAFE DEFAULTS)
    // ============================================================
    public static int getExplicitWait() {
        return getConfigInt("timeouts.explicit", "explicit", 10);
    }

    public static int getPageLoadTimeout() {
        return getConfigInt("timeouts.pageLoad", "pageLoad", 30);
    }

    public static int getScriptTimeout() {
        return getConfigInt("timeouts.script", "script", 30);
    }

    public static int getShortWait() {
        return getConfigInt("timeouts.short", "short", 3);
    }

    public static int getImplicitWait() {
        return getConfigInt("timeouts.implicit", "implicit", 0);
    }

    // ============================================================
    // Artifact Root Directory
    // ============================================================
    public static String getArtifactRoot() {
        return getConfigValue("artifact.root", "artifactRoot");
    }
}