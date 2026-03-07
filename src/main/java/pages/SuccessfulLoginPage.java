package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuccessfulLoginPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    // ============================================================
    // Constructor
    // ============================================================
    public SuccessfulLoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Page Load
    // ============================================================
    @Step("Verify Successful Login Page is loaded")
    public boolean isPageLoaded() {
        return isDisplayed(successMessage) && isDisplayed(logoutButton);
    }

    // ============================================================
    // Visibility Checks
    // ============================================================
    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    @Step("Check if Logout button is displayed")
    public boolean isLogoutButtonDisplayed() {
        return isDisplayed(logoutButton);
    }

    @Step("Get success message text")
    public String getSuccessMessageText() {
        return getText(successMessage);
    }

    // ============================================================
    // Actions
    // ============================================================
    @Step("Click Logout button")
    public LoginPage clickLogoutButton() {
        click(logoutButton);
        return new LoginPage(driver, wait);
    }
}