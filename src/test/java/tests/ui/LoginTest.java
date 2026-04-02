package tests.ui;

import base.BaseTest;
import config.ConfigManager;
import dataproviders.LoginDataProvider;
import dataproviders.JsonDataProvider;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.SuccessfulLoginPage;
import utils.AiDataGenerator;

import java.util.Map;

@Epic("Login")
@Feature("Login Flow")
@Owner("Adam Brouwer")
public class LoginTest extends BaseTest {

    // ============================================================
    // Navigation Helpers
    // ============================================================
    @Step("Navigate to Login Page")
    private LoginPage navigateToLoginPage() {
        return new HomePage(getDriver(), getWait())
                .open()
                .goToLoginPage()
                .waitForLoginPageReady();
    }

    // ============================================================
    // AI-Driven Negative Login Scenarios
    // ============================================================
    @Story("AI-generated invalid login scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates negative login behavior using AI-generated invalid usernames and passwords.")
    @Test(enabled = true)
    public void aiGeneratedNegativeLoginTest() {

        // Skip if AI data is disabled
        if (!ConfigManager.isAiDataEnabled()) {
            Allure.step("AI data generation disabled — skipping AI-driven negative login test.");
            return;
        }

        // Arrange
        LoginPage loginPage = navigateToLoginPage();

        // Generate AI-driven invalid login data
        Map<String, String> aiData = AiDataGenerator.generateInvalidLogin();

        String username = aiData.get("username");
        String password = aiData.get("password");
        String reason = aiData.get("reason");

        // Attach AI payload to Allure
        Allure.addAttachment("AI-Generated Invalid Login Data", aiData.toString());

        // Act
        String actualError = loginPage.loginExpectingFailure(username, password);

        // Assert
        AssertionHelper.assertTrue(
                loginPage.isErrorMessageVisible(),
                "Error message should be visible for AI-generated invalid login"
        );

        AssertionHelper.assertEquals(
                actualError,
                loginPage.getErrorMessage(),
                "Error message text should match expected for invalid login"
        );

        Allure.step("AI-generated invalid login validated successfully. Reason: " + reason);
    }

    // ============================================================
    // CSV-Driven Login Test
    // ============================================================
    @Story("Data-driven login validation (CSV)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates login behavior using CSV-driven test data for both positive and negative scenarios.")
    @Test(dataProvider = "loginDataCsv", dataProviderClass = LoginDataProvider.class)
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
                    "Invalid expectedResult value in test data: " + expectedResult
            );
        }
    }

    // ============================================================
    // JSON-Driven Login Test (Environment + Schema Validated)
    // ============================================================
    @Story("Data-driven login validation (JSON)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates login behavior using JSON-driven test data (environment-specific + schema validated).")
    @Test(dataProvider = "loginDataJson", dataProviderClass = JsonDataProvider.class)
    public void loginDataDrivenTestJson(Map<String, String> data) {
        loginDataDrivenTest(data);
    }

    // ============================================================
    // Positive Flow Tests
    // ============================================================
    @Story("User can log in through navigation flow")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that a user can navigate through the site and successfully log in.")
    @Test
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
    @Description("Validates that a logged-in user can log out and return to the Login page.")
    @Test
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
    @Description("Ensures that an invalid username triggers the expected error message.")
    @Test
    public void invalidUsernameShowsError() {

        LoginPage loginPage = navigateToLoginPage();
        String error = loginPage.loginExpectingFailure("wrongUser", "Password123");

        AssertionHelper.assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible");
        AssertionHelper.assertEquals(error, "Your username is invalid!", "Error message should match expected");
    }

    @Story("Invalid password produces correct error message")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that an invalid password triggers the expected error message.")
    @Test
    public void invalidPasswordShowsError() {

        LoginPage loginPage = navigateToLoginPage();
        String error = loginPage.loginExpectingFailure("student", "WrongPassword");

        AssertionHelper.assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible");
        AssertionHelper.assertEquals(error, "Your password is invalid!", "Error message should match expected");
    }

    @Story("Empty fields produce correct error message")
    @Severity(SeverityLevel.MINOR)
    @Description("Ensures that submitting empty login fields triggers the expected error message.")
    @Test
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
    @Description("Ensures that pressing Enter in the password field does not trigger a successful login.")
    @Test
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