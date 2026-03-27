package api;

import config.ConfigManager;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;

/**
 * Base class for all API service-layer classes.
 * Provides shared behavior for GET/POST/PUT/DELETE requests
 * and endpoint resolution. All retry logic, timing, and
 * request/response logging is delegated to ApiClient.
 */
@Owner("Adam Brouwer")
public abstract class BaseApi {

    // ============================================================
    // Fields
    // ============================================================
    protected final ApiClient client;
    protected final String baseUrl;

    // ============================================================
    // Constructor
    // ============================================================
    protected BaseApi(ApiClient client) {
        this.client = client;
        this.baseUrl = ConfigManager.getApiBaseUrl();
    }

    // ============================================================
    // GET Request
    // ============================================================
    @Step("Send GET request to endpoint: {endpoint}")
    protected ApiResponse get(String endpoint) {
        return client.get(buildUrl(endpoint));
    }

    // ============================================================
    // POST Request
    // ============================================================
    @Step("Send POST request to endpoint: {endpoint}")
    protected ApiResponse post(String endpoint, String jsonBody) {
        return client.post(buildUrl(endpoint), jsonBody);
    }

    // ============================================================
    // PUT Request
    // ============================================================
    @Step("Send PUT request to endpoint: {endpoint}")
    protected ApiResponse put(String endpoint, String jsonBody) {
        return client.put(buildUrl(endpoint), jsonBody);
    }

    // ============================================================
    // DELETE Request
    // ============================================================
    @Step("Send DELETE request to endpoint: {endpoint}")
    protected ApiResponse delete(String endpoint) {
        return client.delete(buildUrl(endpoint));
    }

    // ============================================================
    // URL Builder
    // ============================================================
    private String buildUrl(String endpoint) {
        return baseUrl.endsWith("/") ?
                baseUrl + endpoint :
                baseUrl + "/" + endpoint;
    }
}