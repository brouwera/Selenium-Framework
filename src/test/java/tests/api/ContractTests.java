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
@Owner("Adam Brouwer")
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
    @Story("Validate post schema")
    @Description("Validate that GET /posts/{id} matches the post JSON schema")
    public void testPostSchema(int id) {
        var response = postsApi.getPostById(id);

        AssertionHelper.assertStatusCode(response, 200);
        SchemaValidator.validate("post-schema.json", response.getBody());
    }

    // ============================================================
    // User Schema Validation
    // ============================================================
    @Test(dataProvider = "validUserIds", dataProviderClass = ApiDataProviders.class)
    @Story("Validate user schema")
    @Description("Validate that GET /users/{id} matches the user JSON schema")
    public void testUserSchema(int id) {
        var response = usersApi.getUserById(id);

        AssertionHelper.assertStatusCode(response, 200);
        SchemaValidator.validate("user-schema.json", response.getBody());
    }

    // ============================================================
    // Comment Schema Validation
    // ============================================================
    @Test(dataProvider = "validCommentIds", dataProviderClass = ApiDataProviders.class)
    @Story("Validate comment schema")
    @Description("Validate that GET /comments/{id} matches the comment JSON schema")
    public void testCommentSchema(int id) {
        var response = commentsApi.getCommentById(id);

        AssertionHelper.assertStatusCode(response, 200);
        SchemaValidator.validate("comment-schema.json", response.getBody());
    }
}