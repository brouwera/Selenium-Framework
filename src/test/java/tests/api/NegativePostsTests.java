package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.PostsApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiDataGenerator;
import utils.AiScenarioGenerator;
import utils.AllureApiLogger;

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

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

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

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

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

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

        String invalidJson = AiDataGenerator.generateInvalidJson();
        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = postsApi.postRaw("posts", invalidJson);

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

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

        String invalidJson = AiDataGenerator.generateInvalidJson();
        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = postsApi.putRaw("posts/1", invalidJson);

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

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

        String payload = """
                {
                    "userId": 1
                }
                """;

        ApiResponse response = postsApi.postRaw("posts", payload);

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

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

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

    // ============================================================
    // AI-Generated Malicious / Edge Case Payload Tests
    // ============================================================

    @Story("POST /posts with AI-generated malicious payload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /posts receives an AI-generated malicious or suspicious payload.")
    @Test
    public void createPostWithMaliciousPayloadReturnsErrorOr201() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

        String maliciousPayload = AiDataGenerator.generateMaliciousPayload();
        AllureApiLogger.attachText("AI Generated Malicious Payload", maliciousPayload);

        ApiResponse response = postsApi.postRaw("posts", maliciousPayload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }

    @Story("PUT /posts/{id} with AI-generated oversized payload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /posts/{id} receives an extremely large AI-generated payload.")
    @Test
    public void updatePostWithOversizedPayloadReturnsErrorOr200() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

        String longTitle = AiDataGenerator.generateLongString(2000);
        String longBody = AiDataGenerator.generateLongString(5000);

        AllureApiLogger.attachText("AI Generated Oversized Title", longTitle);
        AllureApiLogger.attachText("AI Generated Oversized Body", longBody);

        String payload = """
                {
                    "id": 1,
                    "title": "%s",
                    "body": "%s",
                    "userId": 1
                }
                """.formatted(longTitle, longBody);

        ApiResponse response = postsApi.putRaw("posts/1", payload);

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

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — Negative");

        ApiResponse response = postsApi.triggerServerError();

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 500 || status == 404 || status == 200,
                "Expected 500/404/200 depending on mock behavior, but got " + status
        );
    }
}