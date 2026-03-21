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
@Feature("Contract Validation")
public class ContractTests {

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
    // Post Schema Validation
    // ============================================================
    @Test(dataProvider = "validPostIds", dataProviderClass = ApiDataProviders.class)
    @Description("Validate that GET /posts/{id} matches the post JSON schema")
    public void testPostSchema(int id) throws Exception {
        var response = postsApi.getPostById(id);

        AssertionHelper.assertStatusCode(response, 200);
        SchemaValidator.validate("post-schema.json", response.getBody());
    }

    // ============================================================
    // User Schema Validation
    // ============================================================
    @Test(dataProvider = "validUserIds", dataProviderClass = ApiDataProviders.class)
    @Description("Validate that GET /users/{id} matches the user JSON schema")
    public void testUserSchema(int id) throws Exception {
        var response = usersApi.getUserById(id);

        AssertionHelper.assertStatusCode(response, 200);
        SchemaValidator.validate("user-schema.json", response.getBody());
    }

    // ============================================================
    // Comment Schema Validation
    // ============================================================
    @Test(dataProvider = "validCommentIds", dataProviderClass = ApiDataProviders.class)
    @Description("Validate that GET /comments/{id} matches the comment JSON schema")
    public void testCommentSchema(int id) throws Exception {
        var response = commentsApi.getCommentById(id);

        AssertionHelper.assertStatusCode(response, 200);
        SchemaValidator.validate("comment-schema.json", response.getBody());
    }
}