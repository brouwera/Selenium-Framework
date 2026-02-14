package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("submit");
    private By errorMessage = By.id("error");

    // Enter username
    public void enterUsername(String username) {
        WebElement user = wait.until( ExpectedConditions.visibilityOfElementLocated(usernameField) );
        user.sendKeys(username);
    }

    // Enter password
    public void enterPassword(String password) {
        WebElement pass = wait.until( ExpectedConditions.visibilityOfElementLocated(passwordField) );
        pass.sendKeys(password);
    }

    // Click login button
    public void clickLoginButton() {
        WebElement button = wait.until( ExpectedConditions.elementToBeClickable(loginButton) );
        button.click();
    }

    // Combined login action
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    // Get error message text
    public String getErrorMessage() {
        WebElement error = wait.until( ExpectedConditions.visibilityOfElementLocated(errorMessage) );
        return error.getText();
    }

    // Get the type attribute of the password field
    public String getPasswordFieldType() {
        WebElement pass = wait.until( ExpectedConditions.visibilityOfElementLocated(passwordField) );
        return pass.getAttribute("type");
    }

    // Click login without entering anything
    public void clickLogin() { clickLoginButton();
    }
}