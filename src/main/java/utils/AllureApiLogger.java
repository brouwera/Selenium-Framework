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

        attach("Request Method", method);
        attach("Request URL", url);
        attach("Request Headers", JsonUtils.toPrettyJson(headers));
        attach("Request Body", JsonUtils.toPrettyJson(body));
        attach("cURL", generateCurl(method, url, headers, body));
    }

    public static void logResponse(int statusCode, String body) {

        if (!ConfigManager.isApiLoggingEnabled()) {
            return;
        }

        log.info("API Response Status: {}", statusCode);
        log.info("Response Body: {}", body);

        attach("Response Status Code", String.valueOf(statusCode));
        attach("Response Body", JsonUtils.toPrettyJson(body));
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
        attach("API Timing", message);
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
        attach("API Retry Attempt", message);
    }

    // ============================================================
    // Attachment Helper
    // ============================================================
    private static void attach(String name, String content) {
        Allure.addAttachment(
                name,
                "text/plain",
                content == null ? "" : content,
                ".txt"
        );
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