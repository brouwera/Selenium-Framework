package api;

import config.ConfigManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all API service-layer classes.
 * Provides shared behavior for GET/POST/PUT/DELETE requests,
 * endpoint resolution, retry logic, and Allure reporting.
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
    // GET Request (with Retry + Allure)
    // ============================================================
    @Step("Send GET request to endpoint: {endpoint}")
    protected ApiResponse get(String endpoint) throws Exception {
        String url = buildUrl(endpoint);
        return executeWithRetry("GET", url, null);
    }

    // ============================================================
    // POST Request (with Retry + Allure)
    // ============================================================
    @Step("Send POST request to endpoint: {endpoint}")
    protected ApiResponse post(String endpoint, String jsonBody) throws Exception {
        String url = buildUrl(endpoint);
        return executeWithRetry("POST", url, jsonBody);
    }

    // ============================================================
    // PUT Request (with Retry + Allure)
    // ============================================================
    @Step("Send PUT request to endpoint: {endpoint}")
    protected ApiResponse put(String endpoint, String jsonBody) throws Exception {
        String url = buildUrl(endpoint);
        return executeWithRetry("PUT", url, jsonBody);
    }

    // ============================================================
    // DELETE Request (with Retry + Allure)
    // ============================================================
    @Step("Send DELETE request to endpoint: {endpoint}")
    protected ApiResponse delete(String endpoint) throws Exception {
        String url = buildUrl(endpoint);
        return executeWithRetry("DELETE", url, null);
    }

    // ============================================================
    // Retry Wrapper + Allure Attachments
    // ============================================================
    private ApiResponse executeWithRetry(String method, String url, String body) throws Exception {

        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {

            try {
                log.debug("{} attempt {} to {}", method, attempt, url);

                if (attempt > 1) {
                    Allure.step("Retry attempt " + attempt + " for " + method + " " + url);
                }

                // Attach request details
                attachRequestDetails(method, url, body);

                ApiResponse response;

                switch (method) {
                    case "GET" -> response = client.get(url);
                    case "POST" -> response = client.post(url, body);
                    case "PUT" -> response = client.put(url, body);
                    case "DELETE" -> response = client.delete(url);
                    default -> throw new IllegalArgumentException("Unsupported method: " + method);
                }

                // Attach response details
                attachResponseDetails(response);

                return response;

            } catch (Exception ex) {
                lastException = ex;
                log.warn("{} attempt {} failed: {}", method, attempt, ex.getMessage());

                Allure.addAttachment(
                        "Exception on attempt " + attempt,
                        ex.getMessage()
                );

                if (attempt < MAX_RETRIES) {
                    Thread.sleep(RETRY_DELAY_MS);
                }
            }
        }

        throw lastException;
    }

    // ============================================================
    // Allure Attachment Helpers
    // ============================================================
    private void attachRequestDetails(String method, String url, String body) {
        Allure.addAttachment("HTTP Method", method);
        Allure.addAttachment("Request URL", url);

        if (body != null) {
            Allure.addAttachment("Request Body", body);
        }
    }

    private void attachResponseDetails(ApiResponse response) {
        Allure.addAttachment("Status Code", String.valueOf(response.getStatusCode()));
        Allure.addAttachment("Response Time (ms)", String.valueOf(response.getResponseTime()));
        Allure.addAttachment("Response Body", response.getBody());
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