package tests.api;

import api.*;
import dataproviders.ApiDataProviders;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("API Testing")
@Feature("Negative Testing Suite")
@Owner("Adam Brouwer")
public class NegativeApiTests {

    // ============================================================
    // Fields
    // ============================================================
    private ApiClient client;
    private UsersApi usersApi;
    private PostsApi postsApi;
    private CommentsApi commentsApi;
    private AuthApi authApi;

    // ============================================================
    // Setup
    // ============================================================
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
    // Invalid ID Tests (JSONPlaceholder returns 404)
    // ============================================================
    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /users/{id} with invalid ID")
    @Description("Verify GET /users/{id} returns 404 for invalid IDs")
    public void testGetUser_InvalidId_ShouldReturn404(int id) {
        var response = usersApi.getUserById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /posts/{id} with invalid ID")
    @Description("Verify GET /posts/{id} returns 404 for invalid IDs")
    public void testGetPost_InvalidId_ShouldReturn404(int id) {
        var response = postsApi.getPostById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /comments/{id} with invalid ID")
    @Description("Verify GET /comments/{id} returns 404 for invalid IDs")
    public void testGetComment_InvalidId_ShouldReturn404(int id) {
        var response = commentsApi.getCommentById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    // ============================================================
    // Invalid Payload Tests (JSONPlaceholder returns 201)
    // ============================================================
    @Test(dataProvider = "invalidCommentPayloads", dataProviderClass = ApiDataProviders.class)
    @Story("POST /comments with invalid payload")
    @Description("Verify POST /comments still returns 201 even with invalid payloads")
    public void testCreateComment_InvalidPayload_ShouldStillReturn201(
            Integer postId, String name, String email, String body) {

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
    // Auth Tests (JSONPlaceholder has no auth → always 404)
    // ============================================================
    @Test(dataProvider = "invalidAuthPayloads", dataProviderClass = ApiDataProviders.class)
    @Story("POST /auth/login with invalid credentials")
    @Description("Verify POST /auth/login returns 404 because auth endpoints do not exist")
    public void testLogin_InvalidCredentials_ShouldReturn404(String username, String password) {
        var response = authApi.login(username, password);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test
    @Story("POST /auth/refresh with invalid token")
    @Description("Verify POST /auth/refresh returns 404 because auth endpoints do not exist")
    public void testRefreshToken_InvalidToken_ShouldReturn404() {
        var response = authApi.refreshToken("invalid-token");
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test
    @Story("POST /auth/logout with invalid token")
    @Description("Verify POST /auth/logout returns 404 because auth endpoints do not exist")
    public void testLogout_InvalidToken_ShouldReturn404() {
        var response = authApi.logout("invalid-token");
        AssertionHelper.assertStatusCode(response, 404);
    }

    // ============================================================
    // Missing Field Tests (JSONPlaceholder returns 201)
    // ============================================================
    @Test
    @Story("POST /users with missing fields")
    @Description("Verify POST /users still returns 201 even when fields are missing")
    public void testCreateUser_MissingFields_ShouldStillReturn201() {
        var response = usersApi.createUser("", "", "");
        AssertionHelper.assertEquals(
                response.getStatusCode(),
                201,
                "JSONPlaceholder accepts empty fields and returns 201"
        );
    }

    // ============================================================
    // Server Error Simulation (JSONPlaceholder returns 404)
    // ============================================================
    @Test
    @Story("GET /posts/trigger-500 (mocked server error)")
    @Description("Verify mocked server error endpoint returns 404 (endpoint does not exist)")
    public void testServerError_MockedEndpoint_ShouldReturn404() {
        var response = postsApi.triggerServerError();
        AssertionHelper.assertStatusCode(response, 404);
    }
}