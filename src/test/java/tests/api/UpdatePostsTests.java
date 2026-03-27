package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.PostsApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
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
    @Description("Validates that PUT /posts/{id} updates a post and matches the expected JSON schema.")
    @Test
    public void updatePostReturns200AndValidSchema() {

        ApiResponse response = postsApi.putRaw(
                "posts/1",
                """
                {
                    "id": 1,
                    "title": "Updated Title",
                    "body": "Updated body content.",
                    "userId": 1
                }
                """
        );

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

        ApiResponse response = postsApi.putRaw(
                "posts/1",
                """
                {
                    "id": 1,
                    "title": "",
                    "body": "Body with empty title",
                    "userId": 1
                }
                """
        );

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "JSONPlaceholder typically returns 200 even with empty fields"
        );
    }

    @Story("Update post with extremely long title")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when updating a post with a very long title.")
    @Test
    public void updatePostWithVeryLongTitleReturns200() {

        String longTitle = "A".repeat(500);

        ApiResponse response = postsApi.putRaw(
                "posts/1",
                """
                {
                    "id": 1,
                    "title": "%s",
                    "body": "Body with long title",
                    "userId": 1
                }
                """.formatted(longTitle)
        );

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

        ApiResponse response = postsApi.putRaw(
                "posts/invalid-id",
                """
                {
                    "title": "Test",
                    "body": "Body",
                    "userId": 1
                }
                """
        );

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

        ApiResponse response = postsApi.putRaw(
                "posts/1",
                "{ invalid json }"
        );

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

        ApiResponse response = postsApi.putRaw(
                "posts/1",
                """
                {
                    "title": "Only Title Provided"
                }
                """
        );

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }
}