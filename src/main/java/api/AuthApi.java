package api;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;

import java.util.Map;

/**
 * Mock authentication service-layer class.
 * JSONPlaceholder does not support real authentication,
 * so these endpoints return a mocked 404 response.
 */
@Owner("Adam Brouwer")
public class AuthApi extends BaseApi {

    public AuthApi(ApiClient client) {
        super(client);
    }

    // ============================================================
    // LOGIN (UPDATED TO ACCEPT MAP PAYLOAD)
    // ============================================================
    @Step("Send mocked login request with payload map")
    public ApiResponse login(Map<String, Object> payload) {
        return new ApiResponse(
                404,
                "{\"error\":\"Auth endpoint does not exist\"}",
                null,
                0
        );
    }

    // ============================================================
    // REFRESH TOKEN
    // ============================================================
    @Step("Send mocked refresh token request")
    public ApiResponse refreshToken(String refreshToken) {
        return new ApiResponse(
                404,
                "{\"error\":\"Auth endpoint does not exist\"}",
                null,
                0
        );
    }

    // ============================================================
    // LOGOUT
    // ============================================================
    @Step("Send mocked logout request")
    public ApiResponse logout(String token) {
        return new ApiResponse(
                404,
                "{\"error\":\"Auth endpoint does not exist\"}",
                null,
                0
        );
    }
}