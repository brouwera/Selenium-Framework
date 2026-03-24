package api;

import config.ConfigManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AllureApiLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

/**
 * Lightweight HTTP client wrapper for API testing.
 * Provides GET, POST, PUT, and DELETE helpers with clean response handling,
 * centralized Allure logging via AllureApiLogger, and optional retry logic
 * for idempotent operations.
 */
public class ApiClient {

    // ============================================================
    // Fields
    // ============================================================
    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private final HttpClient client;

    // ============================================================
    // Constructor
    // ============================================================
    public ApiClient() {
        this.client = HttpClient.newHttpClient();
    }

    // ============================================================
    // Retry Wrapper
    // ============================================================
    private <T> T executeWithRetry(Supplier<T> action, String description) {
        int maxRetries = ConfigManager.getApiRetries();
        int backoffMs = ConfigManager.getApiRetryBackoffMs();

        int attempt = 0;

        while (true) {
            try {
                return action.get();
            } catch (RuntimeException ex) {

                if (attempt >= maxRetries) {
                    log.error("API call failed after {} retries for: {}", maxRetries, description);
                    throw ex;
                }

                attempt++;

                // Allure retry step
                Allure.step(String.format(
                        "Retry attempt %d of %d for %s",
                        attempt, maxRetries, description
                ));

                // Centralized retry logging
                AllureApiLogger.logRetryAttempt(description, attempt, maxRetries);

                log.warn("Retrying API call (attempt {} of {}) for: {}", attempt, maxRetries, description);

                try {
                    Thread.sleep(backoffMs * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted for: " + description, ie);
                }
            }
        }
    }

    // ============================================================
    // Timing Helper
    // ============================================================
    private void logApiTiming(String method, String url, long durationMs) {
        log.info("{} {} completed in {} ms", method, url, durationMs);
        AllureApiLogger.logTiming(method, url, durationMs);
    }

    // ============================================================
    // GET Request (with retry)
    // ============================================================
    @Step("Send GET request to: {url}")
    public ApiResponse get(String url) {
        return executeWithRetry(() -> doGet(url), "GET " + url);
    }

    private ApiResponse doGet(String url) {
        try {
            long start = System.currentTimeMillis();
            log.debug("Sending GET request to {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Log request
            AllureApiLogger.logRequest("GET", url, request.headers().map(), null);

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long duration = System.currentTimeMillis() - start;

            ApiResponse apiResponse = new ApiResponse(
                    response.statusCode(),
                    response.body(),
                    response.headers().map(),
                    duration
            );

            // Log response + timing
            AllureApiLogger.logResponse(response.statusCode(), response.body());
            logApiTiming("GET", url, duration);

            log.info("GET {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
            return apiResponse;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("GET request failed for: " + url, e);
        }
    }

    // ============================================================
    // POST Request (no retry)
    // ============================================================
    @Step("Send POST request to: {url}")
    public ApiResponse post(String url, String jsonBody) {
        try {
            long start = System.currentTimeMillis();
            log.debug("Sending POST request to {} with body: {}", url, jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Log request
            AllureApiLogger.logRequest("POST", url, request.headers().map(), jsonBody);

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long duration = System.currentTimeMillis() - start;

            ApiResponse apiResponse = new ApiResponse(
                    response.statusCode(),
                    response.body(),
                    response.headers().map(),
                    duration
            );

            // Log response + timing
            AllureApiLogger.logResponse(response.statusCode(), response.body());
            logApiTiming("POST", url, duration);

            log.info("POST {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
            return apiResponse;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("POST request failed for: " + url, e);
        }
    }

    // ============================================================
    // PUT Request (with retry)
    // ============================================================
    @Step("Send PUT request to: {url}")
    public ApiResponse put(String url, String jsonBody) {
        return executeWithRetry(() -> doPut(url, jsonBody), "PUT " + url);
    }

    private ApiResponse doPut(String url, String jsonBody) {
        try {
            long start = System.currentTimeMillis();
            log.debug("Sending PUT request to {} with body: {}", url, jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Log request
            AllureApiLogger.logRequest("PUT", url, request.headers().map(), jsonBody);

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long duration = System.currentTimeMillis() - start;

            ApiResponse apiResponse = new ApiResponse(
                    response.statusCode(),
                    response.body(),
                    response.headers().map(),
                    duration
            );

            // Log response + timing
            AllureApiLogger.logResponse(response.statusCode(), response.body());
            logApiTiming("PUT", url, duration);

            log.info("PUT {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
            return apiResponse;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("PUT request failed for: " + url, e);
        }
    }

    // ============================================================
    // DELETE Request (no retry)
    // ============================================================
    @Step("Send DELETE request to: {url}")
    public ApiResponse delete(String url) {
        try {
            long start = System.currentTimeMillis();
            log.debug("Sending DELETE request to {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            // Log request
            AllureApiLogger.logRequest("DELETE", url, request.headers().map(), null);

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long duration = System.currentTimeMillis() - start;

            ApiResponse apiResponse = new ApiResponse(
                    response.statusCode(),
                    response.body(),
                    response.headers().map(),
                    duration
            );

            // Log response + timing
            AllureApiLogger.logResponse(response.statusCode(), response.body());
            logApiTiming("DELETE", url, duration);

            log.info("DELETE {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
            return apiResponse;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("DELETE request failed for: " + url, e);
        }
    }

    // ============================================================
    // Utility Helpers
    // ============================================================
    private String summarizeBody(String body) {
        if (body == null) return "(no body)";
        return body.length() > 80 ? body.substring(0, 77) + "..." : body;
    }
}