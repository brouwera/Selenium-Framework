package listeners;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.charset.StandardCharsets;

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
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("=== TEST FAILED: " + name + " ===");

        WebDriver driver = BaseTest.getDriver();
        if (driver != null) {
            saveScreenshot(driver);
            savePageSource(driver);
        }

        saveFailureMessage(result.getThrowable());
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

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page Source", type = "text/html")
    public byte[] savePageSource(WebDriver driver) {
        return driver.getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "Failure Details", type = "text/plain")
    public String saveFailureMessage(Throwable throwable) {
        return throwable != null ? throwable.toString() : "No stack trace available";
    }
}