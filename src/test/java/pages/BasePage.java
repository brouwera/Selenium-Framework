package pages;

import base.BaseTest;
import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class BasePage {

    // ============================================================
    // Test Context (Required for Page → Page navigation)
    // ============================================================
    protected final BaseTest test;

    // ============================================================
    // Driver + Waits
    // ============================================================
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
        this.test = test;
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

    protected void waitUntil(Function<WebDriver, Boolean> condition) {
        wait.until(condition);
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

    @Step("Type into element without clearing: {locator}")
    protected void typeWithoutClearing(By locator, String text) {
        log.info("Typing WITHOUT clearing into element: {}", locator);
        logStep("Typing WITHOUT clearing into: " + locator + " → " + text);
        WebElement element = waitForVisibility(locator);
        element.sendKeys(text);
    }

    // ============================================================
    // Text Helpers
    // ============================================================
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
    // Dropdown Helper
    // ============================================================
    protected void selectByVisibleText(By locator, String text) {
        log.info("Selecting '{}' from dropdown: {}", text, locator);
        logStep("Selecting '" + text + "' from dropdown: " + locator);
        WebElement element = waitForVisibility(locator);
        new Select(element).selectByVisibleText(text);
    }

    // ============================================================
    // Checkbox Helper
    // ============================================================
    protected void setCheckbox(By locator, boolean checked) {
        log.info("Setting checkbox {} to {}", locator, checked);
        WebElement element = waitForVisibility(locator);

        boolean isSelected = element.isSelected();
        if (isSelected != checked) {
            element.click();
        }
    }

    // ============================================================
    // Multiple Elements Helper
    // ============================================================
    protected List<WebElement> findElements(By locator) {
        log.info("Finding elements: {}", locator);
        return driver.findElements(locator);
    }

    // ============================================================
    // Navigation Helpers (Option C)
    // ============================================================

    protected void navigateToPractice(String relativeUrl) {
        String base = ConfigManager.getPracticeBaseUrl();
        String fullUrl = base + relativeUrl.replaceFirst("^/", "");

        log.info("Navigating to Practice URL: {}", fullUrl);
        logStep("Navigating to Practice URL: " + fullUrl);

        driver.get(fullUrl);
        waitForPageLoad();
    }

    protected void navigateToHeroku(String relativeUrl) {
        String base = ConfigManager.getHerokuBaseUrl();
        String fullUrl = base + relativeUrl.replaceFirst("^/", "");

        log.info("Navigating to Heroku URL: {}", fullUrl);
        logStep("Navigating to Heroku URL: " + fullUrl);

        driver.get(fullUrl);
        waitForPageLoad();
    }

    // Absolute URL (still useful)
    protected void navigateToAbsoluteUrl(String url) {
        log.info("Navigating to absolute URL: {}", url);
        logStep("Navigating to: " + url);
        driver.get(url);
        waitForPageLoad();
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

    // ============================================================
    // Frame Helpers
    // ============================================================
    protected void switchToFrame(By locator) {
        log.info("Switching to frame: {}", locator);
        driver.switchTo().frame(driver.findElement(locator));
    }

    protected void switchToDefault() {
        log.info("Switching to default content");
        driver.switchTo().defaultContent();
    }
}