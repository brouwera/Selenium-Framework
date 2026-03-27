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
}