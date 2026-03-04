package config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.FrameworkInitializationException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.InputStream;

public final class ConfigManager {

    // ============================================================
    // Fields
    // ============================================================
    private static final String CONFIG_FILE = "config.json";
    private static JsonNode rootNode;          // Entire JSON tree
    private static JsonNode envNode;           // Selected environment block
    private static Dotenv dotenv;              // .env overrides

    private ConfigManager() {}

    // ============================================================
    // Static Initialization (Load .env + JSON config)
    // ============================================================
    static {
        loadDotEnv();
        loadJsonConfig();
        selectEnvironmentBlock();
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
    // Load config.json
    // ============================================================
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

    // ============================================================
    // Select Environment Block
    // ============================================================
    private static void selectEnvironmentBlock() {
        String env = getEnvironment(); // local, stage, prod

        envNode = rootNode.get(env);
        if (envNode == null) {
            throw new FrameworkInitializationException(
                    "Environment block '" + env + "' not found in " + CONFIG_FILE
            );
        }
    }

    // ============================================================
    // Unified Getter (Order: System → .env → JSON)
    // ============================================================
    private static String getOverride(String key) {

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

        return null; // No override found
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

    private static int getJsonInt(JsonNode parent, String key) {
        JsonNode node = parent.get(key);
        if (node == null || !node.isInt()) {
            throw new FrameworkInitializationException(
                    "Missing or invalid integer key: " + key
            );
        }
        return node.asInt();
    }

    // ============================================================
    // Environment Handling
    // ============================================================
    public static String getEnvironment() {
        String override = getOverride("env");
        if (override != null) return override.toLowerCase();

        // Default to "local" if not provided
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

    // ============================================================
    // Timeout Settings
    // ============================================================
    public static int getExplicitWait() {
        String override = getOverride("explicit.wait");
        if (override != null) return Integer.parseInt(override);

        return getJsonInt(envNode.get("timeouts"), "explicit");
    }

    public static int getPageLoadTimeout() {
        String override = getOverride("page.load.timeout");
        if (override != null) return Integer.parseInt(override);

        return getJsonInt(envNode.get("timeouts"), "pageLoad");
    }

    public static int getShortWait() {
        String override = getOverride("short.wait");
        if (override != null) return Integer.parseInt(override);

        return getJsonInt(envNode.get("timeouts"), "short");
    }

    // ============================================================
    // Driver Paths
    // ============================================================
    public static String getEdgeDriverPath() {
        String override = getOverride("edge.driver.path");
        return override != null ? override : getJsonStringFromGroup("drivers", "edge");
    }

    public static String getGeckoDriverPath() {
        String override = getOverride("gecko.driver.path");
        return override != null ? override : getJsonStringFromGroup("drivers", "gecko");
    }

    private static String getJsonStringFromGroup(String group, String key) {
        JsonNode groupNode = envNode.get(group);
        if (groupNode == null) {
            throw new FrameworkInitializationException("Missing group: " + group);
        }

        JsonNode valueNode = groupNode.get(key);
        if (valueNode == null) {
            throw new FrameworkInitializationException("Missing key in group '" + group + "': " + key);
        }

        return valueNode.asText();
    }
}