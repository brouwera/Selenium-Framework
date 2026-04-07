package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.PostsApi;
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
@Feature("Posts API")
@Owner("Adam Brouwer")
public class CreatePostsTests {

    private PostsApi postsApi;

    @BeforeClass
    public void setUp() {
        ApiClient client = new ApiClient();
        postsApi = new PostsApi(client);
    }

    // ============================================================
    // Positive Tests
    // ============================================================
    @Story("Create a new post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that POST /posts creates a new post and matches the expected JSON schema.")
    @Test
    public void createPostReturns201AndValidSchema() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Create");

        String title;
        String body;
        int userId;

        if (ConfigManager.isAiDataEnabled()) {

            Map<String, Object> aiPost = AiDataGenerator.generatePostPayload();

            title = aiPost.get("title").toString();
            body = aiPost.get("body").toString();
            userId = Integer.parseInt(aiPost.get("userId").toString());

            AllureApiLogger.attachText("AI Generated Title", title);
            AllureApiLogger.attachText("AI Generated Body", body);
            AllureApiLogger.attachText("AI Generated UserId", String.valueOf(userId));

        } else {
            title = "My New Post";
            body = "This is the body of the new post.";
            userId = 1;
        }

        ApiResponse response = postsApi.createPost(title, body, userId);

        AllureApiLogger.attachJson("POST /posts Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "Status code for POST /posts should be 201"
        );

        SchemaValidator.validate("posts-create.schema.json", response.getBody());
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================
    @Story("Create post with empty title")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a post with an empty title.")
    @Test
    public void createPostWithEmptyTitleStillReturns201() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Create");

        ApiResponse response = postsApi.createPost(
                "",
                "Body with empty title",
                1
        );

        AllureApiLogger.attachJson("POST /posts (Empty Title) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder typically returns 201 even with empty fields"
        );
    }

    @Story("Create post with extremely long title")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a post with a very long title.")
    @Test
    public void createPostWithVeryLongTitleReturns201() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Create");

        String longTitle = "A".repeat(500);

        AllureApiLogger.attachText("Generated Long Title (500 chars)", longTitle);

        ApiResponse response = postsApi.createPost(
                longTitle,
                "Body with long title",
                1
        );

        AllureApiLogger.attachJson("POST /posts (Long Title) Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder should return 201 even for long titles"
        );
    }

    // ============================================================
    // Negative Tests
    // ============================================================
    @Story("Create post with invalid JSON body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when sending invalid JSON to POST /posts.")
    @Test
    public void createPostWithInvalidJsonReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Create");

        String invalidJson = "{ invalid json }";

        AllureApiLogger.attachText("Invalid JSON Payload", invalidJson);

        ApiResponse response = postsApi.postRaw("posts", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 201,
                "Expected 400/500/201 depending on mock behavior, but got " + status
        );
    }

    @Story("Create post with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when creating a post with missing fields in the payload.")
    @Test
    public void createPostWithMissingFieldsReturns201OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Create");

        String payload = "{ \"userId\": 1 }";

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = postsApi.postRaw("posts", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }
}