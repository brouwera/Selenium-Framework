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

    // ============================================================
    // Navigation Helper
    // ============================================================

    private LoginPage navigateToLoginPage() {
        return new HomePage(this).clickTestLoginLink();
    }

    // ============================================================
    // Data-Driven Login Test
    // ============================================================

    @Story("Data-driven login validation")
    @Test(
            groups = {"regression"},
            dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class,
            description = "Validates login behavior using CSV-driven test data"
    )
    public void loginDataDrivenTest(Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");
        String expectedResult = data.get("expectedResult");

        LoginPage loginPage = navigateToLoginPage();

        if (expectedResult.equalsIgnoreCase("success")) {

            SuccessfulLoginPage successPage = loginPage.login(username, password);

            AssertionHelper.assertTrue(
                    successPage.isSuccessMessageDisplayed(),
                    "Success message should be visible after successful login"
            );

            AssertionHelper.assertTrue(
                    successPage.isLogoutButtonDisplayed(),
                    "Logout button should be visible after successful login"
            );

        } else {

            loginPage.login(username, password);

            AssertionHelper.assertTrue(
                    loginPage.isErrorMessageVisible(),
                    "Error message should be visible for invalid login"
            );
        }
    }

    // ============================================================
    // Positive Flow Tests
    // ============================================================

    @Story("User can log in through navigation flow")
    @Test(
            groups = {"smoke"},
            description = "Validates that a user can navigate through the site and successfully log in"
    )
    public void userCanLoginThroughNavigationFlow() {

        LoginPage loginPage = navigateToLoginPage();
        SuccessfulLoginPage successPage = loginPage.login("student", "Password123");

        AssertionHelper.assertTrue(
                successPage.isSuccessMessageDisplayed(),
                "Success message should be visible after login"
        );

        AssertionHelper.assertTrue(
                successPage.isLogoutButtonDisplayed(),
                "Logout button should be visible after login"
        );
    }

    @Story("User can log out after a successful login")
    @Test(
            groups = {"smoke"},
            description = "Validates that a logged-in user can log out and return to the Login page"
    )
    public void userCanLogoutAfterSuccessfulLogin() {

        LoginPage loginPage = navigateToLoginPage();
        SuccessfulLoginPage successPage = loginPage.login("student", "Password123");

        AssertionHelper.assertTrue(
                successPage.isSuccessMessageDisplayed(),
                "Success message should be visible after login"
        );

        LoginPage loginPageAfterLogout = successPage.clickLogoutButton();

        AssertionHelper.assertTrue(
                loginPageAfterLogout.isLoginButtonDisplayed(),
                "Login button should be visible after logout"
        );
    }
}