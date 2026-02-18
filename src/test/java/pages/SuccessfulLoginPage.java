package pages;

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
        this.driver = driver;
        this.wait = wait;
    }

    // Confirms that the success message is visible after a valid login
    public boolean isSuccessMessageDisplayed() {
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage) );
        return message.isDisplayed();
    }

    // Confirms that the logout button is visible on the success page
    public boolean isLogoutButtonDisplayed() {
        WebElement logout = wait.until(
                ExpectedConditions.visibilityOfElementLocated(logoutButton) );
        return logout.isDisplayed();
    }

    // Returns the visible success message text for assertions requiring exact content
    public String getSuccessMessageText() {
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage) );
        return message.getText();
    }

    // Returns the current page title
    public String getPageTitle() {
        return driver.getTitle();
    }

    // Returns the current page URL
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}