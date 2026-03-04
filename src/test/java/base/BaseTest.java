package base;

import config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class BaseTest {

    // ============================================================
    // Logger
    // ============================================================
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    // ============================================================
    // ThreadLocal WebDriver + Wait (Parallel Execution Safe)
    // ============================================================
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    // ============================================================
    // Accessors (Used by BasePage)
    // ============================================================
    public WebDriver getDriver() {
        return driver.get();
    }

    public WebDriverWait getWait() {
        return wait.get();
    }

    // ============================================================
    // Test Lifecycle
    // ============================================================
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {

        String testName = this.getClass().getSimpleName() + "." + method.getName();
        MDC.put("testName", testName);

        log.info("===== STARTING TEST: {} =====", testName);

        String browser = ConfigManager.getBrowser();
        boolean headless = ConfigManager.isHeadless();
        int explicitWait = ConfigManager.getExplicitWait();

        log.info("Browser: {}", browser);
        log.info("Headless: {}", headless);
        log.info("Explicit Wait: {} seconds", explicitWait);

        WebDriver webDriver = createDriver(browser, headless);
        driver.set(webDriver);

        wait.set(new WebDriverWait(webDriver, Duration.ofSeconds(explicitWait)));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method, ITestResult result) {

        String testName = MDC.get("testName");

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("===== TEST FAILED: {} =====", testName);
            attachScreenshot(testName);
            attachLogFile(testName);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("===== TEST PASSED: {} =====", testName);
        } else {
            log.warn("===== TEST SKIPPED: {} =====", testName);
        }

        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                log.info("Closing browser for test: {}", testName);
                webDriver.quit();
            } catch (Exception e) {
                log.error("Error during driver.quit() for test {}", testName, e);
            }
        }

        driver.remove();
        wait.remove();

        MDC.remove("testName");
    }

    // ============================================================
    // Screenshot Capture
    // ============================================================
    private void attachScreenshot(String testName) {
        try {
            WebDriver webDriver = driver.get();
            if (webDriver == null) return;

            byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);

            Path screenshotPath = Path.of("logs", testName + "-screenshot.png");
            Files.write(screenshotPath, screenshot);

            log.info("Screenshot saved: {}", screenshotPath.toAbsolutePath());

        } catch (Exception e) {
            log.error("Failed to capture screenshot for {}", testName, e);
        }
    }

    // ============================================================
    // Log Attachment (for Allure tomorrow)
    // ============================================================
    private void attachLogFile(String testName) {
        try {
            File logFile = new File("logs/" + testName + ".log");
            if (logFile.exists()) {
                log.info("Log file available for attachment: {}", logFile.getAbsolutePath());
            } else {
                log.warn("No per-test log file found for {}", testName);
            }
        } catch (Exception e) {
            log.error("Failed to process log file for {}", testName, e);
        }
    }

    // ============================================================
    // Driver Factory
    // ============================================================
    private WebDriver createDriver(String browser, boolean headless) {

        log.info("Creating WebDriver instance for browser: {}", browser);

        switch (browser.toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                boolean isCI = System.getenv("CI") != null;

                if (isCI) {
                    log.info("Running in CI mode → enabling CI Chrome flags");
                    chromeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox",
                            "--disable-dev-shm-usage", "--window-size=1920,1080");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                    if (headless) chromeOptions.addArguments("--headless=new");
                }

                return new ChromeDriver(chromeOptions);

            case "edge":
                System.setProperty("webdriver.edge.driver", ConfigManager.getEdgeDriverPath());
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new");
                return new EdgeDriver(edgeOptions);

            case "firefox":
                System.setProperty("webdriver.gecko.driver", ConfigManager.getGeckoDriverPath());
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                return new FirefoxDriver(firefoxOptions);

            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }
    }
}