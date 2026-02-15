package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver; this.wait = wait;
    }

    public boolean isSuccessMessageDisplayed() {
        WebElement message = wait.until( ExpectedConditions.visibilityOfElementLocated(successMessage) );
        return message.isDisplayed();
    }

    public boolean isLogoutButtonDisplayed() {
        WebElement logout = wait.until( ExpectedConditions.visibilityOfElementLocated(logoutButton) );
        return logout.isDisplayed();
    }
}