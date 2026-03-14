package api;

import java.util.Map;

public class ApiResponse {

    private final int statusCode;
    private final String body;
    private final Map<String, java.util.List<String>> headers;

    public ApiResponse(int statusCode, String body, Map<String, java.util.List<String>> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, java.util.List<String>> getHeaders() {
        return headers;
    }
}