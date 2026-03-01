package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait shortWait; // 3-second wait for TimeoutException tests

    // ============================================================
    // Logger (SLF4J)
    // ============================================================
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

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
    protected void logStep(String message) {
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
        log.info("Waiting for visibility of element: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForPresence(By locator) {
        log.info("Waiting for presence of element: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected boolean waitForInvisibility(By locator) {
        log.info("Waiting for invisibility of element: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForClickable(By locator) {
        log.info("Waiting for element to be clickable: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ============================================================
    // Core Actions
    // ============================================================
    protected void click(By locator) {
        log.info("Clicking element: {}", locator);
        logStep("Clicking element: " + locator);
        waitForClickable(locator);
        driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        log.info("Typing '{}' into element: {}", text, locator);
        logStep("Typing into element: " + locator + " → " + text);
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        log.info("Getting text from element: {}", locator);
        logStep("Getting text from: " + locator);
        return waitForVisibility(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        log.info("Checking if element is displayed: {}", locator);
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            log.warn("Element NOT displayed: {}", locator);
            return false;
        }
    }

    // ============================================================
    // Clear Helper
    // ============================================================
    protected void clear(By locator) {
        log.info("Clearing element: {}", locator);
        waitForVisibility(locator);
        driver.findElement(locator).clear();
    }

    // ============================================================
    // Attribute Helper
    // ============================================================
    protected String getAttribute(By locator, String attribute) {
        log.info("Getting attribute '{}' from element: {}", attribute, locator);
        waitForVisibility(locator);
        return driver.findElement(locator).getAttribute(attribute);
    }

    // ============================================================
    // DOM Presence Helper
    // ============================================================
    protected boolean isElementPresent(By locator) {
        log.info("Checking if element is present in DOM: {}", locator);
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            log.warn("Element NOT present in DOM: {}", locator);
            return false;
        }
    }

    // ============================================================
    // Raw Click (no waits)
    // ============================================================
    protected void rawClick(By locator) {
        log.info("Performing RAW click on element: {}", locator);
        driver.findElement(locator).click();
    }

    // ============================================================
    // JavaScript Helpers
    // ============================================================
    protected void jsClick(By locator) {
        log.info("Performing JS click on element: {}", locator);
        logStep("JS-clicking element: " + locator);
        WebElement element = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoView(By locator) {
        log.info("Scrolling element into view: {}", locator);
        logStep("Scrolling into view: " + locator);
        WebElement element = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // ============================================================
    // Safe Click (Selenium → JS fallback)
    // ============================================================
    protected void safeClick(By locator) {
        log.info("Attempting safe click on element: {}", locator);
        try {
            click(locator);
        } catch (Exception e) {
            log.warn("Standard click failed, falling back to JS click for: {}", locator);
            logStep("Standard click failed, falling back to JS click for: " + locator);
            jsClick(locator);
        }
    }

    // ============================================================
    // Page Load Helpers
    // ============================================================
    protected void waitForPageLoad() {
        log.info("Waiting for page load to complete");
        logStep("Waiting for page load to complete");
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    // ============================================================
    // URL Helpers
    // ============================================================
    protected String getCurrentUrl() {
        log.info("Getting current URL");
        return driver.getCurrentUrl();
    }

    protected void waitForUrlContains(String partialUrl) {
        log.info("Waiting for URL to contain: {}", partialUrl);
        logStep("Waiting for URL to contain: " + partialUrl);
        wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    // ============================================================
    // Visibility Helpers (Dynamic Elements)
    // ============================================================
    protected boolean isElementVisible(By locator) {
        log.info("Checking if element is visible: {}", locator);
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            log.warn("Element NOT visible: {}", locator);
            return false;
        }
    }
}