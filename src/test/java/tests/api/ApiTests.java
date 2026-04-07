package tests.api;

import api.ApiClient;
import api.CommentsApi;
import api.PostsApi;
import api.UsersApi;
import dataproviders.ApiDataProviders;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiScenarioGenerator;
import utils.AllureApiLogger;
import utils.SchemaValidator;

@Epic("API Testing")
@Feature("JSONPlaceholder API Suite")
@Owner("Adam Brouwer")
public class ApiTests {

    private ApiClient client;
    private PostsApi postsApi;
    private UsersApi usersApi;
    private CommentsApi commentsApi;

    @BeforeClass(alwaysRun = true)
    @Story("Initialize API service layer")
    @Severity(SeverityLevel.CRITICAL)
    public void setup() {
        client = new ApiClient();
        postsApi = new PostsApi(client);
        usersApi = new UsersApi(client);
        commentsApi = new CommentsApi(client);
    }

    // ============================================================
    // GET Tests
    // ============================================================
    @Test(dataProvider = "validPostIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /posts/{id}")
    @Description("Verify GET /posts/{id} returns correct post and matches schema.")
    public void testGetPostById(int id) {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = postsApi.getPostById(id);

        AllureApiLogger.attachJson("GET /posts/" + id + " Response Body", response.getBody());

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().contains("\"id\": " + id),
                "Response body should contain the correct post ID"
        );

        SchemaValidator.validate("post-schema.json", response.getBody());
    }

    @Test(dataProvider = "validUserIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /users/{id}")
    @Description("Verify GET /users/{id} returns correct user and matches schema.")
    public void testGetUserById(int id) {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = usersApi.getUserById(id);

        AllureApiLogger.attachJson("GET /users/" + id + " Response Body", response.getBody());

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().contains("\"id\": " + id),
                "Response body should contain the correct user ID"
        );

        SchemaValidator.validate("user-schema.json", response.getBody());
    }

    @Test(dataProvider = "validCommentIds", dataProviderClass = ApiDataProviders.class)
    @Story("GET /comments/{id}")
    @Description("Verify GET /comments/{id} returns correct comment and matches schema.")
    public void testGetCommentById(int id) {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = commentsApi.getCommentById(id);

        AllureApiLogger.attachJson("GET /comments/" + id + " Response Body", response.getBody());

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().contains("\"id\": " + id),
                "Response body should contain the correct comment ID"
        );

        SchemaValidator.validate("comment-schema.json", response.getBody());
    }

    // ============================================================
    // POST Tests
    // ============================================================
    @Test(dataProvider = "createUserPayloads", dataProviderClass = ApiDataProviders.class)
    @Story("POST /users")
    @Description("Verify POST /users creates a new user (mocked behavior).")
    public void testCreateUser(String name, String username, String email) {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = usersApi.createUser(name, username, email);

        AllureApiLogger.attachJson("POST /users Response Body", response.getBody());

        AssertionHelper.assertTrue(
                response.getStatusCode() == 201 || response.getStatusCode() == 200,
                "Expected 200 or 201 for user creation"
        );

        AssertionHelper.assertTrue(
                response.getBody().contains(name),
                "Response body should contain the created user's name"
        );
    }

    @Test(dataProvider = "createCommentPayloads", dataProviderClass = ApiDataProviders.class)
    @Story("POST /comments")
    @Description("Verify POST /comments creates a new comment (mocked behavior).")
    public void testCreateComment(int postId, String name, String email, String body) {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = commentsApi.createComment(postId, name, email, body);

        AllureApiLogger.attachJson("POST /comments Response Body", response.getBody());

        AssertionHelper.assertTrue(
                response.getStatusCode() == 201 || response.getStatusCode() == 200,
                "Expected 200 or 201 for comment creation"
        );

        AssertionHelper.assertTrue(
                response.getBody().contains(name),
                "Response body should contain the created comment's name"
        );
    }

    // ============================================================
    // List Tests
    // ============================================================
    @Test
    @Story("GET /posts")
    @Description("Verify GET /posts returns a list of posts.")
    public void testGetAllPosts() {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = postsApi.getAllPosts();

        AllureApiLogger.attachJson("GET /posts Response Body", response.getBody());

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().startsWith("["),
                "Expected response body to be a JSON array"
        );
    }

    @Test
    @Story("GET /users")
    @Description("Verify GET /users returns a list of users.")
    public void testGetAllUsers() {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = usersApi.getAllUsers();

        AllureApiLogger.attachJson("GET /users Response Body", response.getBody());

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().startsWith("["),
                "Expected response body to be a JSON array"
        );
    }

    @Test
    @Story("GET /comments")
    @Description("Verify GET /comments returns a list of comments.")
    public void testGetAllComments() {

        AiScenarioGenerator.attachSuggestedScenarios("JSONPlaceholder API — General Suite");

        var response = commentsApi.getAllComments();

        AllureApiLogger.attachJson("GET /comments Response Body", response.getBody());

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().startsWith("["),
                "Expected response body to be a JSON array"
        );
    }
}