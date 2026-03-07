package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // Step counter resets per test via MDC routing (each test has its own logger instance)
    private static final ThreadLocal<AtomicInteger> stepCounter =
            ThreadLocal.withInitial(() -> new AtomicInteger(1));

    // ============================================================
    // Constructor
    // ============================================================
    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.actions = new Actions(driver);
    }

    // ============================================================
    // Log Formatting Helpers
    // ============================================================
    protected String fmt(By locator) {
        return locator.toString()
                .replace("By.", "[")
                .replace(": ", "=") + "]";
    }

    protected void step(String action, Runnable runnable) {
        int stepNum = stepCounter.get().getAndIncrement();
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        log.info("[{}] {} ({} ms)", String.format("%02d", stepNum), action, end - start);
    }

    // ============================================================
    // Unified Find Helpers (with logging + stale retry)
    // ============================================================
    protected WebElement find(By locator) {
        return stepFind(locator);
    }

    private WebElement stepFind(By locator) {
        return stepReturn("FIND " + fmt(locator), () -> {
            try {
                return driver.findElement(locator);
            } catch (StaleElementReferenceException e) {
                log.warn("FIND STALE → retry {}", fmt(locator));
                return driver.findElement(locator);
            }
        });
    }

    protected List<WebElement> findAll(By locator) {
        return stepReturn("FIND ALL " + fmt(locator),
                () -> driver.findElements(locator));
    }

    private <T> T stepReturn(String action, SupplierWithException<T> supplier) {
        int stepNum = stepCounter.get().getAndIncrement();
        long start = System.currentTimeMillis();
        try {
            T result = supplier.get();
            long end = System.currentTimeMillis();
            log.info("[{}] {} ({} ms)", String.format("%02d", stepNum), action, end - start);
            return result;
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            log.info("[{}] {} FAILED ({} ms)", String.format("%02d", stepNum), action, end - start);
            throw e;
        }
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get();
    }

    // ============================================================
    // Core Interaction Helpers
    // ============================================================
    protected void click(By locator) {
        step("CLICK " + fmt(locator), () -> {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(locator));
                find(locator).click();
            } catch (Exception e) {
                log.warn("CLICK FAILED → JS CLICK {}", fmt(locator));
                jsClick(locator);
            }
        });
    }

    protected void type(By locator, String text) {
        step("TYPE " + fmt(locator) + " → '" + text + "'", () -> {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement el = find(locator);
            el.clear();
            el.sendKeys(text);
        });
    }

    protected void clear(By locator) {
        step("CLEAR " + fmt(locator), () -> {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            find(locator).clear();
        });
    }

    protected String getText(By locator) {
        final String[] result = new String[1];
        step("GET TEXT " + fmt(locator), () -> {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            result[0] = find(locator).getText();
        });
        return result[0];
    }

    protected String getAttribute(By locator, String attribute) {
        final String[] result = new String[1];
        step("GET ATTRIBUTE '" + attribute + "' from " + fmt(locator), () -> {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            result[0] = find(locator).getAttribute(attribute);
        });
        return result[0];
    }

    // ============================================================
    // Hover Helper
    // ============================================================
    protected void hover(By locator) {
        step("HOVER " + fmt(locator), () -> {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            actions.moveToElement(el).perform();
        });
    }

    // ============================================================
    // JS Helpers
    // ============================================================
    protected void jsClick(By locator) {
        step("JS CLICK " + fmt(locator), () -> {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        });
    }

    protected void scrollIntoView(By locator) {
        step("SCROLL INTO VIEW " + fmt(locator), () -> {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
        });
    }

    // ============================================================
    // Visibility & Presence Helpers
    // ============================================================
    protected boolean isDisplayed(By locator) {
        try {
            boolean visible = find(locator).isDisplayed();
            log.info("IS DISPLAYED {} → {}", fmt(locator), visible);
            return visible;
        } catch (Exception e) {
            log.info("IS DISPLAYED {} → false (exception)", fmt(locator));
            return false;
        }
    }

    protected boolean isElementVisible(By locator) {
        try {
            boolean visible = find(locator).isDisplayed();
            log.info("IS VISIBLE {} → {}", fmt(locator), visible);
            return visible;
        } catch (Exception e) {
            log.info("IS VISIBLE {} → false (exception)", fmt(locator));
            return false;
        }
    }

    protected boolean isElementPresent(By locator) {
        boolean present = !findAll(locator).isEmpty();
        log.info("IS PRESENT {} → {}", fmt(locator), present);
        return present;
    }

    // ============================================================
    // Wait Helpers
    // ============================================================
    protected void waitForVisibility(By locator) {
        step("WAIT visibilityOf " + fmt(locator), () ->
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        );
    }

    protected void waitForInvisibility(By locator) {
        step("WAIT invisibilityOf " + fmt(locator), () ->
                wait.until(ExpectedConditions.invisibilityOfElementLocated(locator))
        );
    }

    protected void waitForPresence(By locator) {
        step("WAIT presenceOf " + fmt(locator), () ->
                wait.until(ExpectedConditions.presenceOfElementLocated(locator))
        );
    }

    protected void waitForText(By locator, String expected) {
        step("WAIT textToBe '" + expected + "' in " + fmt(locator), () ->
                wait.until(ExpectedConditions.textToBe(locator, expected))
        );
    }

    protected void waitForAttribute(By locator, String attribute, String value) {
        step("WAIT attribute '" + attribute + "'='" + value + "' in " + fmt(locator), () ->
                wait.until(ExpectedConditions.attributeToBe(locator, attribute, value))
        );
    }

    protected void waitForElementToDisappear(By locator) {
        step("WAIT elementToDisappear " + fmt(locator), () ->
                wait.until(ExpectedConditions.invisibilityOfElementLocated(locator))
        );
    }

    // ============================================================
    // Network Idle Wait (placeholder)
    // ============================================================
    protected void waitForNetworkIdle() {
        step("WAIT network idle (placeholder)", () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        });
    }

    // ============================================================
    // Navigation
    // ============================================================
    protected void navigateTo(String url) {
        step("NAVIGATE " + url, () -> driver.get(url));
    }

    // ============================================================
    // Frame Helpers
    // ============================================================
    protected void switchToFrame(By locator) {
        step("SWITCH TO FRAME " + fmt(locator), () ->
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator))
        );
    }

    protected void switchToDefault() {
        step("SWITCH TO DEFAULT CONTENT", () ->
                driver.switchTo().defaultContent()
        );
    }
}