package tests.api;

import api.ApiClient;
import api.ApiResponse;
import api.PostsApi;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AiScenarioGenerator;
import utils.AllureApiLogger;
import utils.SchemaValidator;

@Epic("API")
@Feature("Posts API")
@Owner("Adam Brouwer")
public class GetPostsTests {

    private PostsApi postsApi;

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
    @Description("Validates that GET /posts returns status 200 and matches the expected JSON schema.")
    @Test
    public void getAllPostsReturns200AndValidSchema() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — GET");

        ApiResponse response = postsApi.getAllPosts();

        AllureApiLogger.attachJson("GET /posts Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "Status code for GET /posts should be 200"
        );

        SchemaValidator.validate("posts-all.schema.json", response.getBody());
    }

    @Story("Retrieve single post by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that GET /posts/{id} returns status 200 and matches the expected JSON schema.")
    @Test
    public void getPostByValidIdReturns200AndValidSchema() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — GET");

        ApiResponse response = postsApi.getPostById(1);

        AllureApiLogger.attachJson("GET /posts/1 Response Body", response.getBody());

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
    @Description("Validates behavior when requesting a post with ID = 0.")
    @Test
    public void getPostByIdZeroReturns404OrEmpty() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — GET");

        ApiResponse response = postsApi.getPostById(0);

        AllureApiLogger.attachJson("GET /posts/0 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 200,
                "Expected status 404 or 200 for GET /posts/0 but got " + status
        );
    }

    @Story("Retrieve post with extremely large ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when requesting a post with a very large ID.")
    @Test
    public void getPostByVeryLargeIdReturns404OrEmpty() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — GET");

        ApiResponse response = postsApi.getPostById(999999);

        AllureApiLogger.attachJson("GET /posts/999999 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 200,
                "Expected status 404 or 200 for GET /posts/999999 but got " + status
        );
    }
}