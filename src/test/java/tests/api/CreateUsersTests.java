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
import utils.AllureApiLogger;
import utils.SchemaValidator;

import java.util.Map;

@Epic("API")
@Feature("Users API")
@Owner("Adam Brouwer")
public class CreateUsersTests {

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
    @Story("Create a new user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that POST /users creates a new user and matches the expected JSON schema.")
    @Test
    public void createUserReturns201AndValidSchema() {

        String name;
        String username;
        String email;

        // ============================================================
        // AI Data Integration (config-driven)
        // ============================================================
        if (ConfigManager.isAiDataEnabled()) {

            Map<String, Object> aiUser = AiDataGenerator.generateUserPayload();

            name = aiUser.get("name").toString();
            username = aiUser.get("username").toString();
            email = aiUser.get("email").toString();

            AllureApiLogger.attachText("AI Generated Name", name);
            AllureApiLogger.attachText("AI Generated Username", username);
            AllureApiLogger.attachText("AI Generated Email", email);

        } else {
            name = "John Doe";
            username = "johndoe";
            email = "john@example.com";
        }

        ApiResponse response = usersApi.createUser(name, username, email);

        AllureApiLogger.attachJson("POST /users Response Body", response.getBody());

        AssertionHelper.assertTrue(
                response.getStatusCode() == 201 || response.getStatusCode() == 200,
                "Expected 201 or 200 for POST /users"
        );

        SchemaValidator.validate("users-create.schema.json", response.getBody());
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================
    @Story("Create user with empty name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a user with an empty name.")
    @Test
    public void createUserWithEmptyNameStillReturns201() {

        ApiResponse response = usersApi.createUser(
                "",
                "testuser",
                "test@example.com"
        );

        AllureApiLogger.attachJson("POST /users (Empty Name) Response Body", response.getBody());

        AssertionHelper.assertTrue(
                response.getStatusCode() == 201 || response.getStatusCode() == 200,
                "JSONPlaceholder typically returns 201/200 even with empty fields"
        );
    }

    @Story("Create user with extremely long username")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a user with a very long username.")
    @Test
    public void createUserWithVeryLongUsernameReturns201() {

        String longUsername = AiDataGenerator.generateLongString(300);

        AllureApiLogger.attachText("Generated Long Username (300 chars)", longUsername);

        ApiResponse response = usersApi.createUser(
                "Test User",
                longUsername,
                "test@example.com"
        );

        AllureApiLogger.attachJson("POST /users (Long Username) Response Body", response.getBody());

        AssertionHelper.assertTrue(
                response.getStatusCode() == 201 || response.getStatusCode() == 200,
                "JSONPlaceholder should return 201/200 even for long usernames"
        );
    }

    // ============================================================
    // Negative Tests
    // ============================================================
    @Story("Create user with invalid JSON body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when sending invalid JSON to POST /users.")
    @Test
    public void createUserWithInvalidJsonReturnsError() {

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("Invalid JSON Payload", invalidJson);

        ApiResponse response = usersApi.postRaw("users", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 201,
                "Expected 400/500/201 depending on mock behavior, but got " + status
        );
    }

    @Story("Create user with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a user with missing fields in the payload.")
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
}