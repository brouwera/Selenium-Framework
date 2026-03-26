package listeners;

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
import pages.BasePage;
import utils.ArtifactManager;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);
    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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

        // Ensure root-level allure-results exists
        Path resultsDir = Path.of("allure-results");
        try {
            Files.createDirectories(resultsDir);
        } catch (Exception e) {
            log.warn("Unable to create allure-results directory: {}", e.getMessage());
        }

        // Copy Allure metadata from CLASSPATH (Maven-safe)
        copyMetadataFromClasspath("allure/categories.json");
        copyMetadataFromClasspath("allure/environment.properties");
        copyMetadataFromClasspath("allure/executor.json");

        Allure.addAttachment("Environment Info", "text/plain",
                "Suite: " + context.getName() + "\n" +
                        "Timestamp: " + TS.format(new Date()));
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
        String fullName = buildFullTestName(result);
        result.setAttribute("finalTestName", fullName);

        BasePage.resetStepCounter();

        MDC.put("testName", fullName);
        MDC.put("step", "0");

        String timestamp = TS.format(new Date());
        MDC.put("testStartTimestamp", timestamp);

        ArtifactManager.createTestDirectory(fullName);

        long start = System.currentTimeMillis();
        result.setAttribute("testStart", start);

        log.info("=== STARTING TEST: {} at {} ===", fullName, timestamp);
        Allure.step("Starting test: " + fullName + " at " + timestamp);

        ArtifactManager.appendToTestLog("Test started: " + fullName + " at " + timestamp);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        handleTestCompletion(result, "PASSED", null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        handleTestCompletion(result, "FAILED", result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        handleTestCompletion(result, "SKIPPED", null);
    }

    // ============================================================
    // COMPLETION HANDLER
    // ============================================================
    private void handleTestCompletion(ITestResult result, String status, Throwable throwable) {
        String fullName = (String) result.getAttribute("finalTestName");
        MDC.put("testName", fullName);

        long start = getStartTime(result);
        long end = System.currentTimeMillis();
        long duration = end - start;

        logStatus(status, fullName);

        WebDriver driver = getDriver(result);
        if (driver != null) {
            attachScreenshot(driver, status.toLowerCase());
            attachPageSource(driver);
            attachBrowserLogs(driver);
        }

        if (throwable != null) {
            attachFailureMessage(throwable);
        }

        ArtifactManager.copyGlobalLog();
        ArtifactManager.appendToTestLog("Test " + status + ": " + fullName);
        attachPerTestLog();

        Map<String, Object> metadata = buildMetadata(result, fullName, start, end, duration, status);
        ArtifactManager.writeMetadata(fullName, metadata);

        ArtifactManager.recordTestResult(
                fullName,
                status,
                duration,
                throwable != null ? throwable.toString() : null,
                start,
                end
        );

        MDC.clear();
    }

    private void logStatus(String status, String fullName) {
        switch (status) {
            case "PASSED" -> log.info("=== TEST PASSED: {} ===", fullName);
            case "FAILED" -> log.error("=== TEST FAILED: {} ===", fullName);
            case "SKIPPED" -> log.warn("=== TEST SKIPPED: {} ===", fullName);
        }
    }

    // ============================================================
    // DRIVER RETRIEVAL
    // ============================================================
    private WebDriver getDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest base) {
            try {
                return base.getDriver();
            } catch (Exception e) {
                log.warn("Unable to retrieve driver from BaseTest: {}", e.getMessage());
            }
        }
        return null;
    }

    // ============================================================
    // ATTACHMENTS
    // ============================================================
    private void attachScreenshot(WebDriver driver, String type) {
        try {
            byte[] data = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path file = ArtifactManager.writeScreenshot(data, type);

            Allure.addAttachment("Screenshot - " + type, "image/png",
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
    // METADATA COPY (FIXED)
    // ============================================================
    private void copyMetadataFromClasspath(String resourcePath) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {

            if (in == null) {
                log.warn("Metadata file not found on classpath: {}", resourcePath);
                return;
            }

            Path target = Path.of("allure-results", Path.of(resourcePath).getFileName().toString());
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);

            log.info("Copied Allure metadata: {}", resourcePath);

        } catch (Exception e) {
            log.warn("Failed to copy metadata {}: {}", resourcePath, e.getMessage());
        }
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private String buildFullTestName(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();

        Object[] params = result.getParameters();

        if (params != null && params.length > 0) {
            String paramString = Arrays.stream(params)
                    .map(Object::toString)
                    .map(this::sanitizeParam)
                    .collect(Collectors.joining("_"));
            return className + "." + methodName + "_" + paramString;
        }

        int index = result.getMethod().getCurrentInvocationCount();
        return className + "." + methodName + "_" + index;
    }

    private String sanitizeParam(String s) {
        return s.replaceAll("[^a-zA-Z0-9_-]", "").replace(" ", "-");
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
                                              long duration,
                                              String status) {

        String className = result.getTestClass().getRealClass().getSimpleName();
        String packageName = result.getTestClass().getRealClass().getPackageName();

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("fullName", fullName);
        metadata.put("className", className);
        metadata.put("packageName", packageName);
        metadata.put("categoryClass", className.replace("Test", ""));
        metadata.put("categoryPackage", packageName.substring(packageName.lastIndexOf('.') + 1));
        metadata.put("methodName", result.getMethod().getMethodName());
        metadata.put("parameters", Arrays.toString(result.getParameters()));
        metadata.put("suiteName", result.getTestContext().getSuite().getName());
        metadata.put("threadName", Thread.currentThread().getName());
        metadata.put("status", status);
        metadata.put("testStart", start);
        metadata.put("testEnd", end);
        metadata.put("testDurationMs", duration);
        metadata.put("testStartTimestamp", MDC.get("testStartTimestamp"));
        return metadata;
    }
}