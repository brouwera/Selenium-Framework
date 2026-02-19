package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuccessfulLoginPage extends BasePage {

    private final By successMessage = By.xpath("//h1[contains(text(),'Logged In Successfully')]");
    private final By logoutButton = By.xpath("//a[contains(text(),'Log out')]");

    public SuccessfulLoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        return isVisible(successMessage);
    }

    @Step("Check if Logout button is displayed")
    public boolean isLogoutButtonDisplayed() {
        return isVisible(logoutButton);
    }

    @Step("Click Logout button")
    public LoginPage clickLogoutButton() {
        click(logoutButton);
        return new LoginPage(driver, wait);
    }
}