package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IFramePage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By editorIframe = By.id("mce_0_ifr");
    private final By editorBody = By.id("tinymce");
    private final By popupCloseButton = By.cssSelector("button.tox-notification__dismiss");

    // ============================================================
    // Constructor
    // ============================================================
    public IFramePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open iFrame page")
    public IFramePage open() {
        String url = ConfigManager.getHerokuBaseUrl() + "/iframe";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Popup Handling
    // ============================================================
    @Step("Close TinyMCE read-only popup if present")
    public IFramePage closePopupIfPresent() {
        switchToDefault();

        if (isElementPresent(popupCloseButton)) {
            click(popupCloseButton);
            waitForInvisibility(popupCloseButton);
        }

        return this;
    }

    // ============================================================
    // Frame Handling
    // ============================================================
    private void enterEditorFrame() {
        switchToDefault();
        switchToFrame(editorIframe);
    }

    private void exitEditorFrame() {
        switchToDefault();
    }

    // ============================================================
    // Editor Interactions
    // ============================================================
    @Step("Get default text from the TinyMCE editor")
    public String getDefaultEditorText() {
        closePopupIfPresent();
        enterEditorFrame();

        waitForVisibility(editorBody);
        String text = find(editorBody).getText().trim();

        exitEditorFrame();
        return text;
    }

    @Step("Clear editor text")
    public IFramePage clearEditor() {
        closePopupIfPresent();
        enterEditorFrame();

        WebElement body = find(editorBody);
        body.clear();

        exitEditorFrame();
        return this;
    }

    @Step("Type into TinyMCE editor: {text}")
    public IFramePage typeIntoEditor(String text) {
        closePopupIfPresent();
        enterEditorFrame();

        WebElement body = find(editorBody);
        body.clear();
        body.sendKeys(text);

        exitEditorFrame();
        return this;
    }

    @Step("Get current editor text")
    public String getEditorText() {
        enterEditorFrame();
        String text = find(editorBody).getText().trim();
        exitEditorFrame();
        return text;
    }
}