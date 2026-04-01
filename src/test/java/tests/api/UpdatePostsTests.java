package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.PostsApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiDataGenerator;
import utils.AllureApiLogger;
import utils.SchemaValidator;

@Epic("API")
@Feature("Posts API")
@Owner("Adam Brouwer")
public class UpdatePostsTests {

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
    @Story("Update an existing post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that PUT /posts/{id} updates a post using AI-generated data and matches the expected JSON schema.")
    @Test
    public void updatePostReturns200AndValidSchema() {

        String aiTitle = AiDataGenerator.generatePostTitle();
        String aiBody = AiDataGenerator.generatePostBody();

        AllureApiLogger.attachText("AI Generated Title", aiTitle);
        AllureApiLogger.attachText("AI Generated Body", aiBody);

        String payload = """
                {
                    "id": 1,
                    "title": "%s",
                    "body": "%s",
                    "userId": 1
                }
                """.formatted(aiTitle, aiBody);

        ApiResponse response = postsApi.putRaw("posts/1", payload);

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "Status code for PUT /posts/1 should be 200"
        );

        SchemaValidator.validate("posts-update.schema.json", response.getBody());
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================
    @Story("Update post with empty title")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a post with an empty title.")
    @Test
    public void updatePostWithEmptyTitleReturns200() {

        String payload = """
                {
                    "id": 1,
                    "title": "",
                    "body": "Body with empty title",
                    "userId": 1
                }
                """;

        ApiResponse response = postsApi.putRaw("posts/1", payload);

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder typically returns 200 even with empty fields"
        );
    }

    @Story("Update post with extremely long title")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a post with a very long AI-generated title.")
    @Test
    public void updatePostWithVeryLongTitleReturns200() {

        String longTitle = AiDataGenerator.generateLongString(500);

        AllureApiLogger.attachText("AI Generated Long Title", longTitle);

        String payload = """
                {
                    "id": 1,
                    "title": "%s",
                    "body": "Body with long title",
                    "userId": 1
                }
                """.formatted(longTitle);

        ApiResponse response = postsApi.putRaw("posts/1", payload);

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder should return 200 even for long titles"
        );
    }

    // ============================================================
    // Negative Tests
    // ============================================================
    @Story("Update post with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a post using an invalid ID.")
    @Test
    public void updatePostWithInvalidIdReturnsError() {

        String aiTitle = AiDataGenerator.generatePostTitle();

        AllureApiLogger.attachText("AI Generated Title", aiTitle);

        String payload = """
                {
                    "title": "%s",
                    "body": "Body",
                    "userId": 1
                }
                """.formatted(aiTitle);

        ApiResponse response = postsApi.putRaw("posts/invalid-id", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Update post with invalid JSON body")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when sending malformed JSON to PUT /posts/{id}.")
    @Test
    public void updatePostWithInvalidJsonReturnsError() {

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = postsApi.putRaw("posts/1", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Update post with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a post with missing fields in the payload.")
    @Test
    public void updatePostWithMissingFieldsReturns200OrError() {

        String payload = """
                {
                    "title": "Only Title Provided"
                }
                """;

        ApiResponse response = postsApi.putRaw("posts/1", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }
}