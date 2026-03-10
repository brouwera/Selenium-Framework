package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DynamicControlsPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By checkbox = By.id("checkbox");
    private final By removeAddButton = By.xpath("//button[text()='Remove' or text()='Add']");
    private final By enableDisableButton = By.xpath("//button[text()='Enable' or text()='Disable']");
    private final By loadingIndicator = By.id("loading");
    private final By message = By.id("message");
    private final By inputField = By.xpath("//input[@type='text']");

    // Tracks whether the spinner was ever visible
    private boolean loadingObserved = false;

    // ============================================================
    // Constructor
    // ============================================================
    public DynamicControlsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Dynamic Controls page")
    public DynamicControlsPage open() {
        String url = ConfigManager.getHerokuBaseUrl() + "/dynamic_controls";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Checkbox Actions
    // ============================================================
    @Step("Click Remove/Add button")
    public DynamicControlsPage clickRemoveOrAdd() {
        click(removeAddButton);
        waitForLoadingCycle();
        return this;
    }

    @Step("Click Remove/Add and verify loading indicator appears")
    public DynamicControlsPage clickRemoveOrAddExpectingLoading() {
        click(removeAddButton);
        waitForLoadingToAppear();
        waitForLoadingToDisappear();
        return this;
    }

    @Step("Check if checkbox is present in DOM")
    public boolean isCheckboxPresent() {
        return isElementPresent(checkbox);
    }

    @Step("Wait for checkbox to disappear")
    public DynamicControlsPage waitForCheckboxToDisappear() {
        waitForInvisibility(checkbox);
        return this;
    }

    @Step("Wait for checkbox to appear")
    public DynamicControlsPage waitForCheckboxToAppear() {
        waitForVisibility(checkbox);
        return this;
    }

    // ============================================================
    // Input Field Actions
    // ============================================================
    @Step("Click Enable/Disable button")
    public DynamicControlsPage clickEnableOrDisable() {
        click(enableDisableButton);
        waitForLoadingCycle();
        return this;
    }

    @Step("Check if input field is enabled")
    public boolean isInputEnabled() {
        return find(inputField).isEnabled();
    }

    @Step("Wait for input field to become enabled")
    public DynamicControlsPage waitForInputToBeEnabled() {
        wait.until(driver -> find(inputField).isEnabled());
        return this;
    }

    @Step("Wait for input field to become disabled")
    public DynamicControlsPage waitForInputToBeDisabled() {
        wait.until(driver -> !find(inputField).isEnabled());
        return this;
    }

    // ============================================================
    // Loading Indicator
    // ============================================================
    @Step("Check if loading indicator is visible")
    public boolean isLoadingVisible() {
        return isElementVisible(loadingIndicator);
    }

    @Step("Wait for loading indicator to appear")
    public DynamicControlsPage waitForLoadingToAppear() {
        try {
            waitForVisibility(loadingIndicator);
            loadingObserved = true;
        } catch (Exception ignored) {
            // Spinner may appear too fast; ignore
        }
        return this;
    }

    @Step("Wait for loading indicator to disappear")
    public DynamicControlsPage waitForLoadingToDisappear() {
        waitForInvisibility(loadingIndicator);
        return this;
    }

    /**
     * Full loading cycle:
     * 1. Wait for spinner to appear (if it does)
     * 2. Wait for spinner to disappear
     */
    @Step("Wait for loading cycle to complete")
    public DynamicControlsPage waitForLoadingCycle() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loadingIndicator));
            loadingObserved = true;
        } catch (Exception ignored) {
            // Spinner may appear too fast; ignore
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIndicator));
        return this;
    }

    // Used by the test to confirm spinner was seen
    public boolean wasLoadingIndicatorObserved() {
        return loadingObserved;
    }

    // ============================================================
    // Message
    // ============================================================
    @Step("Get message text")
    public String getMessageText() {
        return getText(message).replaceAll("\\s+", " ").trim();
    }
}