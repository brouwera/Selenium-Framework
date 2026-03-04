package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SuccessfulLoginPage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    // ============================================================
    // Constructor
    // ============================================================
    public SuccessfulLoginPage(BaseTest test) {
        super(test);
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

    // ============================================================
    // Actions
    // ============================================================
    @Step("Click Logout button")
    public LoginPage clickLogoutButton() {
        click(logoutButton);
        return new LoginPage(test);
    }
}