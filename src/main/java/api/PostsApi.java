package api;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import org.json.JSONObject;

/**
 * Service-layer class for interacting with the JSONPlaceholder /posts endpoints.
 * Mirrors the Page Object Model structure used in UI tests.
 */
@Owner("Adam Brouwer")
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
    public ApiResponse getAllPosts() {
        return get("posts");
    }

    @Step("Get post by ID: {id}")
    public ApiResponse getPostById(int id) {
        return get("posts/" + id);
    }

    // ============================================================
    // POST Endpoints
    // ============================================================
    @Step("Create a new post for user ID: {userId}")
    public ApiResponse createPost(String title, String body, int userId) {
        JSONObject payload = new JSONObject();
        payload.put("title", title == null ? "" : title);
        payload.put("body", body == null ? "" : body);
        payload.put("userId", userId);

        return post("posts", payload.toString());
    }

    // ============================================================
    // PUT Endpoints
    // ============================================================
    @Step("Update post with ID: {id}")
    public ApiResponse updatePost(int id, String title, String body, int userId) {
        JSONObject payload = new JSONObject();
        payload.put("id", id);
        payload.put("title", title == null ? "" : title);
        payload.put("body", body == null ? "" : body);
        payload.put("userId", userId);

        return put("posts/" + id, payload.toString());
    }

    // ============================================================
    // DELETE Endpoints
    // ============================================================
    @Step("Delete post with ID: {id}")
    public ApiResponse deletePost(int id) {
        return delete("posts/" + id);
    }

    // ============================================================
    // Mocked Error Endpoint (Used in NegativeApiTests)
    // ============================================================
    @Step("Trigger mocked server error for /posts endpoint")
    public ApiResponse triggerServerError() {
        return get("posts/trigger-500");
    }

    // ============================================================
    // Raw Passthrough Helpers (For Negative Testing Only)
    // ============================================================
    @Step("Raw GET request to endpoint: {endpoint}")
    public ApiResponse getRaw(String endpoint) {
        return get(endpoint);
    }

    @Step("Raw POST request to endpoint: {endpoint}")
    public ApiResponse postRaw(String endpoint, String body) {
        return post(endpoint, body);
    }

    @Step("Raw PUT request to endpoint: {endpoint}")
    public ApiResponse putRaw(String endpoint, String body) {
        return put(endpoint, body);
    }

    @Step("Raw DELETE request to endpoint: {endpoint}")
    public ApiResponse deleteRaw(String endpoint) {
        return delete(endpoint);
    }
}