package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    // ============================================================
    // Locators (Match Actual Practice Page)
    // ============================================================
    private final By testLoginLink = By.linkText("Test Login Page");

    // ============================================================
    // Constructor
    // ============================================================
    public HomePage() {
        super(); // ThreadLocal driver + wait
    }

    // ============================================================
    // Navigation Actions
    // ============================================================

    @Step("Navigate to Test Login Page")
    public LoginPage clickTestLoginLink() {
        click(testLoginLink);
        return new LoginPage();
    }
}