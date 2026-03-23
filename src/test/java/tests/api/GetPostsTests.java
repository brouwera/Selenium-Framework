package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.PostsApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SchemaValidator;

@Epic("API")
@Feature("Posts API")
public class GetPostsTests {

    private PostsApi postsApi;

    // ============================================================
    // Setup
    // ============================================================
    @Step("Initialize Posts API client")
    @BeforeClass
    public void setUp() {
        ApiClient client = new ApiClient();
        postsApi = new PostsApi(client);
    }

    // ============================================================
    // Positive Tests
    // ============================================================

    @Story("Retrieve all posts")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
    @Description("Validates that GET /posts returns status 200 and matches the expected JSON schema.")
    @Test(groups = {"api", "regression"})
    public void getAllPostsReturns200AndValidSchema() throws Exception {

        ApiResponse response = postsApi.getAllPosts();

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "Status code for GET /posts should be 200"
        );

        SchemaValidator.validate("posts-all.schema.json", response.getBody());
    }

    @Story("Retrieve single post by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
    @Description("Validates that GET /posts/{id} returns status 200 and matches the expected JSON schema.")
    @Test(groups = {"api", "regression"})
    public void getPostByValidIdReturns200AndValidSchema() throws Exception {

        ApiResponse response = postsApi.getPostById(1);

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "Status code for GET /posts/1 should be 200"
        );

        SchemaValidator.validate("posts-single.schema.json", response.getBody());
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================

    @Story("Retrieve post with ID = 0")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when requesting a post with ID = 0.")
    @Test(groups = {"api", "edge"})
    public void getPostByIdZeroReturns404OrEmpty() throws Exception {

        ApiResponse response = postsApi.getPostById(0);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 200,
                "Expected status 404 or 200 for GET /posts/0 but got " + status
        );
    }

    @Story("Retrieve post with extremely large ID")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
    @Description("Validates behavior when requesting a post with a very large ID.")
    @Test(groups = {"api", "edge"})
    public void getPostByVeryLargeIdReturns404OrEmpty() throws Exception {

        ApiResponse response = postsApi.getPostById(999999);

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 200,
                "Expected status 404 or 200 for GET /posts/999999 but got " + status
        );
    }
}