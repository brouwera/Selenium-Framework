package pages;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    // ============================================================
    // Locators (Practice Test Automation site only)
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
    // Page Load (Slash‑Safe URL Normalization)
    // ============================================================
    @Step("Open Practice Test Automation Home Page")
    public HomePage open() {

        // Normalize base URL to avoid double slashes
        String base = ConfigManager.getPracticeBaseUrl().replaceAll("/+$", "");

        // PTA home page is always /practice/
        String url = base + "/practice/";

        navigateTo(url);
        waitForPageLoad();
        return this;
    }

    // ============================================================
    // Navigation Helpers (Practice Test Automation)
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

    // ============================================================
    // Navigation Helpers (Herokuapp)
    // ============================================================
    @Step("Navigate to Dynamic Controls Page")
    public DynamicControlsPage goToDynamicControlsPage() {

        String base = ConfigManager.getHerokuBaseUrl().replaceAll("/+$", "");
        String url = base + "/dynamic_controls";

        navigateTo(url);
        waitForPageLoad();
        return new DynamicControlsPage(driver, wait);
    }

    @Step("Navigate to Drag and Drop Page")
    public DragAndDropPage goToDragAndDropPage() {

        String base = ConfigManager.getHerokuBaseUrl().replaceAll("/+$", "");
        String url = base + "/drag_and_drop";

        navigateTo(url);
        waitForPageLoad();
        return new DragAndDropPage(driver, wait);
    }
}