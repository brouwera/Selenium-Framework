package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import io.qameta.allure.Allure;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Day 51 — AI-Generated Test Data Utility
 *
 * This class provides environment-aware, config-driven test data generation.
 * - Supports local random data generation (default)
 * - Future-ready for OpenAI / Azure providers
 * - Automatically attaches generated data to Allure reports
 * - Produces JSON-serializable maps for UI + API tests
 */
public final class AiDataGenerator {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();

    private AiDataGenerator() {}

    // ============================================================
    // Public API
    // ============================================================

    /**
     * Generates a realistic user profile.
     */
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

    /**
     * Generates a payload suitable for POST /posts.
     */
    public static Map<String, Object> generatePostPayload() {
        ensureAiEnabled();

        Map<String, Object> post = new HashMap<>();
        post.put("userId", random.nextInt(1000));
        post.put("title", randomSentence());
        post.put("body", randomParagraph());

        attach("ai-post-payload", post);
        return post;
    }

    /**
     * Generates a payload suitable for POST /comments.
     */
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

    /**
     * Generates credentials for login tests.
     */
    public static Map<String, String> generateCredentials() {
        ensureAiEnabled();

        Map<String, String> creds = new HashMap<>();
        creds.put("username", randomUsername());
        creds.put("password", randomPassword());

        attach("ai-credentials", creds);
        return creds;
    }

    /**
     * Generates a single table row for Table module tests.
     */
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
    // Local Provider (Default)
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
    // Allure Attachment
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
    // Safety Check
    // ============================================================
    private static void ensureAiEnabled() {
        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException(
                    "AI data generation is disabled. Enable 'aiDataEnabled' in config.json."
            );
        }
    }
}