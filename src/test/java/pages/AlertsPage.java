package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AlertsPage extends BasePage {

    // Locators
    private final By jsAlertButton = By.xpath("//button[text()='Click for JS Alert']");
    private final By jsConfirmButton = By.xpath("//button[text()='Click for JS Confirm']");
    private final By jsPromptButton = By.xpath("//button[text()='Click for JS Prompt']");
    private final By resultMessage = By.id("result");

    public AlertsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================
    // Trigger Alerts
    // ============================

    @Step("Trigger JavaScript Alert")
    public AlertsPage clickJsAlert() {
        click(jsAlertButton);
        return this;
    }

    @Step("Trigger JavaScript Confirm dialog")
    public AlertsPage clickJsConfirm() {
        click(jsConfirmButton);
        return this;
    }

    @Step("Trigger JavaScript Prompt dialog")
    public AlertsPage clickJsPrompt() {
        click(jsPromptButton);
        return this;
    }

    // ============================
    // Alert Handling
    // ============================

    @Step("Accept the alert")
    public AlertsPage acceptAlert() {
        Alert alert = driver.switchTo().alert();
        alert.accept();
        return this;
    }

    @Step("Dismiss the alert")
    public AlertsPage dismissAlert() {
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
        return this;
    }

    @Step("Enter text into prompt: {text}")
    public AlertsPage enterTextIntoPrompt(String text) {
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
        return this;
    }

    @Step("Get alert text")
    public String getAlertText() {
        return driver.switchTo().alert().getText();
    }

    // ============================
    // Result Message
    // ============================

    @Step("Get result message displayed on page")
    public String getResultMessage() {
        return getText(resultMessage);
    }
}