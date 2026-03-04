package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class IFramePage extends BasePage {

    private final By editorIframe = By.id("mce_0_ifr");
    private final By editorBody = By.id("tinymce");
    private final By popupCloseButton = By.cssSelector("button.tox-notification__dismiss");

    public IFramePage(BaseTest test) {
        super(test);
    }

    @Step("Open iFrame page")
    public IFramePage open() {
        navigateToHeroku("iframe");
        return this;
    }

    @Step("Close TinyMCE read-only popup if present")
    public IFramePage closePopupIfPresent() {
        switchToDefault();
        if (isElementPresent(popupCloseButton)) {
            click(popupCloseButton);
            waitForInvisibility(popupCloseButton);
        }
        return this;
    }

    private void enterEditorFrame() {
        switchToDefault();
        waitForPresence(editorIframe);
        driver.switchTo().frame(driver.findElement(editorIframe));
    }

    @Step("Get default text from the TinyMCE editor")
    public String getDefaultEditorText() {

        // Close popup BEFORE entering iframe
        closePopupIfPresent();

        // TinyMCE reloads the iframe after popup closes → re-enter it
        enterEditorFrame();

        // Wait for the editor body to be visible
        waitForVisibility(editorBody);

        // Wait for TinyMCE to inject default text
        waitUntil(driver -> {
            String text = driver.findElement(editorBody).getText().trim();
            return !text.isEmpty();
        });

        String text = driver.findElement(editorBody).getText().trim();

        switchToDefault();
        return text;
    }
}