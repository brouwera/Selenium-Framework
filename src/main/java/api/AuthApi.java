package api;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import org.json.JSONObject;

/**
 * Mock authentication service-layer class.
 * JSONPlaceholder does not support real authentication, so these endpoints
 * simulate a realistic login/refresh/logout flow for testing purposes.
 */
@Owner("Adam Brouwer")
public class AuthApi extends BaseApi {

    // ============================================================
    // Constructor
    // ============================================================
    public AuthApi(ApiClient client) {
        super(client);
    }

    // ============================================================
    // Mocked Authentication Endpoints
    // ============================================================

    @Step("Send mocked login request for user: {username}")
    public ApiResponse login(String username, String password) {
        JSONObject payload = new JSONObject();
        payload.put("username", username == null ? "" : username);
        payload.put("password", password == null ? "" : password);

        return post("auth/login", payload.toString());
    }

    @Step("Send mocked refresh token request")
    public ApiResponse refreshToken(String refreshToken) {
        JSONObject payload = new JSONObject();
        payload.put("refreshToken", refreshToken == null ? "" : refreshToken);

        return post("auth/refresh", payload.toString());
    }

    @Step("Send mocked logout request")
    public ApiResponse logout(String token) {
        JSONObject payload = new JSONObject();
        payload.put("token", token == null ? "" : token);

        return post("auth/logout", payload.toString());
    }
}