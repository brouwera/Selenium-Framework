package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Locators
    private By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    // Page actions
    public boolean isSuccessMessageDisplayed() {
        WebElement message = wait.until( ExpectedConditions.visibilityOfElementLocated(successMessage) );
        return message.isDisplayed();
    }

    public boolean isLogoutButtonDisplayed() {
        WebElement logout = wait.until( ExpectedConditions.visibilityOfElementLocated(logoutButton) );
        return logout.isDisplayed();
    }
}