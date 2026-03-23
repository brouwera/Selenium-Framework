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
    // PUT Endpoints
    // ============================================================
    @Step("Update post with ID: {id}")
    public ApiResponse updatePost(int id, String title, String body, int userId) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("id", id);
        payload.put("title", title);
        payload.put("body", body);
        payload.put("userId", userId);

        return put("posts/" + id, payload.toString());
    }

    // ============================================================
    // DELETE Endpoints
    // ============================================================
    @Step("Delete post with ID: {id}")
    public ApiResponse deletePost(int id) throws Exception {
        return delete("posts/" + id);
    }

    // ============================================================
    // Mocked Error Endpoint (Used in NegativeApiTests)
    // ============================================================
    @Step("Trigger mocked server error for /posts endpoint")
    public ApiResponse triggerServerError() throws Exception {
        return get("posts/trigger-500");
    }

    // ============================================================
    // Raw Passthrough Helpers (For Negative Testing Only)
    // ============================================================
    @Step("Raw GET request to endpoint: {endpoint}")
    public ApiResponse getRaw(String endpoint) throws Exception {
        return get(endpoint);
    }

    @Step("Raw POST request to endpoint: {endpoint}")
    public ApiResponse postRaw(String endpoint, String body) throws Exception {
        return post(endpoint, body);
    }

    @Step("Raw PUT request to endpoint: {endpoint}")
    public ApiResponse putRaw(String endpoint, String body) throws Exception {
        return put(endpoint, body);
    }

    @Step("Raw DELETE request to endpoint: {endpoint}")
    public ApiResponse deleteRaw(String endpoint) throws Exception {
        return delete(endpoint);
    }
}