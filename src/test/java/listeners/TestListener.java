package listeners;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class TestListener implements ITestListener {

    // ============================================================
    // Suite Lifecycle
    // ============================================================

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=== TEST SUITE STARTED: " + context.getName() + " ===");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("=== TEST SUITE FINISHED: " + context.getName() + " ===");
    }

    // ============================================================
    // Test Lifecycle
    // ============================================================

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("=== STARTING TEST: " + name + " ===");
        Allure.step("Starting test: " + name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("=== TEST PASSED: " + name + " ===");
        Allure.step("Test passed: " + name);

        WebDriver driver = BaseTest.getDriver();
        if (driver != null) {
            saveSuccessScreenshot(driver);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("=== TEST FAILED: " + name + " ===");

        WebDriver driver = BaseTest.getDriver();

        if (driver != null) {
            saveFailureScreenshot(driver);
            savePageSource(driver);
            saveBrowserLogs(driver);
        }

        saveFailureMessage(result.getThrowable());
        Allure.step("Failure captured for test: " + name);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("=== TEST SKIPPED: " + name + " ===");
        Allure.step("Test skipped: " + name);
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