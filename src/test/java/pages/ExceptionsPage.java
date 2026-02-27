package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    public ExceptionsPage(BaseTest test) {
        super(test);
    }

    // ============================================================
    // Row 2 Actions
    // ============================================================

    @Step("Click Add button")
    public ExceptionsPage clickAddButton() {
        click(addButton);
        waitForVisibility(row2Input); // Row 2 appears after ~5 seconds
        return this;
    }

    @Step("Check if Row 2 input is visible")
    public boolean isRow2InputVisible() {
        return isElementVisible(row2Input);
    }

    @Step("Enter text into Row 2 input: {text}")
    public ExceptionsPage enterTextRow2(String text) {
        waitForVisibility(row2Input);
        type(row2Input, text);
        return this;
    }

    @Step("Click Save button on Row 2")
    public ExceptionsPage clickRow2Save() {
        click(row2SaveButton);
        return this;
    }

    // ============================================================
    // Row 1 Actions
    // ============================================================

    @Step("Click Edit button on Row 1")
    public ExceptionsPage clickRow1Edit() {
        click(row1EditButton);
        wait.until(ExpectedConditions.elementToBeClickable(row1Input));
        return this;
    }

    @Step("Clear Row 1 input")
    public ExceptionsPage clearRow1Input() {
        waitForVisibility(row1Input);
        driver.findElement(row1Input).clear();
        return this;
    }

    @Step("Enter text into Row 1 input: {text}")
    public ExceptionsPage enterTextRow1(String text) {
        waitForVisibility(row1Input);
        type(row1Input, text);
        return this;
    }

    @Step("Get value from Row 1 input")
    public String getRow1InputValue() {
        waitForVisibility(row1Input);
        return driver.findElement(row1Input).getAttribute("value");
    }

    // ============================================================
    // Visibility Checks
    // ============================================================

    @Step("Check if instructions text is displayed")
    public boolean isInstructionsDisplayed() {
        return isElementVisible(instructionsText);
    }

    @Step("Get confirmation message")
    public String getConfirmationMessage() {
        waitForVisibility(confirmationMessage);
        return getText(confirmationMessage);
    }

    @Step("Get error message")
    public String getErrorMessage() {
        waitForVisibility(errorMessage);
        return getText(errorMessage);
    }

    // ============================================================
    // Short Timeout (TimeoutException Test)
    // ============================================================

    @Step("Wait for Row 2 input with short timeout (3 seconds)")
    public boolean waitForRow2InputShortTimeout() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(row2Input));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}