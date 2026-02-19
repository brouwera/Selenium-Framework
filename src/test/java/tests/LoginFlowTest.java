package tests;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import pages.SuccessfulLoginPage;

@Epic("Login")
@Feature("Login Flow")
public class LoginFlowTest extends BaseTest {

    @Story("User can log in through navigation flow")
    @Test(description = "Validates that a user can navigate through the site and successfully log in")
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
        AssertionHelper.assertTrue(
                success.isSuccessMessageDisplayed(),
                "Success message should be visible after login" );

        AssertionHelper.assertTrue(
                success.isLogoutButtonDisplayed(),
                "Logout button should be visible after login" );
    }

    @Story("User can log out after a successful login")
    @Test(description = "Validates that a logged-in user can log out and return to the Login page")
    public void userCanLogoutAfterSuccessfulLogin() {

        // Start from the main Practice Test Automation homepage
        driver.get("https://practicetestautomation.com/");

        // Navigate into the Practice section
        HomePage home = new HomePage(driver, wait);
        home.clickPracticeButton();

        // Navigate to the Test Login Page
        LoginPage login = home.clickTestLoginLink();

        // Perform a valid login
        SuccessfulLoginPage success = login.login("student", "Password123");

        // Validate login success
        AssertionHelper.assertTrue(
                success.isSuccessMessageDisplayed(),
                "Success message should be visible after login" );

        // Perform logout
        LoginPage loginPageAfterLogout = success.clickLogoutButton();

        // Validate that the user is redirected back to the Login page
        AssertionHelper.assertTrue(
                loginPageAfterLogout.isLoginButtonDisplayed(),
                "Login button should be visible after logout" );
    }
}