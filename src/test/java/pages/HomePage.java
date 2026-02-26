package pages;

import base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private final BaseTest test;

    // ============================================================
    // Locators (Match Actual Practice Page)
    // ============================================================
    private final By testLoginLink = By.linkText("Test Login Page");

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
        driver.get("https://practice.expandtesting.com/");
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Navigation Actions
    // ============================================================

    @Step("Navigate to Test Login Page")
    public LoginPage clickTestLoginLink() {
        click(testLoginLink);
        return new LoginPage(test);
    }
}