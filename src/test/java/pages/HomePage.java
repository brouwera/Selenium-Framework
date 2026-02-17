package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation elements on the main Practice Test Automation homepage
    private final By practiceButton = By.xpath("//a[contains(text(),'Practice')]");
    private final By testLoginLink = By.xpath("//a[contains(text(),'Test Login Page')]");

    // Constructor initializes driver and wait for this page object
    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Clicks the "Practice" button to navigate into the practice section
    public void clickPracticeButton() {
        wait.until(ExpectedConditions.elementToBeClickable(practiceButton)).click();
    }

    // Clicks the "Test Login Page" link and returns a new LoginPage object
    public LoginPage clickTestLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(testLoginLink)).click();
        return new LoginPage(driver, wait);
    }
}