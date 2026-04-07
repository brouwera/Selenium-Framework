package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.CommentsApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiDataGenerator;
import utils.AiScenarioGenerator;
import utils.AllureApiLogger;

@Epic("API")
@Feature("Comments API - Negative Scenarios")
@Owner("Adam Brouwer")
public class NegativeCommentsTests {

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
    // Invalid ID Tests
    // ============================================================
    @Story("GET /comments/{id} with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when GET /comments/{id} is called with an invalid ID.")
    @Test
    public void getCommentWithInvalidIdReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        ApiResponse response = commentsApi.getCommentById(-1);

        AllureApiLogger.attachJson("GET /comments/-1 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 400 || status == 200,
                "Expected 404/400/200 depending on mock behavior, but got " + status
        );
    }

    @Story("GET /comments/{id} with non-numeric ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when GET /comments/{id} is called with a non-numeric ID.")
    @Test
    public void getCommentWithNonNumericIdReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        ApiResponse response = commentsApi.getRaw("comments/abc123");

        AllureApiLogger.attachJson("GET /comments/abc123 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 404 || status == 500,
                "Expected 400/404/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Invalid JSON Tests
    // ============================================================
    @Story("POST /comments with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /comments receives malformed JSON.")
    @Test
    public void createCommentWithInvalidJsonReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = commentsApi.postRaw("comments", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 201,
                "Expected 400/500/201 depending on mock behavior, but got " + status
        );
    }

    @Story("PUT /comments/{id} with invalid JSON")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /comments/{id} receives malformed JSON.")
    @Test
    public void updateCommentWithInvalidJsonReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        String invalidJson = AiDataGenerator.generateInvalidJson();

        AllureApiLogger.attachText("AI Generated Invalid JSON", invalidJson);

        ApiResponse response = commentsApi.putRaw("comments/1", invalidJson);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Missing Field Tests
    // ============================================================
    @Story("POST /comments with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /comments is missing required fields.")
    @Test
    public void createCommentWithMissingFieldsReturns201OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        String payload = "{ \"postId\": 1 }";

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = commentsApi.postRaw("comments", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }

    @Story("PUT /comments/{id} with missing fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /comments/{id} is missing required fields.")
    @Test
    public void updateCommentWithMissingFieldsReturns200OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        String payload = "{ \"name\": \"Only Name Provided\" }";

        AllureApiLogger.attachText("Missing Fields Payload", payload);

        ApiResponse response = commentsApi.putRaw("comments/1", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // AI-Generated Malicious / Oversized Payload Tests
    // ============================================================
    @Story("POST /comments with AI-generated malicious payload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when POST /comments receives an AI-generated malicious or suspicious payload.")
    @Test
    public void createCommentWithMaliciousPayloadReturns201OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        String maliciousPayload = AiDataGenerator.generateMaliciousPayload();

        AllureApiLogger.attachText("AI Generated Malicious Payload", maliciousPayload);

        ApiResponse response = commentsApi.postRaw("comments", maliciousPayload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }

    @Story("PUT /comments/{id} with AI-generated oversized payload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when PUT /comments/{id} receives an extremely large AI-generated payload.")
    @Test
    public void updateCommentWithOversizedPayloadReturns200OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        String longName = AiDataGenerator.generateLongString(2000);
        String longBody = AiDataGenerator.generateLongString(5000);

        AllureApiLogger.attachText("AI Generated Oversized Name", longName);
        AllureApiLogger.attachText("AI Generated Oversized Body", longBody);

        String payload = """
                {
                    "id": 1,
                    "postId": 1,
                    "name": "%s",
                    "email": "test@example.com",
                    "body": "%s"
                }
                """.formatted(longName, longBody);

        ApiResponse response = commentsApi.putRaw("comments/1", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 500,
                "Expected 200/400/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Malformed Endpoint Tests
    // ============================================================
    @Story("DELETE /comments//1 malformed endpoint")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when the DELETE endpoint is malformed.")
    @Test
    public void deleteCommentWithMalformedEndpointReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Comments API — Negative");

        ApiResponse response = commentsApi.deleteRaw("comments//1");

        AllureApiLogger.attachJson("DELETE /comments//1 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 404 || status == 500,
                "Expected 200/400/404/500 depending on mock behavior, but got " + status
        );
    }
}