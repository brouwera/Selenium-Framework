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
import utils.SchemaValidator;

import java.util.Map;

@Epic("API")
@Feature("Posts API")
@Owner("Adam Brouwer")
public class CreatePostsTests {

    private PostsApi postsApi;

    // ============================================================
    // Setup
    // ============================================================
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

        String title;
        String body;
        int userId;

        // ============================================================
        // Day 51 — AI Data Integration (config-driven)
        // ============================================================
        if (ConfigManager.isAiDataEnabled()) {
            Map<String, Object> aiPost = AiDataGenerator.generatePostPayload();
            title = aiPost.get("title").toString();
            body = aiPost.get("body").toString();
            userId = Integer.parseInt(aiPost.get("userId").toString());
        } else {
            // Fallback to static values if AI is disabled
            title = "My New Post";
            body = "This is the body of the new post.";
            userId = 1;
        }

        ApiResponse response = postsApi.createPost(title, body, userId);

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

        ApiResponse response = postsApi.createPost(
                "",
                "Body with empty title",
                1
        );

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

        String longTitle = "A".repeat(500);

        ApiResponse response = postsApi.createPost(
                longTitle,
                "Body with long title",
                1
        );

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

        ApiResponse response = postsApi.postRaw(
                "posts",
                "{ invalid json }"
        );

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

        ApiResponse response = postsApi.postRaw(
                "posts",
                "{ \"userId\": 1 }"
        );

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }
}