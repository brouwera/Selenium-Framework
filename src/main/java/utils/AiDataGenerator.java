package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import io.qameta.allure.Allure;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * AI-Generated Test Data Utility
 *
 * Provides:
 * - Local random data generation (default)
 * - AI-style payloads for Posts, Comments, Users
 * - Malicious payloads for negative tests
 * - Oversized payloads
 * - Invalid JSON generation
 * - Pretty Allure attachments
 */
public final class AiDataGenerator {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();

    private AiDataGenerator() {}

    // ============================================================
    // PUBLIC API
    // ============================================================

    /** Generate AI-style user payload */
    public static Map<String, Object> generateUserPayload() {
        ensureAiEnabled();

        Map<String, Object> user = new HashMap<>();
        user.put("name", randomFullName());
        user.put("username", randomUsername());
        user.put("email", randomEmail());

        attach("ai-user-payload", user);
        return user;
    }

    /** Generate AI-style post title */
    public static String generatePostTitle() {
        ensureAiEnabled();
        String title = randomSentence();
        attach("ai-post-title", title);
        return title;
    }

    /** Generate AI-style post body */
    public static String generatePostBody() {
        ensureAiEnabled();
        String body = randomParagraph();
        attach("ai-post-body", body);
        return body;
    }

    /** Generate invalid JSON for negative tests */
    public static String generateInvalidJson() {
        ensureAiEnabled();
        String invalid = "{ invalid-json-" + random.nextInt(9999) + " }";
        attach("ai-invalid-json", invalid);
        return invalid;
    }

    /** Generate malicious payload for negative tests */
    public static String generateMaliciousPayload() {
        ensureAiEnabled();

        String payload = """
                {
                    "attack": "<script>alert('xss')</script>",
                    "sql": "' OR 1=1; DROP TABLE users; --",
                    "unicode": "🔥💀🚨",
                    "overflow": "%s"
                }
                """.formatted(generateLongString(500));

        attach("ai-malicious-payload", payload);
        return payload;
    }

    /** Generate long string for oversized payload tests */
    public static String generateLongString(int length) {
        ensureAiEnabled();

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append("X");
        }

        String result = sb.toString();
        attach("ai-long-string-" + length, result);
        return result;
    }

    // ============================================================
    // EXISTING METHODS (KEPT + USED BY NEW TESTS)
    // ============================================================

    public static Map<String, Object> generateUser() {
        ensureAiEnabled();

        Map<String, Object> user = new HashMap<>();
        user.put("id", random.nextInt(10_000));
        user.put("name", randomFullName());
        user.put("username", randomUsername());
        user.put("email", randomEmail());
        user.put("phone", randomPhone());
        user.put("website", randomWebsite());

        attach("ai-user", user);
        return user;
    }

    public static Map<String, Object> generatePostPayload() {
        ensureAiEnabled();

        Map<String, Object> post = new HashMap<>();
        post.put("userId", random.nextInt(1000));
        post.put("title", randomSentence());
        post.put("body", randomParagraph());

        attach("ai-post-payload", post);
        return post;
    }

    public static Map<String, Object> generateCommentPayload() {
        ensureAiEnabled();

        Map<String, Object> comment = new HashMap<>();
        comment.put("postId", random.nextInt(1000));
        comment.put("name", randomSentence());
        comment.put("email", randomEmail());
        comment.put("body", randomParagraph());

        attach("ai-comment-payload", comment);
        return comment;
    }

    public static Map<String, String> generateCredentials() {
        ensureAiEnabled();

        Map<String, String> creds = new HashMap<>();
        creds.put("username", randomUsername());
        creds.put("password", randomPassword());

        attach("ai-credentials", creds);
        return creds;
    }

    public static Map<String, Object> generateTableRow() {
        ensureAiEnabled();

        Map<String, Object> row = new HashMap<>();
        row.put("name", randomFullName());
        row.put("email", randomEmail());
        row.put("age", 18 + random.nextInt(50));
        row.put("status", randomStatus());

        attach("ai-table-row", row);
        return row;
    }

    // ============================================================
    // RANDOM DATA PROVIDERS
    // ============================================================

    private static String randomFullName() {
        String[] first = {"Alex", "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Jamie"};
        String[] last = {"Smith", "Johnson", "Brown", "Davis", "Miller", "Wilson", "Moore"};
        return first[random.nextInt(first.length)] + " " + last[random.nextInt(last.length)];
    }

    private static String randomUsername() {
        return "user" + random.nextInt(100000);
    }

    private static String randomEmail() {
        return "test" + random.nextInt(100000) + "@example.com";
    }

    private static String randomPhone() {
        return "555-" + (100 + random.nextInt(900)) + "-" + (1000 + random.nextInt(9000));
    }

    private static String randomWebsite() {
        return "www.example" + random.nextInt(1000) + ".com";
    }

    private static String randomPassword() {
        return "Pass!" + random.nextInt(100000);
    }

    private static String randomSentence() {
        String[] words = {"alpha", "beta", "gamma", "delta", "omega", "sigma", "lambda"};
        return words[random.nextInt(words.length)] + " " +
                words[random.nextInt(words.length)] + " test";
    }

    private static String randomParagraph() {
        return randomSentence() + ". " + randomSentence() + ". " + randomSentence() + ".";
    }

    private static String randomStatus() {
        String[] statuses = {"Active", "Pending", "Inactive"};
        return statuses[random.nextInt(statuses.length)];
    }

    // ============================================================
    // ALLURE ATTACHMENT
    // ============================================================

    private static void attach(String name, Object data) {
        try {
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("provider", ConfigManager.getAiDataProvider());
            wrapper.put("timestamp", Instant.now().toString());
            wrapper.put("data", data);

            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);

            Allure.addAttachment(
                    "AI Data — " + name,
                    "application/json",
                    json,
                    ".json"
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to attach AI-generated data to Allure", e);
        }
    }

    // ============================================================
    // SAFETY CHECK
    // ============================================================

    private static void ensureAiEnabled() {
        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException(
                    "AI data generation is disabled. Enable 'aiDataEnabled' in config.json."
            );
        }
    }
}