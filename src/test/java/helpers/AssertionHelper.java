package helpers;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AssertionHelper {

    // ============================
    // Basic Assertions
    // ============================

    public static void verifyEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void verifyTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void verifyFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

    public static void verifyNotNull(Object obj, String message) {
        Assert.assertNotNull(obj, message);
    }

    // ============================
    // UI-Specific Assertions
    // ============================

    public static void verifyElementDisplayed(WebElement element, String message) {
        Assert.assertTrue(element.isDisplayed(), message);
    }

    public static void verifyTextContains(String actual, String expectedSubstring, String message) {
        Assert.assertTrue(actual.contains(expectedSubstring), message);
    }

    public static void verifyTextMatches(String actual, String expected, String message) {
        Assert.assertEquals(actual.trim(), expected.trim(), message);
    }
}