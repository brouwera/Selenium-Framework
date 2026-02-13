package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================
    // Locators
    // ============================
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("submit");
    private By errorMessage = By.id("error");

    // ============================
    // Actions
    // ============================

    // Enters text into the username field
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    // Enters text into the password field
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    // Clicks the Login button
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    // Performs a full login action using username + password
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Retrieves the error message text displayed on the page
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    // Retrieves the input type of the password field (used to verify masking)
    public String getPasswordFieldType() {
        return driver.findElement(passwordField).getAttribute("type");
    }
}