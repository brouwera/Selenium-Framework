package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private final BaseTest test;

    // ============================================================
    // Constructor
    // ============================================================

    public LoginPage(BaseTest test) {
        super(test);
        this.test = test;
    }

    // ============================================================
    // Locators
    // ============================================================

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.id("submit");
    private final By errorMessage = By.id("error");

    // ============================================================
    // Individual Actions
    // ============================================================

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    @Step("Click Login button")
    public SuccessfulLoginPage clickLoginButton() {
        click(submitButton);
        return new SuccessfulLoginPage(test);
    }

    // ============================================================
    // Composite Action
    // ============================================================

    @Step("Log in as user: {username}")
    public SuccessfulLoginPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }

    // ============================================================
    // Visibility Checks
    // ============================================================

    @Step("Verify Login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return isDisplayed(submitButton);
    }

    @Step("Check if error message is visible")
    public boolean isErrorMessageVisible() {
        return isDisplayed(errorMessage);
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}