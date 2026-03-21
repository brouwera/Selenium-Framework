package tests.api;

import api.*;
import dataproviders.ApiDataProviders;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("API Testing")
@Feature("Negative Testing Suite")
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
    @BeforeClass(alwaysRun = true)
    @Story("Initialize API service layer")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
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
    @Description("Verify GET /users/{id} returns 404 for invalid IDs")
    public void testGetUser_InvalidId_ShouldReturn404(int id) throws Exception {
        var response = usersApi.getUserById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Description("Verify GET /posts/{id} returns 404 for invalid IDs")
    public void testGetPost_InvalidId_ShouldReturn404(int id) throws Exception {
        var response = postsApi.getPostById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = ApiDataProviders.class)
    @Description("Verify GET /comments/{id} returns 404 for invalid IDs")
    public void testGetComment_InvalidId_ShouldReturn404(int id) throws Exception {
        var response = commentsApi.getCommentById(id);
        AssertionHelper.assertStatusCode(response, 404);
    }

    // ============================================================
    // Invalid Payload Tests (JSONPlaceholder returns 201)
    // ============================================================
    @Test(dataProvider = "invalidCommentPayloads", dataProviderClass = ApiDataProviders.class)
    @Description("Verify POST /comments still returns 201 even with invalid payloads")
    public void testCreateComment_InvalidPayload_ShouldStillReturn201(
            Integer postId, String name, String email, String body) throws Exception {

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
    @Description("Verify POST /auth/login returns 404 because auth endpoints do not exist")
    public void testLogin_InvalidCredentials_ShouldReturn404(String username, String password) throws Exception {
        var response = authApi.login(username, password);
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test
    @Description("Verify POST /auth/refresh returns 404 because auth endpoints do not exist")
    public void testRefreshToken_InvalidToken_ShouldReturn404() throws Exception {
        var response = authApi.refreshToken("invalid-token");
        AssertionHelper.assertStatusCode(response, 404);
    }

    @Test
    @Description("Verify POST /auth/logout returns 404 because auth endpoints do not exist")
    public void testLogout_InvalidToken_ShouldReturn404() throws Exception {
        var response = authApi.logout("invalid-token");
        AssertionHelper.assertStatusCode(response, 404);
    }

    // ============================================================
    // Missing Field Tests (JSONPlaceholder returns 201)
    // ============================================================
    @Test
    @Description("Verify POST /users still returns 201 even when fields are missing")
    public void testCreateUser_MissingFields_ShouldStillReturn201() throws Exception {
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
    @Description("Verify mocked server error endpoint returns 404 (endpoint does not exist)")
    public void testServerError_MockedEndpoint_ShouldReturn404() throws Exception {
        var response = postsApi.triggerServerError();
        AssertionHelper.assertStatusCode(response, 404);
    }
}