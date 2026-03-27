package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FramesPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By nestedFramesLink = By.linkText("Nested Frames");
    private final By iFrameLink = By.linkText("iFrame");

    // ============================================================
    // Constructor
    // ============================================================
    public FramesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Frames page")
    public FramesPage open() {
        String url = ConfigManager.getHerokuBaseUrl() + "/frames";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Visibility Checks
    // ============================================================
    @Step("Check if Nested Frames link is visible")
    public boolean isNestedFramesLinkVisible() {
        return isElementVisible(nestedFramesLink);
    }

    @Step("Check if iFrame link is visible")
    public boolean isIFrameLinkVisible() {
        return isElementVisible(iFrameLink);
    }

    // Optional semantic waits for consistency
    @Step("Wait for Nested Frames link to be visible")
    public FramesPage waitForNestedFramesLink() {
        waitForVisibility(nestedFramesLink);
        return this;
    }

    @Step("Wait for iFrame link to be visible")
    public FramesPage waitForIFrameLink() {
        waitForVisibility(iFrameLink);
        return this;
    }

    // ============================================================
    // Actions
    // ============================================================
    @Step("Click Nested Frames link")
    public NestedFramesPage clickNestedFrames() {
        waitForVisibility(nestedFramesLink);
        click(nestedFramesLink);
        waitForPageLoad();
        return new NestedFramesPage(driver, wait);
    }

    @Step("Click iFrame link")
    public IFramePage clickIFrame() {
        waitForVisibility(iFrameLink);
        click(iFrameLink);
        waitForPageLoad();
        return new IFramePage(driver, wait);
    }
}