package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By testLoginLink = By.linkText("Test Login Page");
    private final By testExceptionsLink = By.linkText("Test Exceptions");
    private final By testTableLink = By.linkText("Test Table");

    // ============================================================
    // Constructor
    // ============================================================
    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Page Load
    // ============================================================
    @Step("Open Practice Home Page")
    public HomePage open() {
        navigateTo("https://practicetestautomation.com/practice/");
        return this;
    }

    // ============================================================
    // Navigation Helpers
    // ============================================================
    @Step("Navigate to Login Page")
    public LoginPage goToLoginPage() {
        click(testLoginLink);
        return new LoginPage(driver, wait);
    }

    @Step("Navigate to Exceptions Page")
    public ExceptionsPage goToExceptionsPage() {
        click(testExceptionsLink);
        return new ExceptionsPage(driver, wait);
    }

    @Step("Navigate to Table Page")
    public TablePage goToTablePage() {
        click(testTableLink);
        return new TablePage(driver, wait);
    }
}