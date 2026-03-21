package api;

import config.ConfigManager;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all API service-layer classes.
 * Provides shared behavior for GET/POST requests, endpoint resolution,
 * and retry logic for flaky endpoints.
 */
public abstract class BaseApi {

    // ============================================================
    // Fields
    // ============================================================
    protected final ApiClient client;
    protected final String baseUrl;

    private static final Logger log = LoggerFactory.getLogger(BaseApi.class);

    // Retry configuration
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 500;

    // ============================================================
    // Constructor
    // ============================================================
    protected BaseApi(ApiClient client) {
        this.client = client;
        this.baseUrl = ConfigManager.getApiBaseUrl();
    }

    // ============================================================
    // GET Request (with Retry)
    // ============================================================
    @Step("Send GET request to endpoint: {endpoint}")
    protected ApiResponse get(String endpoint) throws Exception {
        String url = buildUrl(endpoint);
        return executeWithRetry("GET", url, null);
    }

    // ============================================================
    // POST Request (with Retry)
    // ============================================================
    @Step("Send POST request to endpoint: {endpoint}")
    protected ApiResponse post(String endpoint, String jsonBody) throws Exception {
        String url = buildUrl(endpoint);
        return executeWithRetry("POST", url, jsonBody);
    }

    // ============================================================
    // Retry Wrapper
    // ============================================================
    private ApiResponse executeWithRetry(String method, String url, String body) throws Exception {

        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {

            try {
                log.debug("{} attempt {} to {}", method, attempt, url);

                if (attempt > 1) {
                    io.qameta.allure.Allure.step(
                            "Retry attempt " + attempt + " for " + method + " " + url
                    );
                }

                if ("GET".equals(method)) {
                    return client.get(url);
                } else {
                    return client.post(url, body);
                }

            } catch (Exception ex) {
                lastException = ex;
                log.warn("{} attempt {} failed: {}", method, attempt, ex.getMessage());

                if (attempt < MAX_RETRIES) {
                    Thread.sleep(RETRY_DELAY_MS);
                }
            }
        }

        throw lastException;
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