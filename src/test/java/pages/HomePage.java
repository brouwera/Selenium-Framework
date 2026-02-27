package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private final BaseTest test;

    // ============================================================
    // Locators (Match Practice Page)
    // ============================================================

    private final By testLoginLink = By.linkText("Test Login Page");
    private final By testExceptionsLink = By.linkText("Test Exceptions");

    // ============================================================
    // Constructor
    // ============================================================

    public HomePage(BaseTest test) {
        super(test);
        this.test = test;
    }

    // ============================================================
    // Page Load
    // ============================================================

    @Step("Open Practice Home Page")
    public HomePage open() {
        driver.get("https://practicetestautomation.com/practice/");
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Direct Navigation Helpers
    // ============================================================

    @Step("Navigate to Login Page")
    public LoginPage goToLoginPage() {
        open();
        click(testLoginLink);
        return new LoginPage(test);
    }

    @Step("Navigate to Exceptions Page")
    public ExceptionsPage goToExceptionsPage() {
        open();
        click(testExceptionsLink);
        return new ExceptionsPage(test);
    }

    // ============================================================
    // Raw Click Actions
    // ============================================================

    @Step("Click Test Login Page link")
    public LoginPage clickTestLoginLink() {
        click(testLoginLink);
        return new LoginPage(test);
    }

    @Step("Click Test Exceptions link")
    public ExceptionsPage clickTestExceptionsLink() {
        click(testExceptionsLink);
        return new ExceptionsPage(test);
    }
}