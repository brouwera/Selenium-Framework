package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    // Locators
    private final By practiceButton = By.xpath("//a[contains(text(),'Practice')]");
    private final By testLoginLink = By.xpath("//a[contains(text(),'Test Login Page')]");
    private final By testInputsLink = By.xpath("//a[contains(text(),'Test Inputs Page')]");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================
    // Actions with Allure Steps
    // ============================
    @Step("Click 'Practice' button on Home Page")
    public void clickPracticeButton() {
        click(practiceButton);
    }

    @Step("Navigate to Test Login Page")
    public LoginPage clickTestLoginLink() {
        click(testLoginLink);
        return new LoginPage(driver, wait);
    }

    @Step("Navigate to Test Inputs Page")
    public InputsPage clickTestInputsLink() {
        click(testInputsLink);
        return new InputsPage(driver, wait);
    }
}