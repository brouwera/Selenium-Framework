package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class FramesPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By nestedFramesLink = By.linkText("Nested Frames");
    private final By iFrameLink = By.linkText("iFrame");

    // ============================================================
    // Constructor
    // ============================================================
    public FramesPage(BaseTest test) {
        super(test);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Frames page")
    public FramesPage open() {
        navigateToHeroku("frames");
        return this;
    }

    // ============================================================
    // Actions
    // ============================================================
    @Step("Click Nested Frames link")
    public NestedFramesPage clickNestedFrames() {
        click(nestedFramesLink);
        return new NestedFramesPage(test);
    }

    @Step("Click iFrame link")
    public IFramePage clickIFrame() {
        click(iFrameLink);
        return new IFramePage(test);
    }
}