package dataproviders;

import org.testng.annotations.DataProvider;

/**
 * Centralized DataProviders for API test cases.
 * Supports GET, POST, and negative test scenarios.
 */
public class ApiDataProviders {

    // ============================================================
    // GET ID Providers
    // ============================================================
    @DataProvider(name = "validUserIds")
    public Object[][] validUserIds() {
        return new Object[][]{
                {1}, {2}, {3}, {4}, {5}
        };
    }

    @DataProvider(name = "validPostIds")
    public Object[][] validPostIds() {
        return new Object[][]{
                {1}, {10}, {50}, {75}, {100}
        };
    }

    @DataProvider(name = "validCommentIds")
    public Object[][] validCommentIds() {
        return new Object[][]{
                {1}, {5}, {25}, {50}, {100}
        };
    }

    // ============================================================
    // POST Payload Providers
    // ============================================================
    @DataProvider(name = "createUserPayloads")
    public Object[][] createUserPayloads() {
        return new Object[][]{
                {"John Doe", "johnd", "john@example.com"},
                {"Jane Smith", "jsmith", "jane@example.com"},
                {"Adam Brouwer", "abrouwer", "adam@example.com"}
        };
    }

    @DataProvider(name = "createCommentPayloads")
    public Object[][] createCommentPayloads() {
        return new Object[][]{
                {1, "Adam", "adam@example.com", "This is a test comment"},
                {2, "John", "john@example.com", "Another comment"},
                {3, "Jane", "jane@example.com", "Yet another comment"}
        };
    }

    // ============================================================
    // Negative Test Providers
    // ============================================================
    @DataProvider(name = "invalidIds")
    public Object[][] invalidIds() {
        return new Object[][]{
                {-1},
                {0},
                {9999},
                {123456},
                {Integer.MAX_VALUE}
        };
    }

    @DataProvider(name = "invalidAuthPayloads")
    public Object[][] invalidAuthPayloads() {
        return new Object[][]{
                {"", ""},                     // empty username/password
                {"user", ""},                 // missing password
                {"", "password"},             // missing username
                {"invalid", "invalid"},       // wrong credentials
                {null, null}                  // null values
        };
    }

    @DataProvider(name = "invalidCommentPayloads")
    public Object[][] invalidCommentPayloads() {
        return new Object[][]{
                {null, "name", "email@example.com", "body"},   // null postId
                {1, "", "email@example.com", "body"},          // empty name
                {1, "name", "", "body"},                       // empty email
                {1, "name", "email@example.com", ""},          // empty body
                {1, null, null, null}                          // all null
        };
    }
}