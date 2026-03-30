package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Dynamic Controls Page
 * Handles checkbox removal/addition, input enable/disable,
 * and loading indicator synchronization.
 */
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
    @Step("Open Dynamic Controls Page")
    public DynamicControlsPage open() {
        String url = ConfigManager.getHerokuBaseUrl() + "/dynamic_controls";
        navigateTo(url);
        waitForPageLoad();
        loadingObserved = false;
        return this;
    }

    @Step("Wait for Dynamic Controls Page to be ready")
    public DynamicControlsPage waitForPageReady() {
        waitForVisibility(removeAddButton);
        waitForVisibility(enableDisableButton);
        return this;
    }

    @Step("Verify Dynamic Controls Page is loaded")
    public boolean isPageLoaded() {
        return isElementVisible(removeAddButton) && isElementVisible(enableDisableButton);
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

    @Step("Click Remove/Add button (expecting loading indicator)")
    public DynamicControlsPage clickRemoveOrAddExpectingLoading() {
        click(removeAddButton);
        observeLoadingIndicator();
        waitForInvisibility(loadingIndicator);
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
        waitForCondition(driver -> find(inputField).isEnabled());
        return this;
    }

    @Step("Wait for input field to become disabled")
    public DynamicControlsPage waitForInputToBeDisabled() {
        waitForCondition(driver -> !find(inputField).isEnabled());
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
        observeLoadingIndicator();
        return this;
    }

    @Step("Wait for loading indicator to disappear")
    public DynamicControlsPage waitForLoadingToDisappear() {
        waitForInvisibility(loadingIndicator);
        return this;
    }

    @Step("Wait for loading cycle to complete")
    public DynamicControlsPage waitForLoadingCycle() {
        observeLoadingIndicator();
        waitForInvisibility(loadingIndicator);
        return this;
    }

    private void observeLoadingIndicator() {
        try {
            waitForVisibility(loadingIndicator);
            loadingObserved = true;
        } catch (Exception ignored) {
            // Spinner may appear and disappear too quickly — expected behavior
        }
    }

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