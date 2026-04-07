package factory;

import config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;

public class WebDriverFactory {

    private static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class);

    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    public static WebDriver createDriver() {
        String browser = ConfigManager.getBrowser().toLowerCase();
        boolean headless = ConfigManager.isHeadless();
        boolean remote = ConfigManager.isRemote();

        log.info("Creating WebDriver: browser={}, headless={}, remote={}", browser, headless, remote);

        if (remote) {
            return createRemoteDriver(browser, headless);
        }

        switch (browser) {
            case "chrome":
                return createChromeDriver(headless);

            case "firefox":
                return createFirefoxDriver(headless);

            case "edge":
                return createEdgeDriver(headless);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    // ============================================================
    // Chrome
    // ============================================================

    private static WebDriver createChromeDriver(boolean headless) {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = buildChromeOptions(headless);

        if (isCiEnvironment()) {
            log.info("CI detected — forcing Chrome binary path");
            options.setBinary("/usr/bin/google-chrome");
        }

        ChromeDriver driver = new ChromeDriver(options);

        applyTimeouts(driver);
        log.info("ChromeDriver initialized (sessionId={})", driver.getSessionId());
        return driver;
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // Headless logic
        if (headless || isCiEnvironment()) {
            options.addArguments("--headless=new");
        }

        // Modern, stable flags
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        // Window size
        options.addArguments("--window-size=" + WINDOW_WIDTH + "," + WINDOW_HEIGHT);

        return options;
    }

    // ============================================================
    // Firefox
    // ============================================================

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = buildFirefoxOptions(headless);
        FirefoxDriver driver = new FirefoxDriver(options);

        applyTimeouts(driver);
        log.info("FirefoxDriver initialized");
        return driver;
    }

    private static FirefoxOptions buildFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        if (headless || isCiEnvironment()) {
            options.addArguments("-headless");
        }

        options.addArguments("--width=" + WINDOW_WIDTH);
        options.addArguments("--height=" + WINDOW_HEIGHT);

        return options;
    }

    // ============================================================
    // Edge
    // ============================================================

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = buildEdgeOptions(headless);
        EdgeDriver driver = new EdgeDriver(options);

        applyTimeouts(driver);
        log.info("EdgeDriver initialized");
        return driver;
    }

    private static EdgeOptions buildEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        if (headless || isCiEnvironment()) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=" + WINDOW_WIDTH + "," + WINDOW_HEIGHT);

        return options;
    }

    // ============================================================
    // Remote WebDriver
    // ============================================================

    private static WebDriver createRemoteDriver(String browser, boolean headless) {
        try {
            URL gridUrl = new URL(ConfigManager.getRemoteUrl());
            log.info("Connecting to remote WebDriver at {}", gridUrl);

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(CapabilityType.BROWSER_NAME, browser);

            switch (browser) {
                case "chrome":
                    caps.merge(buildChromeOptions(headless));
                    break;

                case "firefox":
                    caps.merge(buildFirefoxOptions(headless));
                    break;

                case "edge":
                    caps.merge(buildEdgeOptions(headless));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported remote browser: " + browser);
            }

            RemoteWebDriver driver = new RemoteWebDriver(gridUrl, caps);
            applyTimeouts(driver);

            log.info("RemoteWebDriver initialized (sessionId={})", driver.getSessionId());
            return driver;

        } catch (Exception e) {
            log.error("Failed to initialize remote WebDriver: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize remote WebDriver", e);
        }
    }

    // ============================================================
    // Shared helpers
    // ============================================================

    private static void applyTimeouts(WebDriver driver) {
        Duration pageLoad = Duration.ofSeconds(ConfigManager.getPageLoadTimeout());
        Duration script = Duration.ofSeconds(ConfigManager.getScriptTimeout());

        log.info("Applying timeouts: pageLoad={}s, script={}s, implicit=0s",
                pageLoad.getSeconds(), script.getSeconds());

        driver.manage().timeouts().pageLoadTimeout(pageLoad);
        driver.manage().timeouts().scriptTimeout(script);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
    }

    // ============================================================
    // CI detection
    // ============================================================

    private static boolean isCiEnvironment() {
        return System.getenv("GITHUB_ACTIONS") != null;
    }
}