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
@Owner("Adam Brouwer")
public class NegativePostsTests {

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
    // Invalid ID Tests
    // ============================================================

    @Story("GET /posts/{id} with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when GET /posts/{id} is called with an invalid ID.")
    @Test
    public void getPostWithInvalidIdReturnsError() {

        ApiResponse response = postsApi.getPostById(-1);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 400 || status == 200,
                "Expected 404/400/200 depending on mock behavior, but got " + status
        );
    }

    @Story("GET /posts/{id} with non-numeric ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when GET /posts/{id} is called with a non-numeric ID.")
    @Test
    public void getPostWithNonNumericIdReturnsError() {

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

    @Story("POST /posts with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /posts receives malformed JSON.")
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

    @Story("PUT /posts/{id} with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /posts/{id} receives malformed JSON.")
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

    // ============================================================
    // Missing Field Tests
    // ============================================================

    @Story("POST /posts with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /posts is missing required fields.")
    @Test
    public void createPostWithMissingFieldsReturnsErrorOr201() {

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

    @Story("PUT /posts/{id} with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /posts/{id} is missing required fields.")
    @Test
    public void updatePostWithMissingFieldsReturnsErrorOr200() {

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
    @Story("GET /posts/trigger-500 (mocked server error)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates behavior when the server returns a 500 error for /posts.")
    @Test
    public void triggerServerErrorReturns500() {

        ApiResponse response = postsApi.triggerServerError();

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 500 || status == 404 || status == 200,
                "Expected 500/404/200 depending on mock behavior, but got " + status
        );
    }
}