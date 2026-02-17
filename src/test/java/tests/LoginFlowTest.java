package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import pages.SuccessfulLoginPage;

public class LoginFlowTest extends BaseTest {

    // Validates that a user can navigate through the site and successfully log in
    @Test
    public void userCanLoginThroughNavigationFlow() {

        // Start from the main Practice Test Automation homepage
        driver.get("https://practicetestautomation.com/");

        // Navigate into the Practice section
        HomePage home = new HomePage(driver, wait);
        home.clickPracticeButton();

        // Navigate to the Test Login Page
        LoginPage login = home.clickTestLoginLink();

        // Perform a valid login and land on the success page
        SuccessfulLoginPage success = login.login("student", "Password123");

        // Validate that the success page is displayed correctly
        Assert.assertTrue(success.isSuccessMessageDisplayed());
        Assert.assertTrue(success.isLogoutButtonDisplayed());
    }
}