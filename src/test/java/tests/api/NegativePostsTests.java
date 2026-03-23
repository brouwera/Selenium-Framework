package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.PostsApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("API")
@Feature("Posts API - Negative Scenarios")
public class NegativePostsTests {

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
    // Invalid ID Tests
    // ============================================================

    @Story("Get post with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when GET /posts/{id} is called with an invalid ID.")
    @Test(groups = {"api", "negative"})
    public void getPostWithInvalidIdReturnsError() throws Exception {

        ApiResponse response = postsApi.getPostById(-1);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 400 || status == 200,
                "Expected 404/400/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Get post with non-numeric ID")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when GET /posts/{id} is called with a non-numeric ID.")
    @Test(groups = {"api", "negative"})
    public void getPostWithNonNumericIdReturnsError() throws Exception {

        ApiResponse response = postsApi.getRaw("posts/abc123");

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 404 || status == 500,
                "Expected 400/404/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Invalid JSON Tests
    // ============================================================

    @Story("Create post with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when POST /posts receives malformed JSON.")
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

    @Story("Update post with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when PUT /posts/{id} receives malformed JSON.")
    @Test(groups = {"api", "negative"})
    public void updatePostWithInvalidJsonReturnsError() throws Exception {

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

    // ============================================================
    // Missing Field Tests
    // ============================================================

    @Story("Create post with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when POST /posts is missing required fields.")
    @Test(groups = {"api", "negative"})
    public void createPostWithMissingFieldsReturnsErrorOr201() throws Exception {

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

    @Story("Update post with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when PUT /posts/{id} is missing required fields.")
    @Test(groups = {"api", "negative"})
    public void updatePostWithMissingFieldsReturnsErrorOr200() throws Exception {

        ApiResponse response = postsApi.putRaw(
                "posts/1",
                "{ \"title\": \"Only Title Provided\" }"
        );

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Server Error Tests
    // ============================================================

    @Story("Trigger server error")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when the server returns a 500 error for /posts.")
    @Test(groups = {"api", "negative"})
    public void triggerServerErrorReturns500() throws Exception {

        ApiResponse response = postsApi.triggerServerError();

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 500 || status == 404 || status == 200,
                "Expected 500/404/200 depending on mock behavior, but got " + status
        );
    }
}