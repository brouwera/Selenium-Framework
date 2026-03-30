package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Drag and Drop Page
 * Handles HTML5 drag-and-drop interactions using both
 * JavaScript simulation (primary) and Actions API (fallback).
 */
public class DragAndDropPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By columnA = By.id("column-a");
    private final By columnB = By.id("column-b");
    private final By headerA = By.cssSelector("#column-a header");
    private final By headerB = By.cssSelector("#column-b header");

    // ============================================================
    // Constructor
    // ============================================================
    public DragAndDropPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Drag and Drop Page")
    public DragAndDropPage open() {
        String url = ConfigManager.getHerokuBaseUrl() + "/drag_and_drop";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    @Step("Wait for Drag and Drop Page to be ready")
    public DragAndDropPage waitForPageReady() {
        waitForVisibility(columnA);
        waitForVisibility(columnB);
        return this;
    }

    @Step("Verify Drag and Drop Page is loaded")
    public boolean isPageLoaded() {
        return isElementVisible(columnA) && isElementVisible(columnB);
    }

    // ============================================================
    // Drag and Drop Actions (JS Primary)
    // ============================================================
    @Step("Drag Column A to Column B (HTML5 JS simulation)")
    public DragAndDropPage dragAtoB() {
        performHtml5DragAndDrop(columnA, columnB);
        return this;
    }

    @Step("Drag Column B to Column A (HTML5 JS simulation)")
    public DragAndDropPage dragBtoA() {
        performHtml5DragAndDrop(columnB, columnA);
        return this;
    }

    // ============================================================
    // Drag and Drop Actions (Actions Fallback)
    // ============================================================
    @Step("Drag Column A to Column B (Actions API fallback)")
    public DragAndDropPage dragAtoBWithActions() {
        performActionsDragAndDrop(columnA, columnB);
        return this;
    }

    @Step("Drag Column B to Column A (Actions API fallback)")
    public DragAndDropPage dragBtoAWithActions() {
        performActionsDragAndDrop(columnB, columnA);
        return this;
    }

    // ============================================================
    // State Verification
    // ============================================================
    @Step("Get header text for Column A")
    public String getColumnAHeaderText() {
        return getText(headerA).replaceAll("\\s+", " ").trim();
    }

    @Step("Get header text for Column B")
    public String getColumnBHeaderText() {
        return getText(headerB).replaceAll("\\s+", " ").trim();
    }

    @Step("Check if Column A header is '{expected}'")
    public boolean isColumnAHeader(String expected) {
        return getColumnAHeaderText().equalsIgnoreCase(expected.trim());
    }

    @Step("Check if Column B header is '{expected}'")
    public boolean isColumnBHeader(String expected) {
        return getColumnBHeaderText().equalsIgnoreCase(expected.trim());
    }

    // ============================================================
    // Internal Helpers
    // ============================================================
    private void performHtml5DragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = find(sourceLocator);
        WebElement target = find(targetLocator);

        String script =
                "function createEvent(typeOfEvent) {" +
                        "  var event = document.createEvent('CustomEvent');" +
                        "  event.initCustomEvent(typeOfEvent, true, true, null);" +
                        "  event.dataTransfer = {" +
                        "    data: {}," +
                        "    setData: function(key, value) { this.data[key] = value; }," +
                        "    getData: function(key) { return this.data[key]; }" +
                        "  };" +
                        "  return event;" +
                        "}" +
                        "function dispatchEvent(element, event) {" +
                        "  if (element.dispatchEvent) {" +
                        "    element.dispatchEvent(event);" +
                        "  } else if (element.fireEvent) {" +
                        "    element.fireEvent('on' + event.type, event);" +
                        "  }" +
                        "}" +
                        "function simulateHTML5DragAndDrop(source, target) {" +
                        "  var dragStartEvent = createEvent('dragstart');" +
                        "  dispatchEvent(source, dragStartEvent);" +
                        "  var dropEvent = createEvent('drop');" +
                        "  dropEvent.dataTransfer = dragStartEvent.dataTransfer;" +
                        "  dispatchEvent(target, dropEvent);" +
                        "  var dragEndEvent = createEvent('dragend');" +
                        "  dragEndEvent.dataTransfer = dropEvent.dataTransfer;" +
                        "  dispatchEvent(source, dragEndEvent);" +
                        "}" +
                        "simulateHTML5DragAndDrop(arguments[0], arguments[1]);";

        ((JavascriptExecutor) driver).executeScript(script, source, target);
        safeSleep(300);
    }

    private void performActionsDragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = find(sourceLocator);
        WebElement target = find(targetLocator);

        Actions actions = new Actions(driver);
        actions.dragAndDrop(source, target).build().perform();
        safeSleep(300);
    }
}