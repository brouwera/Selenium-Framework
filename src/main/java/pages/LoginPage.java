package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.id("submit");
    private final By errorMessage = By.id("error");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open Login Page")
    public LoginPage open() {
        navigateTo("https://practicetestautomation.com/practice-test-login/");
        return this;
    }

    @Step("Verify Login Page is loaded")
    public boolean isPageLoaded() {
        return isDisplayed(usernameField) && isDisplayed(submitButton);
    }

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
        find(passwordField).sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Get password field type attribute")
    public String getPasswordFieldType() {
        return getAttribute(passwordField, "type");
    }

    @Step("Click Login button")
    public SuccessfulLoginPage clickLoginButton() {
        click(submitButton);
        return new SuccessfulLoginPage(driver, wait);
    }

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

    @Step("Clear login form")
    public LoginPage clearLoginForm() {
        clearUsername();
        clearPassword();
        return this;
    }

    public boolean isErrorMessage(String expected) {
        return getErrorMessage().equalsIgnoreCase(expected.trim());
    }

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