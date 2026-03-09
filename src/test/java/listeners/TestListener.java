package listeners;

import pages.BasePage;
import base.BaseTest;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ArtifactManager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    // ============================================================
    // EARLY MDC SUPPORT
    // ============================================================
    public static void setTestNameEarly(String name) {
        MDC.put("testName", name);
    }

    // ============================================================
    // SUITE LIFECYCLE
    // ============================================================

    @Override
    public void onStart(ITestContext context) {
        MDC.put("testName", "SUITE");
        log.info("=== TEST SUITE STARTED: {} ===", context.getName());

        ArtifactManager.initRun();

        // Generate environment.properties
        try {
            Path resultsDir = Path.of("target", "allure-results");
            Files.createDirectories(resultsDir);

            Path envFile = resultsDir.resolve("environment.properties");

            Properties props = new Properties();
            props.setProperty("Suite", context.getName());
            props.setProperty("Timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            props.setProperty("OS", System.getProperty("os.name"));
            props.setProperty("OS Version", System.getProperty("os.version"));
            props.setProperty("Java Version", System.getProperty("java.version"));
            props.setProperty("Browser", System.getProperty("browser", "chrome"));
            props.setProperty("Headless", System.getProperty("headless", "false"));
            props.setProperty("Remote", System.getProperty("remote", "false"));
            props.setProperty("Environment", System.getProperty("env", "local"));

            try (var out = Files.newOutputStream(envFile)) {
                props.store(out, "Allure Environment Properties");
            }

            log.info("Generated Allure environment.properties at {}", envFile.toAbsolutePath());

        } catch (Exception e) {
            log.warn("Unable to generate environment.properties: {}", e.getMessage());
        }

        Allure.addAttachment("Environment Info", "text/plain",
                "Suite: " + context.getName() + "\n" +
                        "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Override
    public void onFinish(ITestContext context) {
        MDC.put("testName", "SUITE");
        log.info("=== TEST SUITE FINISHED: {} ===", context.getName());

        ArtifactManager.writeRunSummary();
        ArtifactManager.zipRunDirectory();
        ArtifactManager.cleanupOldRuns();
    }

    // ============================================================
    // TEST LIFECYCLE
    // ============================================================

    @Override
    public void onTestStart(ITestResult result) {
        String fullName = getFullTestName(result);

        // Store final test name to prevent invocation mismatch
        result.setAttribute("finalTestName", fullName);

        // Reset step counter (Day 29 fix)
        BasePage.resetStepCounter();

        // MDC setup
        MDC.put("testName", fullName);
        MDC.put("step", "0");

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        MDC.put("testStartTimestamp", timestamp);

        // Create per-test directory
        ArtifactManager.createTestDirectory(fullName);

        long start = System.currentTimeMillis();
        result.setAttribute("testStart", start);

        log.info("=== STARTING TEST: {} at {} ===", fullName, timestamp);
        Allure.step("Starting test: " + fullName + " at " + timestamp);

        ArtifactManager.appendToTestLog("Test started: " + fullName + " at " + timestamp);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String fullName = (String) result.getAttribute("finalTestName");
        MDC.put("testName", fullName);

        long start = getStartTime(result);
        long end = System.currentTimeMillis();
        long duration = end - start;

        log.info("=== TEST PASSED: {} ===", fullName);
        Allure.step("Test passed: " + fullName);

        WebDriver driver = getDriver(result);
        if (driver != null) {
            attachScreenshot(driver, "success");
            attachPageSource(driver);
            attachBrowserLogs(driver);
        }

        ArtifactManager.copyGlobalLog();
        ArtifactManager.appendToTestLog("Test passed: " + fullName);
        attachPerTestLog();

        Map<String, Object> metadata = buildMetadata(result, fullName, start, end, duration);
        ArtifactManager.writeMetadata(fullName, metadata);

        ArtifactManager.recordTestResult(fullName, "PASSED", duration, null, start, end);

        MDC.clear();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String fullName = (String) result.getAttribute("finalTestName");
        MDC.put("testName", fullName);

        long start = getStartTime(result);
        long end = System.currentTimeMillis();
        long duration = end - start;

        log.error("=== TEST FAILED: {} ===", fullName);

        WebDriver driver = getDriver(result);
        if (driver != null) {
            attachScreenshot(driver, "failure");
            attachPageSource(driver);
            attachBrowserLogs(driver);
        }

        Throwable throwable = result.getThrowable();
        attachFailureMessage(throwable);

        ArtifactManager.copyGlobalLog();
        ArtifactManager.appendToTestLog("Test failed: " + fullName);
        attachPerTestLog();

        String failureMessage = throwable != null ? throwable.toString() : null;

        Map<String, Object> metadata = buildMetadata(result, fullName, start, end, duration);
        ArtifactManager.writeMetadata(fullName, metadata);

        ArtifactManager.recordTestResult(fullName, "FAILED", duration, failureMessage, start, end);

        Allure.step("Failure captured for test: " + fullName);
        MDC.clear();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String fullName = (String) result.getAttribute("finalTestName");
        MDC.put("testName", fullName);

        long start = getStartTime(result);
        long end = System.currentTimeMillis();
        long duration = end - start;

        log.warn("=== TEST SKIPPED: {} ===", fullName);
        Allure.step("Test skipped: " + fullName);

        ArtifactManager.copyGlobalLog();
        ArtifactManager.appendToTestLog("Test skipped: " + fullName);
        attachPerTestLog();

        Map<String, Object> metadata = buildMetadata(result, fullName, start, end, duration);
        ArtifactManager.writeMetadata(fullName, metadata);

        ArtifactManager.recordTestResult(fullName, "SKIPPED", duration, null, start, end);

        MDC.clear();
    }

    // ============================================================
    // DRIVER RETRIEVAL
    // ============================================================

    private WebDriver getDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) {
            return ((BaseTest) instance).getDriver();
        }
        log.warn("Test instance is not a BaseTest: {}", instance);
        return null;
    }

    // ============================================================
    // ATTACHMENTS
    // ============================================================

    private void attachScreenshot(WebDriver driver, String type) {
        try {
            byte[] data = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path file = ArtifactManager.writeScreenshot(data, type);

            Allure.addAttachment("Screenshot (" + type + ")", "image/png",
                    Files.newInputStream(file), "png");

        } catch (Exception e) {
            log.warn("Unable to capture {} screenshot: {}", type, e.getMessage());
        }
    }

    private void attachPageSource(WebDriver driver) {
        try {
            byte[] data = driver.getPageSource().getBytes(StandardCharsets.UTF_8);
            Path file = ArtifactManager.writePageSource(data);

            Allure.addAttachment("Page Source", "text/html",
                    Files.newInputStream(file), "html");

        } catch (Exception e) {
            log.warn("Unable to capture page source: {}", e.getMessage());
        }
    }

    private void attachBrowserLogs(WebDriver driver) {
        try {
            List<LogEntry> logs = driver.manage().logs().get(LogType.BROWSER).getAll();
            String logText = logs.stream().map(LogEntry::toString).collect(Collectors.joining("\n"));

            Path file = ArtifactManager.writeBrowserLogs(logText);

            Allure.addAttachment("Browser Console Logs", "text/plain",
                    Files.newInputStream(file), "txt");

        } catch (Exception e) {
            log.warn("Unable to capture browser logs: {}", e.getMessage());
        }
    }

    private void attachFailureMessage(Throwable throwable) {
        try {
            String message = throwable != null ? throwable.toString() : "No stack trace available";
            Path file = ArtifactManager.writeFailureMessage(message);

            Allure.addAttachment("Failure Details", "text/plain",
                    Files.newInputStream(file), "txt");

        } catch (Exception e) {
            log.warn("Unable to attach failure message: {}", e.getMessage());
        }
    }

    private void attachPerTestLog() {
        try {
            Path logFile = ArtifactManager.getCurrentTestDir()
                    .resolve("logs")
                    .resolve("test.log");

            Allure.addAttachment("Per-Test Log", "text/plain",
                    Files.newInputStream(logFile), "txt");

        } catch (Exception e) {
            log.warn("Unable to attach per-test log: {}", e.getMessage());
        }
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private String getFullTestName(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();

        Object[] params = result.getParameters();

        if (params != null && params.length > 0) {
            String paramString = Arrays.stream(params)
                    .map(Object::toString)
                    .map(s -> s.replaceAll("[^a-zA-Z0-9_-]", ""))
                    .collect(Collectors.joining(""));
            return className + "." + methodName + "_" + paramString;
        }

        int index = result.getMethod().getCurrentInvocationCount();
        return className + "." + methodName + "_" + index;
    }

    // ============================================================
    // Public static getTestName() — required by BaseTest
    // ============================================================
    public static String getTestName(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();

        Object[] params = result.getParameters();

        if (params != null && params.length > 0) {
            String paramString = Arrays.stream(params)
                    .map(Object::toString)
                    .map(s -> s.replaceAll("[^a-zA-Z0-9_-]", ""))
                    .collect(Collectors.joining(""));
            return className + "." + methodName + "_" + paramString;
        }

        int index = result.getMethod().getCurrentInvocationCount();
        return className + "." + methodName + "_" + index;
    }

    private long getStartTime(ITestResult result) {
        Object attr = result.getAttribute("testStart");
        if (attr instanceof Long) {
            return (Long) attr;
        }
        return result.getStartMillis();
    }

    private Map<String, Object> buildMetadata(ITestResult result,
                                              String fullName,
                                              long start,
                                              long end,
                                              long duration) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("fullName", fullName);
        metadata.put("className", result.getTestClass().getName());
        metadata.put("methodName", result.getMethod().getMethodName());
        metadata.put("parameters", Arrays.toString(result.getParameters()));
        metadata.put("suiteName", result.getTestContext().getSuite().getName());
        metadata.put("threadName", Thread.currentThread().getName());
        metadata.put("testStart", start);
        metadata.put("testEnd", end);
        metadata.put("testDurationMs", duration);
        metadata.put("testStartTimestamp", MDC.get("testStartTimestamp"));
        return metadata;
    }
}