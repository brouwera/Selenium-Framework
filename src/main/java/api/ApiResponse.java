package api;

import java.util.List;
import java.util.Map;

/**
 * Represents a structured HTTP response returned by ApiClient.
 * Provides status code, body, headers, response time, and helper accessors.
 */
public class ApiResponse {

    // ============================================================
    // Fields
    // ============================================================
    private final int statusCode;
    private final String body;
    private final Map<String, List<String>> headers;
    private final long responseTime;

    // ============================================================
    // Constructor
    // ============================================================
    public ApiResponse(int statusCode,
                       String body,
                       Map<String, List<String>> headers,
                       long responseTime) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
        this.responseTime = responseTime;
    }

    // ============================================================
    // Basic Getters
    // ============================================================
    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public long getResponseTime() {
        return responseTime;
    }

    // ============================================================
    // Convenience Helpers
    // ============================================================
    /**
     * Returns the first header value for the given header name,
     * or null if the header does not exist.
     */
    public String getHeader(String name) {
        if (headers == null || name == null) return null;

        List<String> values = headers.get(name);
        if (values == null || values.isEmpty()) return null;

        return values.get(0);
    }
}