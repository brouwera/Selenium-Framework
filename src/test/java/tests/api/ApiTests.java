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
import utils.SchemaValidator;

@Epic("API Testing")
@Feature("JSONPlaceholder API Suite")
public class ApiTests {

    // ============================================================
    // Fields
    // ============================================================
    private ApiClient client;
    private PostsApi postsApi;
    private UsersApi usersApi;
    private CommentsApi commentsApi;

    // ============================================================
    // Setup
    // ============================================================
    @BeforeClass(alwaysRun = true)
    @Story("Initialize API service layer")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
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
    @Description("Verify GET /posts/{id} returns correct post and matches schema")
    public void testGetPostById(int id) throws Exception {
        var response = postsApi.getPostById(id);

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().contains("\"id\": " + id),
                "Response body should contain the correct post ID"
        );

        SchemaValidator.validate("post-schema.json", response.getBody());
    }

    @Test(dataProvider = "validUserIds", dataProviderClass = ApiDataProviders.class)
    @Description("Verify GET /users/{id} returns correct user and matches schema")
    public void testGetUserById(int id) throws Exception {
        var response = usersApi.getUserById(id);

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().contains("\"id\": " + id),
                "Response body should contain the correct user ID"
        );

        SchemaValidator.validate("user-schema.json", response.getBody());
    }

    @Test(dataProvider = "validCommentIds", dataProviderClass = ApiDataProviders.class)
    @Description("Verify GET /comments/{id} returns correct comment and matches schema")
    public void testGetCommentById(int id) throws Exception {
        var response = commentsApi.getCommentById(id);

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
    @Description("Verify POST /users creates a new user (mocked behavior)")
    public void testCreateUser(String name, String username, String email) throws Exception {
        var response = usersApi.createUser(name, username, email);

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
    @Description("Verify POST /comments creates a new comment (mocked behavior)")
    public void testCreateComment(int postId, String name, String email, String body) throws Exception {
        var response = commentsApi.createComment(postId, name, email, body);

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
    @Description("Verify GET /posts returns a list of posts")
    public void testGetAllPosts() throws Exception {
        var response = postsApi.getAllPosts();

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().startsWith("["),
                "Expected response body to be a JSON array"
        );
    }

    @Test
    @Description("Verify GET /users returns a list of users")
    public void testGetAllUsers() throws Exception {
        var response = usersApi.getAllUsers();

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().startsWith("["),
                "Expected response body to be a JSON array"
        );
    }

    @Test
    @Description("Verify GET /comments returns a list of comments")
    public void testGetAllComments() throws Exception {
        var response = commentsApi.getAllComments();

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertTrue(
                response.getBody().startsWith("["),
                "Expected response body to be a JSON array"
        );
    }
}