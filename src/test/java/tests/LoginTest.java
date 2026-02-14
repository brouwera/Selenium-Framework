package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.HomePage;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class LoginTest extends BaseTest {

    // ============================
    // Positive Tests
    // ============================

    // Validates that login is successful when entering correct username and password
    @Test
    public void validLoginTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("student", "Password123");

        HomePage home = new HomePage(driver, wait);
        assertTrue(home.isSuccessMessageDisplayed());
        assertTrue(home.isLogoutButtonDisplayed());
    }

    // Validates that password is masked when entered into the password field
    @Test
    public void passwordFieldShouldMaskInputTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.enterPassword("Password123");

        assertEquals(login.getPasswordFieldType(), "password");
    }

    // ============================
    // Negative Tests
    // ============================

    // Validates that username error displays when incorrect username is entered
    @Test
    public void invalidUsernameTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("WrongUsername", "Password123");

        assertEquals(login.getErrorMessage(), "Your username is invalid!");
    }

    // Validates that password error displays when incorrect password is entered
    @Test public void invalidPasswordTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("student", "WrongPassword");

        assertEquals(login.getErrorMessage(), "Your password is invalid!");
    }

    // Validates that username error displays when username and password fields are blank
    @Test
    public void emptyFieldsTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("", "");

        assertEquals(login.getErrorMessage(), "Your username is invalid!");
    }

    // ============================
    // UI Behavior Tests
    // ============================

    // Validates that username error displays in UI when clicking Login with empty fields
    @Test
    public void errorMessageAppearsWhenFieldsAreEmptyTest() {
        LoginPage login = new LoginPage(driver, wait);
        login.clickLogin();

        assertEquals(login.getErrorMessage(), "Your username is invalid!");
    }
}