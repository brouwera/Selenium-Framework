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
public class LoginTest extends BaseTest {

    private LoginPage goToLoginPage() {
        driver.get("https://practicetestautomation.com/");
        HomePage home = new HomePage(driver, wait);
        home.clickPracticeButton();
        return home.clickTestLoginLink();
    }

    @Story("User can log in through navigation flow")
    @Test(description = "Validates that a user can navigate through the site and successfully log in")
    public void userCanLoginThroughNavigationFlow() {
        LoginPage login = goToLoginPage();
        SuccessfulLoginPage success = login.login("student", "Password123");

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
        LoginPage login = goToLoginPage();
        SuccessfulLoginPage success = login.login("student", "Password123");

        AssertionHelper.assertTrue(
                success.isSuccessMessageDisplayed(),
                "Success message should be visible after login" );

        LoginPage loginPageAfterLogout = success.clickLogoutButton();

        AssertionHelper.assertTrue(
                loginPageAfterLogout.isLoginButtonDisplayed(),
                "Login button should be visible after logout" );
    }
}