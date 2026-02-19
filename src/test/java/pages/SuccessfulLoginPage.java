package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuccessfulLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page elements for the "Logged In Successfully" confirmation screen
    private final By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    // Constructor initializes driver and wait for this page object
    public SuccessfulLoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; this.wait = wait;
    }

    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage));
        return message.isDisplayed();
    }

    @Step("Check if logout button is displayed")
    public boolean isLogoutButtonDisplayed() {
        WebElement logout = wait.until(
                ExpectedConditions.visibilityOfElementLocated(logoutButton));
        return logout.isDisplayed();
    }

    @Step("Get success message text")
    public String getSuccessMessageText() {
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage));
        return message.getText();
    }

    @Step("Get page title")
    public String getPageTitle() {
        return driver.getTitle();
    }

    @Step("Get current page URL")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ============================
    // Added for Day 10 Flow Tests
    // ============================

    @Step("Click Logout button")
    public LoginPage clickLogoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
        return new LoginPage(driver, wait);
    }
}