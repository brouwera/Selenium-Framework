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
    // Frame Text Retrieval
    // ============================================================
    @Step("Get text from Left Frame")
    public String getLeftFrameText() {
        switchToDefault();
        switchToFrame(topFrame);
        switchToFrame(leftFrame);

        String text = getText(body).trim();

        switchToDefault();
        return text;
    }

    @Step("Get text from Middle Frame")
    public String getMiddleFrameText() {
        switchToDefault();
        switchToFrame(topFrame);
        switchToFrame(middleFrame);

        String text = getText(body).trim();

        switchToDefault();
        return text;
    }

    @Step("Get text from Right Frame")
    public String getRightFrameText() {
        switchToDefault();
        switchToFrame(topFrame);
        switchToFrame(rightFrame);

        String text = getText(body).trim();

        switchToDefault();
        return text;
    }

    @Step("Get text from Bottom Frame")
    public String getBottomFrameText() {
        switchToDefault();
        switchToFrame(bottomFrame);

        String text = getText(body).trim();

        switchToDefault();
        return text;
    }
}