package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuccessfulLoginPage extends BasePage {

    private final By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    public SuccessfulLoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================
    // Assertions / Visibility Checks
    // ============================

    @Step("Verify success message is displayed on Successful Login Page")
    public boolean isSuccessMessageDisplayed() {
        return isVisible(successMessage);
    }

    @Step("Verify Logout button is displayed on Successful Login Page")
    public boolean isLogoutButtonDisplayed() {
        return isVisible(logoutButton);
    }

    // ============================
    // Actions
    // ============================

    @Step("Click Logout button to return to Login Page")
    public LoginPage clickLogoutButton() {
        click(logoutButton);
        return new LoginPage(driver, wait);
    }
}