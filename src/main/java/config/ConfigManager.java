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
    // Helpers
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

    private static String getJsonString(String jsonKey) {
        JsonNode node = envNode.get(jsonKey);
        if (node == null || node.isNull()) {
            throw new FrameworkInitializationException(
                    "Missing required JSON key: " + jsonKey
            );
        }
        return node.asText();
    }

    private static int getJsonInt(JsonNode parent, String key, int defaultValue) {
        if (parent == null) return defaultValue;

        JsonNode node = parent.get(key);
        if (node == null || !node.isInt()) {
            return defaultValue;
        }

        return node.asInt();
    }

    // ============================================================
    // Environment
    // ============================================================
    public static String getEnvironment() {
        String override = getOverride("env");
        if (override != null) return override.toLowerCase();
        return "local";
    }

    // ============================================================
    // Base URLs
    // ============================================================
    public static String getPracticeBaseUrl() {
        String override = getOverride("practiceBaseUrl");
        return override != null ? override : getJsonString("practiceBaseUrl");
    }

    public static String getHerokuBaseUrl() {
        String override = getOverride("herokuBaseUrl");
        return override != null ? override : getJsonString("herokuBaseUrl");
    }

    // ============================================================
    // Browser Settings
    // ============================================================
    public static String getBrowser() {
        String override = getOverride("browser");
        return override != null ? override.toLowerCase() : getJsonString("browser").toLowerCase();
    }

    public static boolean isHeadless() {
        String override = getOverride("headless");
        if (override != null) return Boolean.parseBoolean(override);
        return envNode.get("headless").asBoolean();
    }

    public static boolean isRemote() {
        String override = getOverride("remote");
        if (override != null) return Boolean.parseBoolean(override);

        JsonNode node = envNode.get("remote");
        return node != null && node.asBoolean(false);
    }

    public static String getRemoteUrl() {
        String override = getOverride("remoteUrl");
        if (override != null) return override;

        JsonNode node = envNode.get("remoteUrl");
        if (node != null && !node.isNull()) {
            return node.asText();
        }

        return "";
    }

    // ============================================================
    // Timeout Settings (SAFE DEFAULTS)
    // ============================================================
    public static int getExplicitWait() {
        String override = getOverride("explicit.wait");
        if (override != null) return Integer.parseInt(override);
        return getJsonInt(envNode.get("timeouts"), "explicit", 10);
    }

    public static int getPageLoadTimeout() {
        String override = getOverride("page.load.timeout");
        if (override != null) return Integer.parseInt(override);
        return getJsonInt(envNode.get("timeouts"), "pageLoad", 30);
    }

    public static int getScriptTimeout() {
        String override = getOverride("script.timeout");
        if (override != null) return Integer.parseInt(override);
        return getJsonInt(envNode.get("timeouts"), "script", 30);
    }

    public static int getShortWait() {
        String override = getOverride("short.wait");
        if (override != null) return Integer.parseInt(override);
        return getJsonInt(envNode.get("timeouts"), "short", 3);
    }

    // ⭐ NEW — Required by WebDriverFactory
    public static int getImplicitWait() {
        String override = getOverride("implicit.wait");
        if (override != null) return Integer.parseInt(override);
        return getJsonInt(envNode.get("timeouts"), "implicit", 0);
    }

    // ============================================================
    // Artifact Root Directory (SAFE DEFAULT)
    // ============================================================
    public static String getArtifactRoot() {
        String override = getOverride("artifact.root");
        if (override != null) return override;

        JsonNode node = envNode.get("artifactRoot");
        if (node != null && !node.isNull()) {
            return node.asText();
        }

        return "target/artifacts";
    }
}