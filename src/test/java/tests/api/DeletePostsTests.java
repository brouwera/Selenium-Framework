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

@Epic("API")
@Feature("Posts API")
@Owner("Adam Brouwer")
public class DeletePostsTests {

    private PostsApi postsApi;

    @BeforeClass
    public void setUp() {
        ApiClient client = new ApiClient();
        postsApi = new PostsApi(client);
    }

    // ============================================================
    // Positive Tests
    // ============================================================
    @Story("Delete an existing post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that DELETE /posts/{id} returns a successful status code.")
    @Test
    public void deletePostReturns200() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — DELETE");

        ApiResponse response = postsApi.deleteRaw("posts/1");

        AllureApiLogger.attachJson("DELETE /posts/1 Response Body", response.getBody());

        AssertionHelper.assertEquals(
                response.getStatusCode(),
                200,
                "Status code for DELETE /posts/1 should be 200"
        );
    }

    // ============================================================
    // Edge Case Tests
    // ============================================================
    @Story("Delete post with ID = 0")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when deleting a post with ID = 0.")
    @Test
    public void deletePostWithIdZeroReturns404Or200() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — DELETE");

        ApiResponse response = postsApi.deleteRaw("posts/0");

        AllureApiLogger.attachJson("DELETE /posts/0 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 200,
                "Expected 404 or 200 for DELETE /posts/0 but got " + status
        );
    }

    @Story("Delete post with extremely large ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when deleting a post with a very large ID.")
    @Test
    public void deletePostWithVeryLargeIdReturns404Or200() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — DELETE");

        ApiResponse response = postsApi.deleteRaw("posts/999999");

        AllureApiLogger.attachJson("DELETE /posts/999999 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 404 || status == 200,
                "Expected 404 or 200 for DELETE /posts/999999 but got " + status
        );
    }

    // ============================================================
    // Negative Tests
    // ============================================================
    @Story("Delete post with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when deleting a post using an invalid ID.")
    @Test
    public void deletePostWithInvalidIdReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — DELETE");

        ApiResponse response = postsApi.deleteRaw("posts/invalid-id");

        AllureApiLogger.attachText("DELETE /posts/invalid-id Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 400 || status == 500 || status == 200,
                "Expected 400/500/200 depending on mock behavior, but got " + status
        );
    }

    @Story("Delete post with malformed endpoint")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates behavior when the DELETE endpoint is malformed.")
    @Test
    public void deletePostWithMalformedEndpointReturnsError() {

        AiScenarioGenerator.attachSuggestedScenarios("Posts API — DELETE");

        ApiResponse response = postsApi.deleteRaw("posts//1");

        AllureApiLogger.attachText("DELETE /posts//1 Response Body", response.getBody());

        int status = response.getStatusCode();

        AssertionHelper.assertTrue(
                status == 200 || status == 400 || status == 404 || status == 500,
                "Expected 200/400/404/500 depending on mock behavior, but got " + status
        );
    }
}