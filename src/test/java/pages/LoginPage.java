package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Input fields and actions for the Practice Test Automation login page
    By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("submit");
    private final By errorMessage = By.id("error");

    // Constructor initializes driver and wait for this page object
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; this.wait = wait;
    }

    // Enters the provided username into the username input field
    public void enterUsername(String username) {
        WebElement user = wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField) );
        user.sendKeys(username);
    }

    // Enters the provided password into the password input field
    public void enterPassword(String password) {
        WebElement pass = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField) );
        pass.sendKeys(password);
    }

    // Clicks the login button and returns the next page object upon successful login
    public SuccessfulLoginPage clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return new SuccessfulLoginPage(driver, wait);
    }

    // Performs a full login action using the provided credentials
    public SuccessfulLoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    // Retrieves the error message text displayed after an invalid login attempt
    public String getErrorMessage() {
        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage) );
        return error.getText();
    }

    // Returns the HTML 'type' attribute of the password field (e.g., "password")
    public String getPasswordFieldType() {
        WebElement pass = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField) );
        return pass.getAttribute("type");
    }
}