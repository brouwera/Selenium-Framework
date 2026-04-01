package api;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import org.json.JSONObject;

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
    // POST Endpoints
    // ============================================================

    @Step("Create a new comment for post ID: {postId}")
    public ApiResponse createComment(int postId, String name, String email, String body) {
        JSONObject payload = new JSONObject();
        payload.put("postId", postId);
        payload.put("name", name == null ? "" : name);
        payload.put("email", email == null ? "" : email);
        payload.put("body", body == null ? "" : body);

        return post("comments", payload.toString());
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