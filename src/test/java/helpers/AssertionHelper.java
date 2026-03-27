package helpers;

import api.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.TestDataManager;

public class AssertionHelper {

    // ============================================================
    // Basic Assertions — String
    // ============================================================
    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertNotNull(actual, message + " | Actual value is null");
        Assert.assertNotNull(expected, message + " | Expected value is null");
        Assert.assertEquals(actual.trim(), expected.trim(), message);
    }

    // ============================================================
    // Basic Assertions — Primitive Overloads (API Support)
    // ============================================================
    public static void assertEquals(int actual, int expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertEquals(long actual, long expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertEquals(double actual, double expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertEquals(boolean actual, boolean expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    // ============================================================
    // Boolean Assertions
    // ============================================================
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

    // ============================================================
    // API-Specific Assertions
    // ============================================================
    public static void assertStatusCode(ApiResponse response, int expectedStatus) {
        Assert.assertNotNull(response, "API response is null");
        Assert.assertEquals(
                response.getStatusCode(),
                expectedStatus,
                "Unexpected status code"
        );
    }

    public static void assertHeaderExists(ApiResponse response, String headerName) {
        Assert.assertNotNull(response, "API response is null");

        String value = response.getHeader(headerName);

        Assert.assertNotNull(
                value,
                "Expected header '" + headerName + "' was not found in the response"
        );
    }

    public static void assertHeader(ApiResponse response, String headerName, String expectedValue) {
        Assert.assertNotNull(response, "API response is null");

        String actual = response.getHeader(headerName);

        Assert.assertNotNull(
                actual,
                "Header '" + headerName + "' not found in response"
        );

        Assert.assertEquals(
                actual,
                expectedValue,
                "Header '" + headerName + "' does not match expected value"
        );
    }

    public static void assertContentTypeJson(ApiResponse response) {
        Assert.assertNotNull(response, "API response is null");

        String contentType = response.getHeader("Content-Type");

        Assert.assertNotNull(contentType, "Content-Type header is missing");

        Assert.assertTrue(
                contentType.contains("application/json"),
                "Expected Content-Type to be JSON but got: " + contentType
        );
    }

    // ⭐ NEW: Schema Validation Assertion
    public static void assertSchemaValid(String schemaFile, ApiResponse response) {
        Assert.assertNotNull(response, "API response is null");

        try {
            utils.SchemaValidator.validate(schemaFile, response.getBody());
        } catch (Exception ex) {
            Assert.fail("Schema validation failed for file: " + schemaFile + "\n" + ex.getMessage());
        }
    }

    // ============================================================
    // JSON Assertions
    // ============================================================
    public static void assertJsonContainsKey(JSONObject json, String key) {
        Assert.assertNotNull(json, "JSON object is null");
        Assert.assertTrue(
                json.has(key),
                "JSON object does not contain expected key: " + key
        );
    }

    public static void assertJsonFieldNotNull(JSONObject json, String key) {
        assertJsonContainsKey(json, key);
        Assert.assertNotNull(json.get(key), "JSON field '" + key + "' is null");
    }

    public static void assertJsonFieldEquals(JSONObject json, String key, Object expected) {
        assertJsonContainsKey(json, key);

        Object actual = json.get(key);
        Assert.assertEquals(
                actual,
                expected,
                "JSON field '" + key + "' does not match expected value"
        );
    }

    public static void assertJsonArraySize(JSONArray array, int expectedSize) {
        Assert.assertNotNull(array, "JSON array is null");
        Assert.assertEquals(
                array.length(),
                expectedSize,
                "JSON array size does not match expected value"
        );
    }

    public static void assertJsonArrayContains(JSONArray array, JSONObject expectedObject) {
        Assert.assertNotNull(array, "JSON array is null");
        Assert.assertNotNull(expectedObject, "Expected JSON object is null");

        boolean found = false;

        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);

            boolean match = true;
            for (String key : expectedObject.keySet()) {
                if (!item.has(key) || !item.get(key).equals(expectedObject.get(key))) {
                    match = false;
                    break;
                }
            }

            if (match) {
                found = true;
                break;
            }
        }

        Assert.assertTrue(
                found,
                "JSON array does not contain expected object: " + expectedObject
        );
    }

    public static void assertJsonMatches(ApiResponse response, String expectedJsonFile) {
        Assert.assertNotNull(response, "API response is null");

        JSONObject expected = new JSONObject(
                TestDataManager.loadJsonObject(expectedJsonFile).toString()
        );

        JSONObject actual = new JSONObject(response.getBody());

        Assert.assertEquals(
                actual.toString(),
                expected.toString(),
                "JSON response does not match expected file: " + expectedJsonFile
        );
    }

    // ⭐ NEW: JSON Helper
    public static JSONObject getJson(ApiResponse response) {
        Assert.assertNotNull(response, "API response is null");
        Assert.assertNotNull(response.getBody(), "API response body is null");

        return new JSONObject(response.getBody());
    }

    // ============================================================
    // Performance Assertions
    // ============================================================
    public static void assertResponseTime(long actualMs, long maxAllowedMs, String message) {
        Assert.assertTrue(
                actualMs <= maxAllowedMs,
                message + " | Expected <= " + maxAllowedMs + "ms but got " + actualMs + "ms"
        );
    }

    // ============================================================
    // Exception Assertions (⭐ NEW ⭐)
    // ============================================================
    public static void assertThrows(Class<? extends Throwable> expected, Runnable action) {
        try {
            action.run();
            Assert.fail("Expected exception: " + expected.getName() + " but no exception was thrown");
        } catch (Throwable actual) {
            if (!expected.isAssignableFrom(actual.getClass())) {
                Assert.fail("Expected exception: " + expected.getName()
                        + " but got: " + actual.getClass().getName());
            }
        }
    }
}