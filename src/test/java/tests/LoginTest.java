package tests;

import base.BaseTest;
import dataproviders.LoginDataProvider;
import helpers.AssertionHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.SuccessfulLoginPage;

import java.util.Map;

@Epic("Login")
@Feature("Login Flow")
public class LoginTest extends BaseTest {

    private LoginPage goToLoginPage() {
        // BaseTest already loads https://practicetestautomation.com/practice/
        HomePage home = new HomePage(getDriver(), getWait());
        return home.clickTestLoginLink();   // Correct navigation
    }

    // ============================
    // Data-Driven Login Test
    // ============================
    @Story("Data-driven login validation")
    @Test(
            dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class,
            description = "Validates login behavior using CSV-driven test data"
    )
    public void loginDataDrivenTest(Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");
        String expectedResult = data.get("expectedResult");

        LoginPage login = goToLoginPage();

        if (expectedResult.equalsIgnoreCase("success")) {
            SuccessfulLoginPage success = login.login(username, password);

            AssertionHelper.assertTrue(
                    success.isSuccessMessageDisplayed(),
                    "Success message should be visible after login"
            );

            AssertionHelper.assertTrue(
                    success.isLogoutButtonDisplayed(),
                    "Logout button should be visible after login"
            );

        } else {
            login.login(username, password);

            AssertionHelper.assertTrue(
                    login.isErrorMessageDisplayed(),
                    "Error message should be visible for invalid login"
            );
        }
    }

    // ============================
    // Existing Flow Tests
    // ============================

    @Story("User can log in through navigation flow")
    @Test(description = "Validates that a user can navigate through the site and successfully log in")
    public void userCanLoginThroughNavigationFlow() {
        LoginPage login = goToLoginPage();
        SuccessfulLoginPage success = login.login("student", "Password123");

        AssertionHelper.assertTrue(
                success.isSuccessMessageDisplayed(),
                "Success message should be visible after login"
        );

        AssertionHelper.assertTrue(
                success.isLogoutButtonDisplayed(),
                "Logout button should be visible after login"
        );
    }

    @Story("User can log out after a successful login")
    @Test(description = "Validates that a logged-in user can log out and return to the Login page")
    public void userCanLogoutAfterSuccessfulLogin() {
        LoginPage login = goToLoginPage();
        SuccessfulLoginPage success = login.login("student", "Password123");

        AssertionHelper.assertTrue(
                success.isSuccessMessageDisplayed(),
                "Success message should be visible after login"
        );

        LoginPage loginPageAfterLogout = success.clickLogoutButton();

        AssertionHelper.assertTrue(
                loginPageAfterLogout.isLoginButtonDisplayed(),
                "Login button should be visible after logout"
        );
    }
}