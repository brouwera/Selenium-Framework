package base;

import config.ConfigManager;
import factory.WebDriverFactory;
import listeners.TestListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

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

        // ============================================================
        // EARLY MDC SET (critical for BasePage logging)
        // ============================================================
        String testName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        TestListener.setTestNameEarly(testName);
        MDC.put("testName", testName);

        log.info("===== STARTING TEST: {} =====", testName);

        // ============================================================
        // Configuration Logging
        // ============================================================
        log.info("Environment: {}", ConfigManager.getEnvironment());
        log.info("Browser: {}", ConfigManager.getBrowser());
        log.info("Headless: {}", ConfigManager.isHeadless());
        log.info("Remote: {}", ConfigManager.isRemote());
        log.info("Explicit Wait: {} seconds", ConfigManager.getExplicitWait());
        log.info("Page Load Timeout: {} seconds", ConfigManager.getPageLoadTimeout());
        log.info("Script Timeout: {} seconds", ConfigManager.getScriptTimeout());

        // ============================================================
        // WebDriver Creation
        // ============================================================
        WebDriver webDriver = WebDriverFactory.createDriver();
        driver.set(webDriver);

        // ============================================================
        // Apply Timeouts
        // ============================================================
        webDriver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigManager.getPageLoadTimeout())
        );

        webDriver.manage().timeouts().scriptTimeout(
                Duration.ofSeconds(ConfigManager.getScriptTimeout())
        );

        // ============================================================
        // Explicit Wait
        // ============================================================
        wait.set(new WebDriverWait(webDriver, Duration.ofSeconds(ConfigManager.getExplicitWait())));

        log.info("Driver setup complete for test: {}", testName);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method, ITestResult result) {

        String testName = MDC.get("testName");

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("===== TEST FAILED: {} =====", testName);
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

        // DO NOT clear MDC here — TestListener handles it
    }
}