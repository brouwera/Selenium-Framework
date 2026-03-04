package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

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
    public NestedFramesPage(BaseTest test) {
        super(test);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Nested Frames page")
    public NestedFramesPage open() {
        navigateToHeroku("nested_frames");
        return this;
    }

    // ============================================================
    // Frame Text Retrieval
    // ============================================================
    @Step("Get text from Left Frame")
    public String getLeftFrameText() {
        switchToFrame(topFrame);
        switchToFrame(leftFrame);
        String text = getText(body);
        switchToDefault();
        return text;
    }

    @Step("Get text from Middle Frame")
    public String getMiddleFrameText() {
        switchToFrame(topFrame);
        switchToFrame(middleFrame);
        String text = getText(body);
        switchToDefault();
        return text;
    }

    @Step("Get text from Right Frame")
    public String getRightFrameText() {
        switchToFrame(topFrame);
        switchToFrame(rightFrame);
        String text = getText(body);
        switchToDefault();
        return text;
    }

    @Step("Get text from Bottom Frame")
    public String getBottomFrameText() {
        switchToFrame(bottomFrame);
        String text = getText(body);
        switchToDefault();
        return text;
    }
}