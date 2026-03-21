package api;

import io.qameta.allure.Step;
import org.json.JSONObject;

/**
 * Service-layer class for interacting with the JSONPlaceholder /posts endpoints.
 * Mirrors the Page Object Model structure used in UI tests.
 */
public class PostsApi extends BaseApi {

    // ============================================================
    // Constructor
    // ============================================================
    public PostsApi(ApiClient client) {
        super(client);
    }

    // ============================================================
    // GET Endpoints
    // ============================================================
    @Step("Get all posts")
    public ApiResponse getAllPosts() throws Exception {
        return get("posts");
    }

    @Step("Get post by ID: {id}")
    public ApiResponse getPostById(int id) throws Exception {
        return get("posts/" + id);
    }

    // ============================================================
    // POST Endpoints
    // ============================================================
    @Step("Create a new post for user ID: {userId}")
    public ApiResponse createPost(String title, String body, int userId) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("title", title);
        payload.put("body", body);
        payload.put("userId", userId);

        return post("posts", payload.toString());
    }

    // ============================================================
    // Mocked Error Endpoint (Used in NegativeApiTests)
    // ============================================================
    @Step("Trigger mocked server error for /posts endpoint")
    public ApiResponse triggerServerError() throws Exception {
        return get("posts/trigger-500");
    }
}