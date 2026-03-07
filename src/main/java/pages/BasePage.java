package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // ============================================================
    // Constructor
    // ============================================================
    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ============================================================
    // Core Element Helpers
    // ============================================================
    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        find(locator).click();
    }

    protected void type(By locator, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElement el = find(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void clear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        find(locator).clear();
    }

    protected String getText(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return find(locator).getText();
    }

    protected String getAttribute(By locator, String attribute) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return find(locator).getAttribute(attribute);
    }

    // ============================================================
    // Visibility & Presence Helpers
    // ============================================================
    protected boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isElementVisible(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isElementPresent(By locator) {
        return !findAll(locator).isEmpty();
    }

    // ============================================================
    // Wait Helpers
    // ============================================================
    protected void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForPresence(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ============================================================
    // Navigation
    // ============================================================
    protected void navigateTo(String url) {
        driver.get(url);
    }

    // ============================================================
    // Frame Helpers
    // ============================================================
    protected void switchToFrame(By locator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    protected void switchToDefault() {
        driver.switchTo().defaultContent();
    }
}