package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InputsPage extends BasePage {

    private final By numberInput = By.id("number");

    public InputsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================
    // Actions
    // ============================

    @Step("Enter number: {value}")
    public InputsPage enterNumber(String value) {
        scrollToElement(numberInput);
        type(numberInput, value);
        return this;
    }

    @Step("Clear number input field")
    public InputsPage clearInput() {
        scrollToElement(numberInput);
        find(numberInput).clear();
        return this;
    }

    // ============================
    // Getters / Assertions
    // ============================

    @Step("Get current value from number input field")
    public String getInputValue() {
        return getAttribute(numberInput, "value");
    }
}