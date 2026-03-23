package api;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AllureApiLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Lightweight HTTP client wrapper for API testing.
 * Provides GET, POST, PUT, and DELETE helpers with clean response handling
 * and centralized Allure logging via AllureApiLogger.
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
    // GET Request
    // ============================================================
    @Step("Send GET request to: {url}")
    public ApiResponse get(String url) throws IOException, InterruptedException {

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

        // Log response
        AllureApiLogger.logResponse(response.statusCode(), response.body());

        log.info("GET {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
        return apiResponse;
    }

    // ============================================================
    // POST Request
    // ============================================================
    @Step("Send POST request to: {url}")
    public ApiResponse post(String url, String jsonBody) throws IOException, InterruptedException {

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

        // Log response
        AllureApiLogger.logResponse(response.statusCode(), response.body());

        log.info("POST {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
        return apiResponse;
    }

    // ============================================================
    // PUT Request
    // ============================================================
    @Step("Send PUT request to: {url}")
    public ApiResponse put(String url, String jsonBody) throws IOException, InterruptedException {

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

        // Log response
        AllureApiLogger.logResponse(response.statusCode(), response.body());

        log.info("PUT {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
        return apiResponse;
    }

    // ============================================================
    // DELETE Request
    // ============================================================
    @Step("Send DELETE request to: {url}")
    public ApiResponse delete(String url) throws IOException, InterruptedException {

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

        // Log response
        AllureApiLogger.logResponse(response.statusCode(), response.body());

        log.info("DELETE {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
        return apiResponse;
    }

    // ============================================================
    // Utility Helpers
    // ============================================================
    private String summarizeBody(String body) {
        if (body == null) return "(no body)";
        return body.length() > 80 ? body.substring(0, 77) + "..." : body;
    }
}