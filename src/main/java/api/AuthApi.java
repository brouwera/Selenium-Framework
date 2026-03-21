package api;

import io.qameta.allure.Step;
import org.json.JSONObject;

/**
 * Mock authentication service-layer class.
 * JSONPlaceholder does not support real authentication, so these endpoints
 * simulate a realistic login/refresh/logout flow for testing purposes.
 */
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
    public ApiResponse login(String username, String password) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("password", password);

        return post("auth/login", payload.toString());
    }

    @Step("Send mocked refresh token request")
    public ApiResponse refreshToken(String refreshToken) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("refreshToken", refreshToken);

        return post("auth/refresh", payload.toString());
    }

    @Step("Send mocked logout request")
    public ApiResponse logout(String token) throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("token", token);

        return post("auth/logout", payload.toString());
    }
}