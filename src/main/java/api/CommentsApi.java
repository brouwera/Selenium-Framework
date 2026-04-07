package api;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import org.json.JSONObject;

import java.util.Map;

/**
 * Service-layer class for interacting with the JSONPlaceholder /comments endpoints.
 * Mirrors the Page Object Model structure used in UI tests.
 */
@Owner("Adam Brouwer")
public class CommentsApi extends BaseApi {

    // ============================================================
    // Constructor
    // ============================================================
    public CommentsApi(ApiClient client) {
        super(client);
    }

    // ============================================================
    // GET Endpoints
    // ============================================================
    @Step("Get all comments")
    public ApiResponse getAllComments() {
        return get("comments");
    }

    @Step("Get comment by ID: {id}")
    public ApiResponse getCommentById(int id) {
        return get("comments/" + id);
    }

    @Step("Get comments for post ID: {postId}")
    public ApiResponse getCommentsForPost(int postId) {
        return get("posts/" + postId + "/comments");
    }

    // ============================================================
    // POST Endpoints (UPDATED TO ACCEPT MAP PAYLOAD)
    // ============================================================
    @Step("Create a new comment using payload map")
    public ApiResponse createComment(Map<String, Object> payload) {
        JSONObject json = new JSONObject(payload);
        return post("comments", json.toString());
    }

    // ============================================================
    // RAW Passthrough Methods
    // ============================================================
    @Step("POST raw to endpoint: {endpoint}")
    public ApiResponse postRaw(String endpoint, String rawBody) {
        return post(endpoint, rawBody);
    }

    @Step("PUT raw to endpoint: {endpoint}")
    public ApiResponse putRaw(String endpoint, String rawBody) {
        return put(endpoint, rawBody);
    }

    @Step("DELETE raw to endpoint: {endpoint}")
    public ApiResponse deleteRaw(String endpoint) {
        return delete(endpoint);
    }

    @Step("GET raw from endpoint: {endpoint}")
    public ApiResponse getRaw(String endpoint) {
        return get(endpoint);
    }
}