package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private WebDriver driver;

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================
    // Locators
    // ============================
    private By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    // ============================
    // UI Checks
    // ============================

    // Returns true if the success message is displayed
    public boolean isSuccessMessageDisplayed() {
        return driver.findElement(successMessage).isDisplayed();
    }

    // Returns true if the Logout button is displayed
    public boolean isLogoutButtonDisplayed() {
        return driver.findElement(logoutButton).isDisplayed();
    }
}