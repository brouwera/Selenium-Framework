package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AlertsPage;
import pages.HomePage;

@Epic("Alerts")
@Feature("JavaScript Alerts")
public class AlertsTest extends BaseTest {

    @Test(description = "User can accept a simple JavaScript alert")
    @Story("Accept JS Alert")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can trigger and accept a JavaScript alert and see the correct result message.")
    public void testAcceptJsAlert() {
        HomePage home = new HomePage(getDriver(), wait);
        AlertsPage alerts = home.clickTestAlertsLink();

        alerts.clickJsAlert()
                .acceptAlert();

        String result = alerts.getResultMessage();
        Assert.assertEquals(result, "You successfully clicked an alert");
    }

    @Test(description = "User can dismiss a JavaScript confirm dialog")
    @Story("Dismiss JS Confirm")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can trigger and dismiss a JavaScript confirm dialog and see the correct result message.")
    public void testDismissJsConfirm() {
        HomePage home = new HomePage(getDriver(), wait);
        AlertsPage alerts = home.clickTestAlertsLink();

        alerts.clickJsConfirm()
                .dismissAlert();

        String result = alerts.getResultMessage();
        Assert.assertEquals(result, "You clicked: Cancel");
    }

    @Test(description = "User can enter text into a JavaScript prompt")
    @Story("Enter Text in JS Prompt")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the user can enter text into a JavaScript prompt and see the correct result message.")
    public void testEnterTextInJsPrompt() {
        HomePage home = new HomePage(getDriver(), wait);
        AlertsPage alerts = home.clickTestAlertsLink();

        String inputText = "Adam";
        alerts.clickJsPrompt()
                .enterTextIntoPrompt(inputText)
                .acceptAlert();

        String result = alerts.getResultMessage();
        Assert.assertEquals(result, "You entered: " + inputText);
    }
}