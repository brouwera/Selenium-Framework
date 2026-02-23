package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE + " on classpath.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE, e);
        }
    }

    private static String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    // ============================================================
    // Environment Handling
    // ============================================================

    public static String getEnvironment() {
        return get("env").toLowerCase();
    }

    public static String getBaseUrl() {
        String env = getEnvironment();
        return get(env + ".base.url");
    }

    // ============================================================
    // Browser Settings
    // ============================================================

    public static String getBrowser() {
        return get("browser").toLowerCase();
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    // ============================================================
    // Timeout Settings
    // ============================================================

    public static int getExplicitWait() {
        return Integer.parseInt(get("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout"));
    }

    // ============================================================
    // Driver Paths
    // ============================================================

    public static String getEdgeDriverPath() {
        return get("edge.driver.path");
    }

    public static String getGeckoDriverPath() {
        return get("gecko.driver.path");
    }
}