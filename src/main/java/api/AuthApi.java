package api;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;

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

    @Step("Send mocked login request for user: {username}")
    public ApiResponse login(String username, String password) {
        return new ApiResponse(
                404,
                "{\"error\":\"Auth endpoint does not exist\"}",
                null,
                0
        );
    }

    @Step("Send mocked refresh token request")
    public ApiResponse refreshToken(String refreshToken) {
        return new ApiResponse(
                404,
                "{\"error\":\"Auth endpoint does not exist\"}",
                null,
                0
        );
    }

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