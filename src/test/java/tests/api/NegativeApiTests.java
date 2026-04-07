package tests.api;

import api.*;
import dataproviders.ApiDataProviders;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiDataGenerator;
import utils.AiScenarioGenerator;
import utils.AllureApiLogger;

@Epic("API Testing")
@Feature("Negative Testing Suite")
@Owner("Adam Brouwer")
public class NegativeApiTests {

    private ApiClient client;
    private UsersApi usersApi;
    private PostsApi postsApi;
    private CommentsApi commentsApi;
    private AuthApi authApi;

    @BeforeClass
    @Story("Initialize API service layer")
    @Severity(SeverityLevel.CRITICAL)
    public void setup() {
        client = new ApiClient();
        usersApi = new UsersApi(client);
        postsApi = new PostsApi(client);
        commentsApi = new CommentsApi(client);
        authApi = new AuthApi(client);
    }

    // ============================================================
    // Invalid ID Tests
    // ============================================================
    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /users/{id} with invalid ID")
    @Description("Verify GET /users/{id} returns 404 for invalid IDs.")
    public void testGetUser_InvalidId_ShouldReturn404(int id) {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = usersApi.getUserById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /posts/{id} with invalid ID")
    @Description("Verify GET /posts/{id} returns 404 for invalid IDs.")
    public void testGetPost_InvalidId_ShouldReturn404(int id) {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = postsApi.getPostById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /comments/{id} with invalid ID")
    @Description("Verify GET /comments/{id} returns 404 for invalid IDs.")
    public void testGetComment_InvalidId_ShouldReturn404(int id) {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = commentsApi.getCommentById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    // ============================================================
    // Invalid Payload Tests
    // ============================================================
    @Test(dataProvider = "invalidCommentPayloads", dataProviderClass = ApiDataProviders.class)
    @Story("POST /comments with invalid payload")
    @Description("Verify POST /comments still returns 201 even with invalid payloads.")
    public void testCreateComment_InvalidPayload_ShouldStillReturn201(
            Integer postId, String name, String email, String body) {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = commentsApi.createComment(
                postId == null ? 0 : postId,
                name,
                email,
                body
        );

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder accepts invalid payloads and returns 201"
        );
    }

    // ============================================================
    // AI-Generated Invalid Payload Tests
    // ============================================================
    @Test
    @Story("POST /comments with AI-generated malicious payload")
    @Description("Verify POST /comments returns 201 or error when receiving an AI-generated malicious payload.")
    public void testCreateComment_MaliciousPayload_ShouldReturn201OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        String maliciousPayload = AiDataGenerator.generateMaliciousPayload();
        AllureApiLogger.attachText("AI Generated Malicious Payload", maliciousPayload);

        var response = commentsApi.postRaw("comments", maliciousPayload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }

    @Test
    @Story("POST /comments with AI-generated oversized payload")
    @Description("Verify POST /comments returns 201 or error when receiving an oversized AI-generated payload.")
    public void testCreateComment_OversizedPayload_ShouldReturn201OrError() {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        String longName = AiDataGenerator.generateLongString(2000);
        String longBody = AiDataGenerator.generateLongString(5000);

        AllureApiLogger.attachText("AI Generated Oversized Name", longName);
        AllureApiLogger.attachText("AI Generated Oversized Body", longBody);

        String payload = """
                {
                    "postId": 1,
                    "name": "%s",
                    "email": "test@example.com",
                    "body": "%s"
                }
                """.formatted(longName, longBody);

        var response = commentsApi.postRaw("comments", payload);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 201 || status == 400 || status == 500,
                "Expected 201/400/500 depending on mock behavior, but got " + status
        );
    }

    // ============================================================
    // Auth Tests
    // ============================================================
    @Test(dataProvider = "invalidAuthPayloads", dataProviderClass = ApiDataProviders.class)
    @Story("POST /auth/login with invalid credentials")
    @Description("Verify POST /auth/login returns 404 because auth endpoints do not exist.")
    public void testLogin_InvalidCredentials_ShouldReturn404(String username, String password) {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = authApi.login(username, password);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test
    @Story("POST /auth/refresh with invalid token")
    @Description("Verify POST /auth/refresh returns 404 because auth endpoints do not exist.")
    public void testRefreshToken_InvalidToken_ShouldReturn404() {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = authApi.refreshToken("invalid-token");
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test
    @Story("POST /auth/logout with invalid token")
    @Description("Verify POST /auth/logout returns 404 because auth endpoints do not exist.")
    public void testLogout_InvalidToken_ShouldReturn404() {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = authApi.logout("invalid-token");
        AssertionHelper.assertStatusCode(response, 404);
    }

    // ============================================================
    // Missing Field Tests
    // ============================================================
    @Test
    @Story("POST /users with missing fields")
    @Description("Verify POST /users still returns 201 even when fields are missing.")
    public void testCreateUser_MissingFields_ShouldStillReturn201() {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = usersApi.createUser("", "", "");
        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder accepts empty fields and returns 201"
        );
    }

    // ============================================================
    // Server Error Simulation
    // ============================================================
    @Test
    @Story("GET /posts/trigger-500 (mocked server error)")
    @Description("Verify mocked server error endpoint returns 404 (endpoint does not exist).")
    public void testServerError_MockedEndpoint_ShouldReturn404() {

        AiScenarioGenerator.attachSuggestedScenarios("API — Negative Suite");

        var response = postsApi.triggerServerError();
        AssertionHelper.assertStatusCode(response, 404);
    }
}