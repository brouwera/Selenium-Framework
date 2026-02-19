package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("submit");
    private final By errorMessage = By.id("error");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Navigate to login page
    @Step("Navigate to Login Page")
    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/practice-test-login/");
        return this;
    }

    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        WebElement user = wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField));
        user.sendKeys(username);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        WebElement pass = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField));
        pass.sendKeys(password);
    }

    @Step("Click Login button")
    public SuccessfulLoginPage clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return new SuccessfulLoginPage(driver, wait);
    }

    @Step("Login with username: {username} and password")
    public SuccessfulLoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    @Step("Click Login button without entering credentials")
    public void clickLoginOnly() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return error.getText();
    }

    @Step("Check if error message is visible")
    public boolean isErrorMessageVisible() {
        try {
            WebElement error = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get password field type attribute")
    public String getPasswordFieldType() {
        WebElement pass = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField));
        return pass.getAttribute("type");
    }

    @Step("Check if Login button is displayed")
    public boolean isLoginButtonDisplayed() {
        try {
            WebElement button = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(loginButton));
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}