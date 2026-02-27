package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait shortWait; // 3-second wait for TimeoutException tests

    // ============================================================
    // Constructor (ThreadLocal-safe)
    // ============================================================

    public BasePage(BaseTest test) {
        this.driver = test.getDriver();
        this.wait = test.getWait();
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    // ============================================================
    // Allure Logging
    // ============================================================

    @Step("{message}")
    protected void log(String message) {
        // No-op: Allure handles the annotation
    }

    // ============================================================
    // Screenshot Helper
    // ============================================================

    protected byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // ============================================================
    // Wait Helpers
    // ============================================================

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ============================================================
    // Core Actions
    // ============================================================

    protected void click(By locator) {
        log("Clicking element: " + locator);
        waitForClickable(locator);
        driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        log("Typing into element: " + locator + " → " + text);
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        log("Getting text from: " + locator);
        return waitForVisibility(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ============================================================
    // NEW: Clear Helper
    // ============================================================

    protected void clear(By locator) {
        waitForVisibility(locator);
        driver.findElement(locator).clear();
    }

    // ============================================================
    // NEW: Attribute Helper
    // ============================================================

    protected String getAttribute(By locator, String attribute) {
        waitForVisibility(locator);
        return driver.findElement(locator).getAttribute(attribute);
    }

    // ============================================================
    // NEW: DOM Presence Helper
    // ============================================================

    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // ============================================================
    // NEW: Raw Click (no waits, used for exception tests)
    // ============================================================

    protected void rawClick(By locator) {
        driver.findElement(locator).click();
    }

    // ============================================================
    // JavaScript Helpers
    // ============================================================

    protected void jsClick(By locator) {
        log("JS-clicking element: " + locator);
        WebElement element = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoView(By locator) {
        log("Scrolling into view: " + locator);
        WebElement element = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // ============================================================
    // Safe Click (Selenium → JS fallback)
    // ============================================================

    protected void safeClick(By locator) {
        try {
            click(locator);
        } catch (Exception e) {
            log("Standard click failed, falling back to JS click for: " + locator);
            jsClick(locator);
        }
    }

    // ============================================================
    // Page Load Helpers
    // ============================================================

    protected void waitForPageLoad() {
        log("Waiting for page load to complete");
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    // ============================================================
    // URL Helpers
    // ============================================================

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void waitForUrlContains(String partialUrl) {
        log("Waiting for URL to contain: " + partialUrl);
        wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    // ============================================================
    // Visibility Helpers (Dynamic Elements)
    // ============================================================

    protected boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}