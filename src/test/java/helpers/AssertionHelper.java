package helpers;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AssertionHelper {

    // ============================================================
    // Basic Assertions
    // ============================================================

    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertNotNull(actual, message + " | Actual value is null");
        Assert.assertNotNull(expected, message + " | Expected value is null");
        Assert.assertEquals(actual.trim(), expected.trim(), message);
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

    // ============================================================
    // UI-Specific Assertions
    // ============================================================

    public static void assertElementDisplayed(WebElement element, String message) {
        Assert.assertNotNull(element, message + " | Element reference is null");

        try {
            Assert.assertTrue(
                    element.isDisplayed(),
                    message + " | Element is NOT displayed"
            );
        } catch (StaleElementReferenceException e) {
            Assert.fail(message + " | Element became stale while checking display state");
        }
    }

    public static void assertTextContains(String actual, String expectedSubstring, String message) {
        Assert.assertNotNull(actual, message + " | Actual text is null");
        Assert.assertNotNull(expectedSubstring, message + " | Expected substring is null");

        Assert.assertTrue(
                actual.contains(expectedSubstring),
                message + " | Expected substring: '" + expectedSubstring + "' | Actual: '" + actual + "'"
        );
    }

    public static void assertTextMatches(String actual, String expected, String message) {
        Assert.assertNotNull(actual, message + " | Actual text is null");
        Assert.assertNotNull(expected, message + " | Expected text is null");

        Assert.assertEquals(
                actual.trim(),
                expected.trim(),
                message + " | Expected: '" + expected + "' | Actual: '" + actual + "'"
        );
    }
}