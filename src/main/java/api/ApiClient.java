package api;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Lightweight HTTP client wrapper for API testing.
 * Provides GET and POST helpers with clean response handling
 * and full Allure request/response attachments.
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

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        long duration = System.currentTimeMillis() - start;

        ApiResponse apiResponse = new ApiResponse(
                response.statusCode(),
                response.body(),
                response.headers().map(),
                duration
        );

        attachAllureDetails("GET", url, null, apiResponse);

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

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        long duration = System.currentTimeMillis() - start;

        ApiResponse apiResponse = new ApiResponse(
                response.statusCode(),
                response.body(),
                response.headers().map(),
                duration
        );

        attachAllureDetails("POST", url, jsonBody, apiResponse);

        log.info("POST {} → {} {}", url, response.statusCode(), summarizeBody(response.body()));
        return apiResponse;
    }

    // ============================================================
    // Allure Attachments
    // ============================================================
    private void attachAllureDetails(String method,
                                     String url,
                                     String requestBody,
                                     ApiResponse response) {

        Allure.addAttachment("Request Method", method);
        Allure.addAttachment("Request URL", url);

        if (requestBody != null) {
            Allure.addAttachment(
                    "Request Body",
                    "application/json",
                    new ByteArrayInputStream(requestBody.getBytes()),
                    ".json"
            );
        }

        Allure.addAttachment("Status Code", String.valueOf(response.getStatusCode()));
        Allure.addAttachment("Response Time (ms)", String.valueOf(response.getResponseTime()));

        Allure.addAttachment(
                "Response Body",
                "application/json",
                new ByteArrayInputStream(response.getBody().getBytes()),
                ".json"
        );

        Allure.addAttachment("Response Headers", response.getHeaders().toString());
    }

    // ============================================================
    // Utility Helpers
    // ============================================================
    private String summarizeBody(String body) {
        if (body == null) return "(no body)";
        return body.length() > 80 ? body.substring(0, 77) + "..." : body;
    }
}