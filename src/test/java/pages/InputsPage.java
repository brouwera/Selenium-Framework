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
    // Actions with Allure Steps
    // ============================
    @Step("Enter number: {value}")
    public void enterNumber(String value) {
        scrollToElement(numberInput);
        type(numberInput, value);
    }

    @Step("Clear number input field")
    public void clearInput() {
        scrollToElement(numberInput);
        find(numberInput).clear();
    }

    // ============================
    // Getters
    // ============================
    public String getInputValue() {
        return getAttribute(numberInput, "value");
    }
}