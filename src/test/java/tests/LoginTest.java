package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.HomePage;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    // ============================
    // Positive Tests
    // ============================
    @Test
    public void validLoginTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("student", "Password123");
        HomePage home = new HomePage(driver, wait);

        assertTrue(home.isSuccessMessageDisplayed(), "Success message should be visible");
        assertTrue(home.isLogoutButtonDisplayed(), "Logout button should be visible");
    }

    @Test
    public void passwordFieldShouldMaskInputTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.enterPassword("Password123");

        assertEquals(login.getPasswordFieldType(), "password", "Password field should mask input");
    }

    // ============================
    // Negative Tests
    // ============================
    @Test
    public void invalidUsernameTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("WrongUsername", "Password123");

        assertEquals(login.getErrorMessage(), "Your username is invalid!");
    }

    @Test
    public void invalidPasswordTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("student", "WrongPassword");

        assertEquals(login.getErrorMessage(), "Your password is invalid!");
    }

    @Test
    public void emptyFieldsTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("", "");

        assertEquals(login.getErrorMessage(), "Your username is invalid!");
    }

    // ============================
    // UI Behavior Tests
    // ============================
    @Test public void errorMessageAppearsWhenFieldsAreEmptyTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.clickLoginButton();

        assertEquals(login.getErrorMessage(), "Your username is invalid!");
    }
}