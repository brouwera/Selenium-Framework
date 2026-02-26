package config;

import exceptions.FrameworkInitializationException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    private static Dotenv dotenv;

    static {
        loadDotEnv();
        loadProperties();
    }

    // ============================================================
    // Load .env file
    // ============================================================

    private static void loadDotEnv() {
        try {
            dotenv = Dotenv.configure()
                    .ignoreIfMissing()     // Allows repo to run even if .env isn't present
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
                throw new FrameworkInitializationException("Unable to find " + CONFIG_FILE + " on classpath.");
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
        if (System.getProperty(key) != null) {
            return System.getProperty(key);
        }

        // 2. .env override
        if (dotenv != null && dotenv.get(key) != null) {
            return dotenv.get(key);
        }

        // 3. config.properties fallback
        return properties.getProperty(key);
    }

    // ============================================================
    // Environment Handling
    // ============================================================

    public static String getEnvironment() {
        String env = get("env");
        if (env == null) {
            throw new FrameworkInitializationException("Environment (env) is not set in .env or config.properties.");
        }
        return env.toLowerCase();
    }

    public static String getBaseUrl() {
        String env = getEnvironment();
        String url = get(env + ".base.url");

        if (url == null) {
            throw new FrameworkInitializationException("Base URL for environment '" + env + "' is missing.");
        }

        return url;
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
        return Integer.parseInt(get("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout"));
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