package tests;

import base.BaseTest;
import dataproviders.LoginDataProvider;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.SuccessfulLoginPage;

import java.util.Map;

@Epic("Login")
@Feature("Login Flow")
public class LoginTest extends BaseTest {

    // ============================================================
    // Navigation Helpers
    // ============================================================
    @Step("Navigate to Login Page")
    private LoginPage navigateToLoginPage() {
        return new HomePage(getDriver(), getWait())
                .open()
                .goToLoginPage();
    }

    // ============================================================
    // Data-Driven Login Test
    // ============================================================
    @Story("Data-driven login validation")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
    @Description("Validates login behavior using CSV-driven test data for both positive and negative scenarios.")
    @Test(
            groups = {"regression"},
            dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class
    )
    public void loginDataDrivenTest(Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");
        String expectedResult = data.get("expectedResult").toLowerCase().trim();

        LoginPage loginPage = navigateToLoginPage();

        switch (expectedResult) {

            case "success" -> {
                SuccessfulLoginPage successPage = loginPage.loginExpectingSuccess(username, password);

                AssertionHelper.assertTrue(
                        successPage.isSuccessMessageDisplayed(),
                        "Success message should be visible after successful login"
                );

                AssertionHelper.assertTrue(
                        successPage.isLogoutButtonDisplayed(),
                        "Logout button should be visible after successful login"
                );
            }

            case "failure" -> {
                String actualError = loginPage.loginExpectingFailure(username, password);

                AssertionHelper.assertTrue(
                        loginPage.isErrorMessageVisible(),
                        "Error message should be visible for invalid login"
                );

                AssertionHelper.assertEquals(
                        actualError,
                        loginPage.getErrorMessage(),
                        "Error message text should match expected"
                );
            }

            default -> throw new IllegalArgumentException(
                    "Invalid expectedResult value in CSV: " + expectedResult
            );
        }
    }

    // ============================================================
    // Positive Flow Tests
    // ============================================================
    @Story("User can log in through navigation flow")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("Adam Brouwer")
    @Description("Validates that a user can navigate through the site and successfully log in.")
    @Test(groups = {"smoke"})
    public void userCanLoginThroughNavigationFlow() {

        LoginPage loginPage = navigateToLoginPage();
        loginPage.attachBrowserConsoleLogs();

        SuccessfulLoginPage successPage = loginPage.loginExpectingSuccess("student", "Password123");

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
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
    @Description("Validates that a logged-in user can log out and return to the Login page.")
    @Test(groups = {"smoke"})
    public void userCanLogoutAfterSuccessfulLogin() {

        LoginPage loginPage = navigateToLoginPage();
        SuccessfulLoginPage successPage = loginPage.loginExpectingSuccess("student", "Password123");

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

    // ============================================================
    // Standalone Negative Tests
    // ============================================================
    @Story("Invalid username produces correct error message")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Ensures that an invalid username triggers the expected error message.")
    @Test(groups = {"regression"})
    public void invalidUsernameShowsError() {

        LoginPage loginPage = navigateToLoginPage();
        String error = loginPage.loginExpectingFailure("wrongUser", "Password123");

        AssertionHelper.assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible");
        AssertionHelper.assertEquals(error, "Your username is invalid!", "Error message should match expected");
    }

    @Story("Invalid password produces correct error message")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Ensures that an invalid password triggers the expected error message.")
    @Test(groups = {"regression"})
    public void invalidPasswordShowsError() {

        LoginPage loginPage = navigateToLoginPage();
        String error = loginPage.loginExpectingFailure("student", "WrongPassword");

        AssertionHelper.assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible");
        AssertionHelper.assertEquals(error, "Your password is invalid!", "Error message should match expected");
    }

    @Story("Empty fields produce correct error message")
    @Severity(SeverityLevel.MINOR)
    @Owner("Adam Brouwer")
    @Description("Ensures that submitting empty login fields triggers the expected error message.")
    @Test(groups = {"regression"})
    public void emptyFieldsShowError() {

        LoginPage loginPage = navigateToLoginPage();
        loginPage.clearLoginForm();

        String error = loginPage.loginExpectingFailure("", "");

        AssertionHelper.assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible");
        AssertionHelper.assertEquals(error, "Your username is invalid!", "Error message should match expected");
    }

    // ============================================================
    // Enter-Key Behavior Test
    // ============================================================
    @Story("Submit login form using Enter key")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Ensures that pressing Enter in the password field does not trigger a successful login.")
    @Test(groups = {"regression"})
    public void submitWithEnterKeyTriggersLogin() {

        LoginPage loginPage = navigateToLoginPage();

        loginPage.enterUsername("student")
                .enterPassword("Password123")
                .submitWithEnterKey();

        AssertionHelper.assertTrue(
                loginPage.isLoginButtonDisplayed(),
                "Login button should still be visible because Enter key does not submit the form"
        );

        AssertionHelper.assertFalse(
                loginPage.isErrorMessageVisible(),
                "No error message should appear when pressing Enter"
        );
    }
}