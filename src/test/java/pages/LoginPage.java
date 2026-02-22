package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    // ============================
    // Locators
    // ============================
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.id("submit");
    private final By errorMessage = By.id("error"); // NEW

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================
    // Individual Actions
    // ============================
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    @Step("Enter password: ******")
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    @Step("Click Login button")
    public SuccessfulLoginPage clickLoginButton() {
        click(submitButton);
        return new SuccessfulLoginPage(driver, wait);
    }

    // ============================
    // Composite Action
    // ============================
    @Step("Log in as user: {username}")
    public SuccessfulLoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    // ============================
    // Assertions / Visibility Checks
    // ============================
    @Step("Verify Login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return isVisible(submitButton);
    }

    @Step("Check if error message is visible")
    public boolean isErrorMessageVisible() {
        return isVisible(errorMessage);
    }

    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isErrorMessageVisible();
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}