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
    @Step("Wait for Successful Login Page to be ready")
    public SuccessfulLoginPage waitForSuccessPageReady() {
        waitForVisibility(successMessage);
        waitForVisibility(logoutButton);
        return this;
    }

    @Step("Verify Successful Login Page is loaded")
    public boolean isPageLoaded() {
        return isElementVisible(successMessage) && isElementVisible(logoutButton);
    }

    // ============================================================
    // Visibility Checks
    // ============================================================
    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(successMessage);
    }

    @Step("Check if Logout button is displayed")
    public boolean isLogoutButtonDisplayed() {
        return isElementVisible(logoutButton);
    }

    // ============================================================
    // Text Helpers
    // ============================================================
    @Step("Get success message text")
    public String getSuccessMessageText() {
        return getText(successMessage).trim();
    }

    // ============================================================
    // Actions
    // ============================================================
    @Step("Click Logout button")
    public LoginPage clickLogoutButton() {
        click(logoutButton);
        waitForPageLoad();
        return new LoginPage(driver, wait);
    }
}