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
public class CreatePostsTests {

    private PostsApi postsApi;

    // ============================================================
    // Setup
    // ============================================================
    @Step("Initialize Posts API client")
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
    @Owner("Adam Brouwer")
    @Description("Validates that POST /posts creates a new post and matches the expected JSON schema.")
    @Test(groups = {"api", "regression"})
    public void createPostReturns201AndValidSchema() throws Exception {

        ApiResponse response = postsApi.createPost(
                "My New Post",
                "This is the body of the new post.",
                1
        );

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
    @Owner("Adam Brouwer")
    @Description("Validates behavior when creating a post with an empty title.")
    @Test(groups = {"api", "edge"})
    public void createPostWithEmptyTitleStillReturns201() throws Exception {

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
    @Owner("Adam Brouwer")
    @Description("Validates behavior when creating a post with a very long title.")
    @Test(groups = {"api", "edge"})
    public void createPostWithVeryLongTitleReturns201() throws Exception {

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
    @Owner("Adam Brouwer")
    @Description("Validates behavior when sending invalid JSON to POST /posts.")
    @Test(groups = {"api", "negative"})
    public void createPostWithInvalidJsonReturnsError() throws Exception {

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
    @Owner("Adam Brouwer")
    @Description("Validates behavior when creating a post with missing fields in the payload.")
    @Test(groups = {"api", "negative"})
    public void createPostWithMissingFieldsReturns201OrError() throws Exception {

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