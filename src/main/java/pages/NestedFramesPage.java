package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NestedFramesPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By topFrame = By.name("frame-top");
    private final By leftFrame = By.name("frame-left");
    private final By middleFrame = By.name("frame-middle");
    private final By rightFrame = By.name("frame-right");
    private final By bottomFrame = By.name("frame-bottom");
    private final By body = By.tagName("body");

    // ============================================================
    // Constructor
    // ============================================================
    public NestedFramesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Nested Frames page")
    public NestedFramesPage open() {
        String url = ConfigManager.getHerokuBaseUrl() + "/nested_frames";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Frame Helpers
    // ============================================================
    private void enterTopFrame() {
        switchToDefault();
        switchToFrame(topFrame);
    }

    private void enterLeftFrame() {
        enterTopFrame();
        switchToFrame(leftFrame);
    }

    private void enterMiddleFrame() {
        enterTopFrame();
        switchToFrame(middleFrame);
    }

    private void enterRightFrame() {
        enterTopFrame();
        switchToFrame(rightFrame);
    }

    private void enterBottomFrame() {
        switchToDefault();
        switchToFrame(bottomFrame);
    }

    private String readBodyText() {
        waitForVisibility(body);
        return getText(body).trim();
    }

    // ============================================================
    // Frame Text Retrieval
    // ============================================================
    @Step("Get text from Left Frame")
    public String getLeftFrameText() {
        enterLeftFrame();
        String text = readBodyText();
        switchToDefault();
        return text;
    }

    @Step("Get text from Middle Frame")
    public String getMiddleFrameText() {
        enterMiddleFrame();
        String text = readBodyText();
        switchToDefault();
        return text;
    }

    @Step("Get text from Right Frame")
    public String getRightFrameText() {
        enterRightFrame();
        String text = readBodyText();
        switchToDefault();
        return text;
    }

    @Step("Get text from Bottom Frame")
    public String getBottomFrameText() {
        enterBottomFrame();
        String text = readBodyText();
        switchToDefault();
        return text;
    }
}