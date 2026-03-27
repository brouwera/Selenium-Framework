package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExceptionsPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By addButton = By.id("add_btn");

    private final By row1Input = By.cssSelector("#row1 input.input-field");
    private final By row1EditButton = By.cssSelector("#row1 button[name='Edit']");

    private final By row2Input = By.cssSelector("#row2 input.input-field");
    private final By row2SaveButton = By.cssSelector("#row2 button[name='Save']");

    private final By instructionsText = By.id("instructions");

    private final By confirmationMessage = By.id("confirmation");
    private final By errorMessage = By.id("error");

    // ============================================================
    // Constructor
    // ============================================================
    public ExceptionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Exceptions Page")
    public ExceptionsPage open() {
        String url = ConfigManager.getPracticeBaseUrl() + "/practice-test-exceptions/";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Row 2 Actions
    // ============================================================
    @Step("Click Add button to reveal Row 2")
    public ExceptionsPage clickAddButton() {
        click(addButton);
        waitForVisibility(row2Input);
        return this;
    }

    @Step("Wait for Row 2 input to appear")
    public ExceptionsPage waitForRow2ToAppear() {
        waitForVisibility(row2Input);
        return this;
    }

    @Step("Check if Row 2 input is visible")
    public boolean isRow2InputVisible() {
        return isElementVisible(row2Input);
    }

    @Step("Enter text into Row 2 input: {text}")
    public ExceptionsPage enterTextRow2(String text) {
        type(row2Input, text);
        return this;
    }

    @Step("Click Save button on Row 2")
    public ExceptionsPage clickRow2Save() {
        click(row2SaveButton);
        return this;
    }

    @Step("Wait for Row 2 input using short timeout")
    public boolean waitForRow2InputShortTimeout() {
        try {
            waitForCondition(driver -> isElementVisible(row2Input));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ============================================================
    // Row 1 Actions
    // ============================================================
    @Step("Click Edit button on Row 1")
    public ExceptionsPage clickRow1Edit() {
        click(row1EditButton);
        waitForVisibility(row1Input);
        return this;
    }

    @Step("Wait for Row 1 input to appear")
    public ExceptionsPage waitForRow1ToAppear() {
        waitForVisibility(row1Input);
        return this;
    }

    @Step("Clear Row 1 input")
    public ExceptionsPage clearRow1Input() {
        clear(row1Input);
        return this;
    }

    @Step("Enter text into Row 1 input: {text}")
    public ExceptionsPage enterTextRow1(String text) {
        type(row1Input, text);
        return this;
    }

    @Step("Get value from Row 1 input")
    public String getRow1InputValue() {
        return getAttribute(row1Input, "value");
    }

    @Step("Check if Row 1 input is enabled")
    public boolean isRow1InputEnabled() {
        return find(row1Input).isEnabled();
    }

    // ============================================================
    // Intentional Exception Trigger (for testing)
    // ============================================================
    @Step("Click invisible Save button (expected ElementNotInteractableException)")
    public ExceptionsPage clickInvisibleSaveButton() {
        log.info("RAW CLICK (expected failure): {}", By.name("Save"));
        driver.findElement(By.name("Save")).click(); // intentionally bypass BasePage
        return this;
    }

    // ============================================================
    // Instructions Text
    // ============================================================
    @Step("Check if instructions text is displayed")
    public boolean isInstructionsDisplayed() {
        return isElementVisible(instructionsText);
    }

    @Step("Check if instructions element is present in DOM")
    public boolean isInstructionsPresentInDOM() {
        return isElementPresent(instructionsText);
    }

    @Step("Wait for instructions text to disappear from DOM")
    public boolean waitForInstructionsToDisappear() {
        try {
            waitForInvisibility(instructionsText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================================
    // Messages
    // ============================================================
    @Step("Get confirmation message")
    public String getConfirmationMessage() {
        return getText(confirmationMessage).replaceAll("\\s+", " ").trim();
    }

    @Step("Get error message")
    public String getErrorMessage() {
        return getText(errorMessage).replaceAll("\\s+", " ").trim();
    }
}