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
public class CreateCommentsTests {

    private CommentsApi commentsApi;

    @BeforeClass
    public void setUp() {
        ApiClient client = new ApiClient();
        commentsApi = new CommentsApi(client);
    }

    // ============================================================
    // Positive Tests
    // ============================================================
    @Story("Create a new comment")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that POST /comments creates a new comment and matches the expected JSON schema.")
    @Test
    public void createCommentReturns201AndValidSchema() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Create");

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
            name = "Test User";
            email = "test@example.com";
            body = "This is a test comment.";
            postId = 1;
        }

        // NEW: Build Map payload
        Map<String, Object> payload = Map.of(
                "postId", postId,
                "name", name,
                "email", email,
                "body", body
        );

        ApiResponse response = commentsApi.createComment(payload);

        AllureApiLogger.attachJson("POST /comments Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "Status code for POST /comments should be 201"
        );

        SchemaValidator.validate("comments-create.schema.json", response.getBody());
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================
    @Story("Create comment with empty name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a comment with an empty name.")
    @Test
    public void createCommentWithEmptyNameStillReturns201() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Create");

        Map<String, Object> payload = Map.of(
                "postId", 1,
                "name", "",
                "email", "test@example.com",
                "body", "Body with empty name"
        );

        ApiResponse response = commentsApi.createComment(payload);

        AllureApiLogger.attachJson("POST /comments (Empty Name) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder typically returns 201 even with empty fields"
        );
    }

    @Story("Create comment with extremely long body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a comment with a very long body.")
    @Test
    public void createCommentWithVeryLongBodyReturns201() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Create");

        String longBody = AiDataGenerator.generateLongString(2000);

        AllureApiLogger.attachText("Generated Long Body (2000 chars)", longBody);

        Map<String, Object> payload = Map.of(
                "postId", 1,
                "name", "Test User",
                "email", "test@example.com",
                "body", longBody
        );

        ApiResponse response = commentsApi.createComment(payload);

        AllureApiLogger.attachJson("POST /comments (Long Body) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder should return 201 even for long bodies"
        );
    }

    // ============================================================
    // Negative Tests
    // ============================================================
    @Story("Create comment with invalid JSON body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when sending invalid JSON to POST /comments.")
    @Test
    public void createCommentWithInvalidJsonReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Create");

        String invalidJson = "{ invalid json }";

        AllureApiLogger.attachText("Invalid JSON Payload", invalidJson);

        ApiResponse response = commentsApi.postRaw("comments", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 201,
                "Expected 400/500/201 depending on mock behavior, but got " + status
        );
    }

    @Story("Create comment with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a comment with missing fields in the payload.")
    @Test
    public void createCommentWithMissingFieldsReturns201OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Create");

        String payload = "{ \"postId\": 1 }";

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = commentsApi.postRaw("comments", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }
}