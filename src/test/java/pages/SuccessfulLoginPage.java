package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SuccessfulLoginPage extends BasePage {

    private final By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    // ============================================================
    // Constructor
    // ============================================================
    public SuccessfulLoginPage() {
        super(); // ThreadLocal driver + wait
    }

    // ============================================================
    // Assertions / Visibility Checks
    // ============================================================

    @Step("Verify success message is displayed on Successful Login Page")
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    @Step("Verify Logout button is displayed on Successful Login Page")
    public boolean isLogoutButtonDisplayed() {
        return isDisplayed(logoutButton);
    }

    // ============================================================
    // Actions
    // ============================================================

    @Step("Click Logout button to return to Login Page")
    public LoginPage clickLogoutButton() {
        click(logoutButton);
        return new LoginPage();
    }
}