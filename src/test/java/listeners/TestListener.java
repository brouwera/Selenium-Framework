package listeners;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LogEntry;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        Object testClass = result.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();

        if (driver != null) {
            saveScreenshot(driver);
            savePageSource(driver);
            saveBrowserConsoleLogs(driver);
        }
    }

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    private byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page Source", type = "text/html")
    private byte[] savePageSource(WebDriver driver) {
        return driver.getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "Browser Console Logs", type = "text/plain")
    private String saveBrowserConsoleLogs(WebDriver driver) {
        StringBuilder logs = new StringBuilder();
        List<LogEntry> logEntries = driver.manage().logs().get(LogType.BROWSER).getAll();

        for (LogEntry entry : logEntries) {
            logs.append(entry.getLevel())
                    .append(" : ")
                    .append(entry.getMessage())
                    .append("\n");
        }

        return logs.toString();
    }

    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("Starting test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("Test passed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step("Test skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        Allure.step("Starting test suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        Allure.step("Finished test suite: " + context.getName());
    }
}