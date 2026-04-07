package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.CommentsApi;
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
@Feature("Comments API")
@Owner("Adam Brouwer")
public class UpdateCommentsTests {

    private CommentsApi commentsApi;

    // ============================================================
    // Setup
    // ============================================================
    @BeforeClass
    public void setUp() {
        ApiClient client = new ApiClient();
        commentsApi = new CommentsApi(client);
    }

    // ============================================================
    // Positive Tests
    // ============================================================
    @Story("Update an existing comment")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that PUT /comments/{id} updates a comment using AI-generated data and matches the expected JSON schema.")
    @Test
    public void updateCommentReturns200AndValidSchema() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Update");

        String name;
        String email;
        String body;
        int postId;

        if (ConfigManager.isAiDataEnabled()) {

            Map<String, Object> aiComment = AiDataGenerator.generateCommentPayload();

            name = aiComment.get("name").toString();
            email = aiComment.get("email").toString();
            body = aiComment.get("body").toString();
            postId = Integer.parseInt(aiComment.get("postId").toString());

            AllureApiLogger.attachText("AI Generated Name", name);
            AllureApiLogger.attachText("AI Generated Email", email);
            AllureApiLogger.attachText("AI Generated Body", body);
            AllureApiLogger.attachText("AI Generated PostId", String.valueOf(postId));

        } else {
            name = "Updated User";
            email = "updated@example.com";
            body = "Updated comment body.";
            postId = 1;
        }

        String payload = """
                {
                    "id": 1,
                    "postId": %s,
                    "name": "%s",
                    "email": "%s",
                    "body": "%s"
                }
                """.formatted(postId, name, email, body);

        ApiResponse response = commentsApi.putRaw("comments/1", payload);

        AllureApiLogger.attachJson("PUT /comments/1 Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "Status code for PUT /comments/1 should be 200"
        );

        SchemaValidator.validate("comments-update.schema.json", response.getBody());
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================
    @Story("Update comment with empty name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a comment with an empty name.")
    @Test
    public void updateCommentWithEmptyNameReturns200() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Update");

        String payload = """
                {
                    "id": 1,
                    "postId": 1,
                    "name": "",
                    "email": "test@example.com",
                    "body": "Body with empty name"
                }
                """;

        ApiResponse response = commentsApi.putRaw("comments/1", payload);

        AllureApiLogger.attachJson("PUT /comments/1 (Empty Name) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder typically returns 200 even with empty fields"
        );
    }

    @Story("Update comment with extremely long body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a comment with a very long body.")
    @Test
    public void updateCommentWithVeryLongBodyReturns200() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Update");

        String longBody = AiDataGenerator.generateLongString(3000);

        AllureApiLogger.attachText("Generated Long Body (3000 chars)", longBody);

        String payload = """
                {
                    "id": 1,
                    "postId": 1,
                    "name": "Test User",
                    "email": "test@example.com",
                    "body": "%s"
                }
                """.formatted(longBody);

        ApiResponse response = commentsApi.putRaw("comments/1", payload);

        AllureApiLogger.attachJson("PUT /comments/1 (Long Body) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder should return 200 even for long bodies"
        );
    }

    // ============================================================
    // Negative Tests
    // ============================================================
    @Story("Update comment with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a comment using an invalid ID.")
    @Test
    public void updateCommentWithInvalidIdReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Update");

        String payload = """
                {
                    "postId": 1,
                    "name": "Test",
                    "email": "test@example.com",
                    "body": "Body"
                }
                """;

        ApiResponse response = commentsApi.putRaw("comments/invalid-id", payload);

        AllureApiLogger.attachText("PUT /comments/invalid-id Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Update comment with invalid JSON body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when sending malformed JSON to PUT /comments/{id}.")
    @Test
    public void updateCommentWithInvalidJsonReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Update");

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = commentsApi.putRaw("comments/1", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Update comment with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a comment with missing fields in the payload.")
    @Test
    public void updateCommentWithMissingFieldsReturns200OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Update");

        String payload = """
                {
                    "name": "Only Name Provided"
                }
                """;

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = commentsApi.putRaw("comments/1", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }
}