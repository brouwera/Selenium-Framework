package tests;

import base.BaseTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SuccessfulLoginPage;
import io.qameta.allure.Step;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners(listeners.TestListener.class)
public class LoginTest extends BaseTest {

    // ============================
    // Positive Tests
    // ============================
    @Test public void validLoginTest() {

        // Arrange
        LoginPage login = new LoginPage(driver, wait);
        login.open(baseUrl);

        // Act
        login.login("student", "Password123");
        SuccessfulLoginPage success = new SuccessfulLoginPage(driver, wait);

        // Assert
        assertEquals(success.getPageTitle(),
                "Logged In Successfully | Practice Test Automation",
                "Page title should match expected after login");

        assertTrue(success.getCurrentUrl().contains("/logged-in-successfully/"),
                "URL should contain the success page path");

        assertTrue(success.isSuccessMessageDisplayed(),
                "Success message should be visible");

        assertEquals(success.getSuccessMessageText(),
                "Logged In Successfully",
                "Success message text should match exactly");

        assertTrue(success.isLogoutButtonDisplayed(),
                "Logout button should be visible on success page");
    }

    @Test
    public void passwordFieldShouldMaskInputTest() {

        LoginPage login = new LoginPage(driver, wait);
        login.open(baseUrl);
        login.enterPassword("Password123");

        assertEquals(login.getPasswordFieldType(),
                "password",
                "Password field should mask input");
    }

    // ============================
    // Negative Tests
    // ============================
    @Test
    public void invalidUsernameTest() {

        LoginPage login = new LoginPage(driver, wait);
        login.open(baseUrl);
        login.login("WrongUsername", "Password123");

        assertTrue(login.isErrorMessageVisible(),
                "Error message should be displayed for invalid username");

        assertEquals(login.getErrorMessage(),
                "Your username is invalid!",
                "Error message text should match expected");

        assertTrue(driver.getCurrentUrl().contains("login"),
                "URL should remain on login page after invalid login");
    }

    @Test
    public void invalidPasswordTest() {

        LoginPage login = new LoginPage(driver, wait);
        login.open(baseUrl);
        login.login("student", "WrongPassword");

        assertEquals(login.getErrorMessage(),
                "Your password is invalid!");
    }

    @Test
    public void emptyFieldsTest() {

        LoginPage login = new LoginPage(driver, wait);
        login.open(baseUrl);
        login.login("", "");

        assertEquals(login.getErrorMessage(),
                "Your username is invalid!");
    }

    // ============================
    // UI Behavior Tests
    // ============================
    @Test
    public void errorMessageAppearsWhenFieldsAreEmptyTest() {

        LoginPage login = new LoginPage(driver, wait);
        login.open(baseUrl);
        login.clickLoginButton();

        assertEquals(login.getErrorMessage(),
                "Your username is invalid!");
    }
}