package api;

import io.qameta.allure.Step;
import org.json.JSONObject;

/**
 * Service-layer class for interacting with the JSONPlaceholder /users endpoints.
 * Mirrors the Page Object Model structure used in UI tests.
 */
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
    public ApiResponse getAllUsers() throws Exception {
        return get("users");
    }

    @Step("Get user by ID: {id}")
    public ApiResponse getUserById(int id) throws Exception {
        return get("users/" + id);
    }

    // ============================================================
    // POST Endpoints
    // ============================================================
    @Step("Create a new user with name: {name}, username: {username}")
    public ApiResponse createUser(String name, String username, String email) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("name", name);
        payload.put("username", username);
        payload.put("email", email);

        return post("users", payload.toString());
    }
}