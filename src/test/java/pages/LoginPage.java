package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators private final
    By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("submit");
    private final By errorMessage = By.id("error");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; this.wait = wait;
    }

    public void enterUsername(String username) {
        WebElement user = wait.until( ExpectedConditions.visibilityOfElementLocated(usernameField) );
        user.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement pass = wait.until( ExpectedConditions.visibilityOfElementLocated(passwordField) );
        pass.sendKeys(password);
    }

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

    public String getErrorMessage() {
        WebElement error = wait.until( ExpectedConditions.visibilityOfElementLocated(errorMessage) );
        return error.getText();
    }

    public String getPasswordFieldType() {
        WebElement pass = wait.until( ExpectedConditions.visibilityOfElementLocated(passwordField) );
        return pass.getAttribute("type");
    }
}