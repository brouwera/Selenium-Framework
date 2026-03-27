package pages;

import config.ConfigManager;
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
    private final By dynamicControlsLink = By.linkText("Dynamic Controls");

    // ============================================================
    // Constructor
    // ============================================================
    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Page Load
    // ============================================================
    @Step("Open Practice Test Automation Home Page")
    public HomePage open() {
        String url = ConfigManager.getPracticeBaseUrl() + "/practice/";
        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Navigation Helpers
    // ============================================================
    @Step("Navigate to Test Login Page")
    public LoginPage goToLoginPage() {
        waitForVisibility(testLoginLink);
        click(testLoginLink);
        waitForPageLoad();
        return new LoginPage(driver, wait);
    }

    @Step("Navigate to Test Exceptions Page")
    public ExceptionsPage goToExceptionsPage() {
        waitForVisibility(testExceptionsLink);
        click(testExceptionsLink);
        waitForPageLoad();
        return new ExceptionsPage(driver, wait);
    }

    @Step("Navigate to Test Table Page")
    public TablePage goToTablePage() {
        waitForVisibility(testTableLink);
        click(testTableLink);
        waitForPageLoad();
        return new TablePage(driver, wait);
    }

    @Step("Navigate to Dynamic Controls Page")
    public DynamicControlsPage goToDynamicControlsPage() {
        waitForVisibility(dynamicControlsLink);
        click(dynamicControlsLink);
        waitForPageLoad();
        return new DynamicControlsPage(driver, wait);
    }
}