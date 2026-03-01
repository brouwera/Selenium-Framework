package listeners;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);
    private static final String LOG_DIR = "logs";

    // ============================================================
    // Suite Lifecycle
    // ============================================================

    @Override
    public void onStart(ITestContext context) {
        MDC.put("testName", "SUITE");
        logger.info("=== TEST SUITE STARTED: {} ===", context.getName());
        MDC.remove("testName");

        createLogDirectory();
    }

    @Override
    public void onFinish(ITestContext context) {
        MDC.put("testName", "SUITE");
        logger.info("=== TEST SUITE FINISHED: {} ===", context.getName());
        MDC.remove("testName");
    }

    // ============================================================
    // Test Lifecycle
    // ============================================================

    @Override
    public void onTestStart(ITestResult result) {
        String fullName = getFullTestName(result);
        MDC.put("testName", fullName);

        logger.info("=== STARTING TEST: {} ===", fullName);
        Allure.step("Starting test: " + fullName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String fullName = getFullTestName(result);
        MDC.put("testName", fullName);

        logger.info("=== TEST PASSED: {} ===", fullName);
        Allure.step("Test passed: " + fullName);

        WebDriver driver = getDriver(result);
        if (driver != null) {
            saveSuccessScreenshot(driver);
        }

        attachPerTestLog(result);

        MDC.remove("testName");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String fullName = getFullTestName(result);
        MDC.put("testName", fullName);

        logger.error("=== TEST FAILED: {} ===", fullName);

        WebDriver driver = getDriver(result);
        if (driver != null) {
            saveFailureScreenshot(driver);
            savePageSource(driver);
            saveBrowserLogs(driver);
        }

        saveFailureMessage(result.getThrowable());
        attachPerTestLog(result);

        Allure.step("Failure captured for test: " + fullName);

        MDC.remove("testName");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String fullName = getFullTestName(result);
        MDC.put("testName", fullName);

        logger.warn("=== TEST SKIPPED: {} ===", fullName);
        Allure.step("Test skipped: " + fullName);

        attachPerTestLog(result);

        MDC.remove("testName");
    }

    // ============================================================
    // WebDriver Retrieval
    // ============================================================

    private WebDriver getDriver(ITestResult result) {
        Object instance = result.getInstance();
        return (instance instanceof BaseTest) ? ((BaseTest) instance).getDriver() : null;
    }

    // ============================================================
    // Per-Test Log Handling
    // ============================================================

    private String getFullTestName(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        return className + "." + methodName;
    }

    private Path getPerTestLogPath(ITestResult result) {
        return Paths.get(LOG_DIR, getFullTestName(result) + ".log");
    }

    @Attachment(value = "Test Log", type = "text/plain")
    private byte[] attachLogFile(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return ("Unable to attach log file: " + e.getMessage()).getBytes();
        }
    }

    private void attachPerTestLog(ITestResult result) {
        Path logPath = getPerTestLogPath(result);
        if (Files.exists(logPath)) {
            attachLogFile(logPath);
        }
    }

    private void createLogDirectory() {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
        } catch (IOException ignored) {}
    }

    // ============================================================
    // Allure Attachments
    // ============================================================

    @Attachment(value = "Screenshot on Success", type = "image/png")
    public byte[] saveSuccessScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] saveFailureScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page Source", type = "text/html")
    public byte[] savePageSource(WebDriver driver) {
        return driver.getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "Browser Console Logs", type = "text/plain")
    public String saveBrowserLogs(WebDriver driver) {
        try {
            List<LogEntry> logs = driver.manage().logs().get(LogType.BROWSER).getAll();
            return logs.stream()
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return "No browser logs available or browser does not support log retrieval.";
        }
    }

    @Attachment(value = "Failure Details", type = "text/plain")
    public String saveFailureMessage(Throwable throwable) {
        return throwable != null ? throwable.toString() : "No stack trace available";
    }
}