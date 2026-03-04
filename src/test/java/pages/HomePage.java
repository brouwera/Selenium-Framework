package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    // ============================================================
    // Locators (Practice Page)
    // ============================================================
    private final By testLoginLink = By.linkText("Test Login Page");
    private final By testExceptionsLink = By.linkText("Test Exceptions");
    private final By testTableLink = By.linkText("Test Table");

    // ============================================================
    // Constructor
    // ============================================================
    public HomePage(BaseTest test) {
        super(test);
    }

    // ============================================================
    // Page Load
    // ============================================================
    @Step("Open Practice Home Page")
    public HomePage open() {
        navigateToPractice("");
        return this;
    }

    // ============================================================
    // Navigation Helpers
    // ============================================================
    @Step("Navigate to Login Page")
    public LoginPage goToLoginPage() {
        click(testLoginLink);
        return new LoginPage(test);
    }

    @Step("Navigate to Exceptions Page")
    public ExceptionsPage goToExceptionsPage() {
        click(testExceptionsLink);
        return new ExceptionsPage(test);
    }

    @Step("Navigate to Table Page")
    public TablePage goToTablePage() {
        click(testTableLink);
        return new TablePage(test);
    }
}