package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.UsersApi;
import config.ConfigManager;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiDataGenerator;
import utils.AiScenarioGenerator;
import utils.AllureApiLogger;
import utils.SchemaValidator;

import java.util.Map;

@Epic("API")
@Feature("Users API")
@Owner("Adam Brouwer")
public class UpdateUsersTests {

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
    // Positive Tests
    // ============================================================
    @Story("Update an existing user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that PUT /users/{id} updates a user using AI-generated data and matches the expected JSON schema.")
    @Test
    public void updateUserReturns200AndValidSchema() {

        AiScenarioGenerator.attachSuggestedScenarios("Users API — Update");

        String name;
        String username;
        String email;

        if (ConfigManager.isAiDataEnabled()) {

            Map<String, Object> aiUser = AiDataGenerator.generateUserPayload();

            name = aiUser.get("name").toString();
            username = aiUser.get("username").toString();
            email = aiUser.get("email").toString();

            AllureApiLogger.attachText("AI Generated Name", name);
            AllureApiLogger.attachText("AI Generated Username", username);
            AllureApiLogger.attachText("AI Generated Email", email);

        } else {
            name = "Updated User";
            username = "updateduser";
            email = "updated@example.com";
        }

        String payload = """
                {
                    "id": 1,
                    "name": "%s",
                    "username": "%s",
                    "email": "%s"
                }
                """.formatted(name, username, email);

        ApiResponse response = usersApi.putRaw("users/1", payload);

        AllureApiLogger.attachJson("PUT /users/1 Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "Status code for PUT /users/1 should be 200"
        );

        SchemaValidator.validate("users-update.schema.json", response.getBody());
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================
    @Story("Update user with empty name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a user with an empty name.")
    @Test
    public void updateUserWithEmptyNameReturns200() {

        AiScenarioGenerator.attachSuggestedScenarios("Users API — Update");

        String payload = """
                {
                    "id": 1,
                    "name": "",
                    "username": "testuser",
                    "email": "test@example.com"
                }
                """;

        ApiResponse response = usersApi.putRaw("users/1", payload);

        AllureApiLogger.attachJson("PUT /users/1 (Empty Name) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder typically returns 200 even with empty fields"
        );
    }

    @Story("Update user with extremely long username")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a user with a very long username.")
    @Test
    public void updateUserWithVeryLongUsernameReturns200() {

        AiScenarioGenerator.attachSuggestedScenarios("Users API — Update");

        String longUsername = AiDataGenerator.generateLongString(300);

        AllureApiLogger.attachText("Generated Long Username (300 chars)", longUsername);

        String payload = """
                {
                    "id": 1,
                    "name": "Test User",
                    "username": "%s",
                    "email": "test@example.com"
                }
                """.formatted(longUsername);

        ApiResponse response = usersApi.putRaw("users/1", payload);

        AllureApiLogger.attachJson("PUT /users/1 (Long Username) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder should return 200 even for long usernames"
        );
    }

    // ============================================================
    // Negative Tests
    // ============================================================
    @Story("Update user with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a user using an invalid ID.")
    @Test
    public void updateUserWithInvalidIdReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Users API — Update");

        String payload = """
                {
                    "name": "Test",
                    "username": "testuser",
                    "email": "test@example.com"
                }
                """;

        ApiResponse response = usersApi.putRaw("users/invalid-id", payload);

        AllureApiLogger.attachText("PUT /users/invalid-id Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Update user with invalid JSON body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when sending malformed JSON to PUT /users/{id}.")
    @Test
    public void updateUserWithInvalidJsonReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Users API — Update");

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = usersApi.putRaw("users/1", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Update user with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a user with missing fields in the payload.")
    @Test
    public void updateUserWithMissingFieldsReturns200OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Users API — Update");

        String payload = """
                {
                    "username": "OnlyUsernameProvided"
                }
                """;

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = usersApi.putRaw("users/1", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }
}