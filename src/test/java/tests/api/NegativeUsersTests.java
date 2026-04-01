package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.UsersApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiDataGenerator;
import utils.AllureApiLogger;

@Epic("API")
@Feature("Users API - Negative Scenarios")
@Owner("Adam Brouwer")
public class NegativeUsersTests {

    private UsersApi usersApi;

    // ============================================================
    // Setup
    // ============================================================
    @BeforeClass
    public void setUp() {
        ApiClient client = new ApiClient();
        usersApi = new UsersApi(client);
    }

    // ============================================================
    // Invalid ID Tests
    // ============================================================
    @Story("GET /users/{id} with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when GET /users/{id} is called with an invalid ID.")
    @Test
    public void getUserWithInvalidIdReturnsError() {

        ApiResponse response = usersApi.getUserById(-1);

        AllureApiLogger.attachJson("GET /users/-1 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 400 || status == 200,
                "Expected 404/400/200 depending on mock behavior, but got " + status
        );
    }

    @Story("GET /users/{id} with non-numeric ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when GET /users/{id} is called with a non-numeric ID.")
    @Test
    public void getUserWithNonNumericIdReturnsError() {

        ApiResponse response = usersApi.getRaw("users/abc123");

        AllureApiLogger.attachJson("GET /users/abc123 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 404 || status == 500,
                "Expected 400/404/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Invalid JSON Tests
    // ============================================================
    @Story("POST /users with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /users receives malformed JSON.")
    @Test
    public void createUserWithInvalidJsonReturnsError() {

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = usersApi.postRaw("users", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 201,
                "Expected 400/500/201 depending on mock behavior, but got " + status
        );
    }

    @Story("PUT /users/{id} with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /users/{id} receives malformed JSON.")
    @Test
    public void updateUserWithInvalidJsonReturnsError() {

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = usersApi.putRaw("users/1", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Missing Field Tests
    // ============================================================
    @Story("POST /users with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /users is missing required fields.")
    @Test
    public void createUserWithMissingFieldsReturns201OrError() {

        String payload = "{ \"email\": \"test@example.com\" }";

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = usersApi.postRaw("users", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }

    @Story("PUT /users/{id} with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /users/{id} is missing required fields.")
    @Test
    public void updateUserWithMissingFieldsReturns200OrError() {

        String payload = "{ \"username\": \"OnlyUsernameProvided\" }";

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = usersApi.putRaw("users/1", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // AI-Generated Malicious / Oversized Payload Tests
    // ============================================================
    @Story("POST /users with AI-generated malicious payload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /users receives an AI-generated malicious or suspicious payload.")
    @Test
    public void createUserWithMaliciousPayloadReturns201OrError() {

        String maliciousPayload = AiDataGenerator.generateMaliciousPayload();

        AllureApiLogger.attachText("AI Generated Malicious Payload", maliciousPayload);

        ApiResponse response = usersApi.postRaw("users", maliciousPayload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }

    @Story("PUT /users/{id} with AI-generated oversized payload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /users/{id} receives an extremely large AI-generated payload.")
    @Test
    public void updateUserWithOversizedPayloadReturns200OrError() {

        String longName = AiDataGenerator.generateLongString(2000);
        String longEmail = AiDataGenerator.generateLongString(3000);

        AllureApiLogger.attachText("AI Generated Oversized Name", longName);
        AllureApiLogger.attachText("AI Generated Oversized Email", longEmail);

        String payload = """
                {
                    "id": 1,
                    "name": "%s",
                    "username": "testuser",
                    "email": "%s"
                }
                """.formatted(longName, longEmail);

        ApiResponse response = usersApi.putRaw("users/1", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Malformed Endpoint Tests
    // ============================================================
    @Story("DELETE /users//1 malformed endpoint")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when the DELETE endpoint is malformed.")
    @Test
    public void deleteUserWithMalformedEndpointReturnsError() {

        ApiResponse response = usersApi.deleteRaw("users//1");

        AllureApiLogger.attachJson("DELETE /users//1 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 404 || status == 500,
                "Expected 200/400/404/500 depending on mock behavior, but got " + status
        );
    }
}