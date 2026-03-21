package api;

import io.qameta.allure.Step;
import org.json.JSONObject;

/**
 * Service-layer class for interacting with the JSONPlaceholder /comments endpoints.
 * Mirrors the Page Object Model structure used in UI tests.
 */
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
    public ApiResponse getAllComments() throws Exception {
        return get("comments");
    }

    @Step("Get comment by ID: {id}")
    public ApiResponse getCommentById(int id) throws Exception {
        return get("comments/" + id);
    }

    @Step("Get comments for post ID: {postId}")
    public ApiResponse getCommentsForPost(int postId) throws Exception {
        return get("posts/" + postId + "/comments");
    }

    // ============================================================
    // POST Endpoints
    // ============================================================

    @Step("Create a new comment for post ID: {postId}")
    public ApiResponse createComment(int postId, String name, String email, String body) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("postId", postId);
        payload.put("name", name);
        payload.put("email", email);
        payload.put("body", body);

        return post("comments", payload.toString());
    }
}