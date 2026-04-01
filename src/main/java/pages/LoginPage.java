package pages;

import config.ConfigManager;
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
        String url = ConfigManager.getPracticeBaseUrl() + "/practice-test-login/";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    @Step("Wait for Login Page to be ready")
    public LoginPage waitForLoginPageReady() {
        waitForVisibility(usernameField);
        waitForVisibility(submitButton);
        return this;
    }

    @Step("Verify Login Page is loaded")
    public boolean isPageLoaded() {
        return isElementVisible(usernameField) && isElementVisible(submitButton);
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

    @Step("Clear username field")
    public LoginPage clearUsername() {
        clear(usernameField);
        return this;
    }

    @Step("Clear password field")
    public LoginPage clearPassword() {
        clear(passwordField);
        return this;
    }

    @Step("Submit login form using Enter key")
    public LoginPage submitWithEnterKey() {
        submitWithEnterKey(passwordField);
        return this;
    }

    @Step("Get password field type attribute")
    public String getPasswordFieldType() {
        return getAttribute(passwordField, "type");
    }

    // ============================================================
    // Button Actions
    // ============================================================
    @Step("Click Login button")
    public SuccessfulLoginPage clickLoginButton() {
        click(submitButton);
        waitForPageLoad();
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
        waitForErrorMessage();
        return getErrorMessage();
    }

    @Step("Wait for error message to appear")
    public void waitForErrorMessage() {
        waitForVisibility(errorMessage);
    }

    // ============================================================
    // Utility Helpers
    // ============================================================
    @Step("Clear login form")
    public LoginPage clearLoginForm() {
        clearUsername();
        clearPassword();
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
        return isElementVisible(submitButton);
    }

    @Step("Check if error message is visible")
    public boolean isErrorMessageVisible() {
        return isElementVisible(errorMessage);
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return getText(errorMessage).replaceAll("\\s+", " ").trim();
    }

    // ============================================================
    // Browser Console Log Exposure
    // ============================================================
    @Step("Attach browser console logs")
    public void attachBrowserConsoleLogs() {
        super.attachBrowserConsoleLogs();
    }
}