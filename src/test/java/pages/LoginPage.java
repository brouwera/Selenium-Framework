package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.id("submit");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Logging in with username: {username} and password: {password}")
    public SuccessfulLoginPage login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(submitButton);
        return new SuccessfulLoginPage(driver, wait);
    }

    @Step("Check if Login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return isVisible(submitButton);
    }
}