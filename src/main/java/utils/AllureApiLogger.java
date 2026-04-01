package utils;

import config.ConfigManager;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Centralized API logging utility for Allure reporting.
 * Handles request/response logging, pretty JSON formatting,
 * cURL generation for reproducible debugging, and timing attachments.
 */
public class AllureApiLogger {

    private static final Logger log = LoggerFactory.getLogger(AllureApiLogger.class);

    // ============================================================
    // Public Logging Methods
    // ============================================================

    public static void logRequest(String method,
                                  String url,
                                  Map<String, ?> headers,
                                  String body) {

        if (!ConfigManager.isApiLoggingEnabled()) {
            return;
        }

        log.info("API Request: {} {}", method, url);
        log.info("Headers: {}", headers);
        log.info("Body: {}", body);

        attachText("Request Method", method);
        attachText("Request URL", url);
        attachJson("Request Headers", JsonUtils.toPrettyJson(headers));
        attachJson("Request Body", JsonUtils.toPrettyJson(body));
        attachText("cURL", generateCurl(method, url, headers, body));
    }

    public static void logResponse(int statusCode, String body) {

        if (!ConfigManager.isApiLoggingEnabled()) {
            return;
        }

        log.info("API Response Status: {}", statusCode);
        log.info("Response Body: {}", body);

        attachText("Response Status Code", String.valueOf(statusCode));
        attachJson("Response Body", JsonUtils.toPrettyJson(body));
    }

    /**
     * Logs API timing information as a separate Allure attachment.
     */
    public static void logTiming(String method, String url, long durationMs) {

        if (!ConfigManager.isApiLoggingEnabled()) {
            return;
        }

        String message = String.format(
                "%s %s completed in %d ms",
                method, url, durationMs
        );

        log.info(message);
        attachText("API Timing", message);
    }

    /**
     * Logs retry attempt information as a separate Allure attachment.
     */
    public static void logRetryAttempt(String description, int attempt, int maxRetries) {

        if (!ConfigManager.isApiLoggingEnabled()) {
            return;
        }

        String message = String.format(
                "Retry attempt %d of %d for %s",
                attempt, maxRetries, description
        );

        log.warn(message);
        attachText("API Retry Attempt", message);
    }

    // ============================================================
    // New Public Attachment Helpers
    // ============================================================

    /**
     * Attach plain text content to Allure.
     */
    public static void attachText(String name, String content) {
        Allure.addAttachment(
                name,
                "text/plain",
                content == null ? "" : content,
                ".txt"
        );
    }

    /**
     * Attach JSON content to Allure with pretty formatting.
     */
    public static void attachJson(String name, String json) {
        Allure.addAttachment(
                name,
                "application/json",
                json == null ? "" : json,
                ".json"
        );
    }

    // ============================================================
    // Legacy Attachment Helper (kept for backward compatibility)
    // ============================================================
    private static void attach(String name, String content) {
        attachText(name, content);
    }

    // ============================================================
    // cURL Generator
    // ============================================================
    private static String generateCurl(String method,
                                       String url,
                                       Map<String, ?> headers,
                                       String body) {

        StringBuilder curl = new StringBuilder("curl -X ")
                .append(method)
                .append(" \"")
                .append(url)
                .append("\"");

        if (headers != null) {
            headers.forEach((key, value) ->
                    curl.append(" -H \"")
                            .append(key)
                            .append(": ")
                            .append(value)
                            .append("\""));
        }

        if (body != null && !body.isEmpty()) {
            curl.append(" -d '")
                    .append(body.replace("'", "\\'"))
                    .append("'");
        }

        return curl.toString();
    }
}