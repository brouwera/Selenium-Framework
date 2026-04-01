package api;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import org.json.JSONObject;

/**
 * Service-layer class for interacting with the JSONPlaceholder /users endpoints.
 * Mirrors the Page Object Model structure used in UI tests.
 */
@Owner("Adam Brouwer")
public class UsersApi extends BaseApi {

    // ============================================================
    // Constructor
    // ============================================================
    public UsersApi(ApiClient client) {
        super(client);
    }

    // ============================================================
    // GET Endpoints
    // ============================================================
    @Step("Get all users")
    public ApiResponse getAllUsers() {
        return get("users");
    }

    @Step("Get user by ID: {id}")
    public ApiResponse getUserById(int id) {
        return get("users/" + id);
    }

    // ============================================================
    // POST Endpoints
    // ============================================================
    @Step("Create a new user with name: {name}, username: {username}")
    public ApiResponse createUser(String name, String username, String email) {
        JSONObject payload = new JSONObject();
        payload.put("name", name == null ? "" : name);
        payload.put("username", username == null ? "" : username);
        payload.put("email", email == null ? "" : email);

        return post("users", payload.toString());
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