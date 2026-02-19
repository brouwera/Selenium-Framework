package helpers;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AssertionHelper {

    // ============================
    // Basic Assertions
    // ============================

    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

    public static void assertNotNull(Object obj, String message) {
        Assert.assertNotNull(obj, message);
    }

    // ============================
    // UI-Specific Assertions
    // ============================

    public static void assertElementDisplayed(WebElement element, String message) {
        Assert.assertTrue(element.isDisplayed(), message);
    }

    public static void assertTextContains(String actual, String expectedSubstring, String message) {
        Assert.assertTrue(actual.contains(expectedSubstring), message);
    }

    public static void assertTextMatches(String actual, String expected, String message) {
        Assert.assertEquals(actual.trim(), expected.trim(), message);
    }
}