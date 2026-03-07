package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.id("submit");
    private final By errorMessage = By.id("error");

    // ============================================================
    // Constructor
    // ============================================================
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Login Page")
    public LoginPage open() {
        driver.get("https://practicetestautomation.com/practice-test-login/");
        return this;
    }

    @Step("Verify Login Page is loaded")
    public boolean isPageLoaded() {
        return isDisplayed(usernameField) && isDisplayed(submitButton);
    }

    // ============================================================
    // Field Actions
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
        return new SuccessfulLoginPage(driver, wait);
    }

    // ============================================================
    // Composite Actions (Positive Flow)
    // ============================================================
    @Step("Log in as user: {username}")
    public SuccessfulLoginPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }

    @Step("Attempt login expecting success for user: {username}")
    public SuccessfulLoginPage loginExpectingSuccess(String username, String password) {
        return login(username, password);
    }

    // ============================================================
    // Negative Login Flow
    // ============================================================
    @Step("Attempt login expecting failure for user: {username}")
    public String loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        click(submitButton);
        return getErrorMessage();
    }

    // ============================================================
    // Utility Helpers
    // ============================================================
    @Step("Clear login form")
    public LoginPage clearLoginForm() {
        clear(usernameField);
        clear(passwordField);
        return this;
    }

    public boolean isErrorMessage(String expected) {
        return getErrorMessage().equalsIgnoreCase(expected.trim());
    }

    // ============================================================
    // Visibility + Text Helpers
    // ============================================================
    @Step("Check if Login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return isDisplayed(submitButton);
    }

    @Step("Check if error message is visible")
    public boolean isErrorMessageVisible() {
        return isDisplayed(errorMessage);
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return getText(errorMessage).replaceAll("\\s+", " ").trim();
    }
}