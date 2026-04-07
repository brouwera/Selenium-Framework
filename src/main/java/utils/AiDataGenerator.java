package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import io.qameta.allure.Allure;

import java.time.Instant;
import java.util.*;

/**
 * AI-Generated Test Data Utility
 *
 * Provides:
 * - Local random data generation (default)
 * - AI-style payloads for Posts, Comments, Users
 * - Malicious payloads for negative tests
 * - Oversized payloads
 * - Invalid JSON generation
 * - AI-driven login and table scenarios
 * - Pretty Allure attachments
 */
public final class AiDataGenerator {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();

    private AiDataGenerator() {}

    // ============================================================
    // PUBLIC API — NEW (UI TESTS)
    // ============================================================

    /**
     * Generate AI-style invalid login credentials.
     * Produces:
     * - malformed usernames
     * - weak or invalid passwords
     * - reason for debugging
     *
     * NOTE: Must avoid non-BMP Unicode (ChromeDriver limitation)
     */
    public static Map<String, String> generateInvalidLogin() {
        ensureAiEnabled();

        // BMP-safe invalid usernames
        List<String> badUsernames = List.of(
                "wrongUser",
                "invalid@example",
                "admin' OR 1=1 --",
                "",
                "user" + generateLongString(20),
                "DROP_TABLE_USER"
        );

        // BMP-safe invalid passwords
        List<String> badPasswords = List.of(
                "123",
                "wrongPass",
                "Password!DROP TABLE users;",
                "",
                generateLongString(25),
                "invalid-password"
        );

        String username = badUsernames.get(random.nextInt(badUsernames.size()));
        String password = badPasswords.get(random.nextInt(badPasswords.size()));

        Map<String, String> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        result.put("reason", "AI-generated invalid login combination");

        attach("ai-invalid-login", result);
        return result;
    }

    /**
     * Generate an AI-driven table scenario including:
     * - Natural language instruction
     * - Expected dataset
     * - Action map for TablePage
     */
    public static TableScenario generateTableScenario() {
        ensureAiEnabled();

        List<String> languages = List.of("Java", "Python", "JavaScript");
        List<String> levels = List.of("Beginner", "Intermediate", "Advanced");
        List<String> sortColumns = List.of("Course Name", "Enrollments");

        Map<String, Object> actions = new HashMap<>();

        // Random language filter
        if (random.nextBoolean()) {
            actions.put("language", languages.get(random.nextInt(languages.size())));
        }

        // Random level toggles
        actions.put("beginner", random.nextBoolean());
        actions.put("intermediate", random.nextBoolean());
        actions.put("advanced", random.nextBoolean());

        // Random min enrollments
        if (random.nextBoolean()) {
            actions.put("minEnrollments", 10000);
        }

        // Random sorting
        if (random.nextBoolean()) {
            actions.put("sortBy", sortColumns.get(random.nextInt(sortColumns.size())));
        }

        // Expected dataset (mocked for UI validation)
        List<Map<String, String>> expectedRows = generateExpectedTableRows(actions);

        String instruction = "AI-generated table scenario with filters: " + actions;

        TableScenario scenario = new TableScenario(instruction, expectedRows, actions);

        attach("ai-table-scenario", Map.of(
                "instruction", instruction,
                "actions", actions,
                "expectedRows", expectedRows
        ));

        return scenario;
    }

    // ============================================================
    // Helper for expected table rows (mocked for UI validation)
    // ============================================================
    private static List<Map<String, String>> generateExpectedTableRows(Map<String, Object> actions) {

        List<Map<String, String>> rows = new ArrayList<>();

        int count = 3 + random.nextInt(4); // 3–6 rows

        for (int i = 0; i < count; i++) {
            Map<String, String> row = new HashMap<>();

            // Language
            if (actions.containsKey("language")) {
                row.put("language", (String) actions.get("language"));
            } else {
                row.put("language", randomLanguage());
            }

            // Level
            row.put("level", randomLevel());

            // Enrollments
            int enrollments = 1000 + random.nextInt(50000);
            row.put("enrollments", String.valueOf(enrollments));

            // Course name
            row.put("course", randomCourseName());

            rows.add(row);
        }

        return rows;
    }

    private static String randomLanguage() {
        List<String> langs = List.of("Java", "Python", "JavaScript");
        return langs.get(random.nextInt(langs.size()));
    }

    private static String randomLevel() {
        List<String> levels = List.of("Beginner", "Intermediate", "Advanced");
        return levels.get(random.nextInt(levels.size()));
    }

    private static String randomCourseName() {
        List<String> names = List.of(
                "Intro to Automation",
                "Advanced Selenium",
                "API Testing Mastery",
                "Java Fundamentals",
                "Python for Testers"
        );
        return names.get(random.nextInt(names.size()));
    }

    // ============================================================
    // AI Scenario Suggestions
    // ============================================================

    public static List<String> generateScenarioSuggestions(String featureName) {

        if (featureName == null) {
            return List.of("No feature name provided.");
        }

        switch (featureName.toLowerCase()) {

            case "login page":
                return List.of(
                        "Attempt login with leading/trailing spaces",
                        "Attempt login with Unicode characters",
                        "Attempt login with SQL injection patterns",
                        "Attempt login with extremely long username",
                        "Attempt login with valid username but invalid password"
                );

            case "users api — invalid id":
                return List.of(
                        "Request user with negative ID",
                        "Request user with extremely large ID",
                        "Request user with alphanumeric ID",
                        "Request user with special characters",
                        "Request user with null ID"
                );

            case "table sorting":
                return List.of(
                        "Sort table with mixed numeric and text values",
                        "Sort table with empty cells",
                        "Sort table with duplicate values",
                        "Sort table after filtering",
                        "Sort table after resizing columns"
                );

            default:
                return List.of(
                        "No predefined scenarios — consider adding more cases.",
                        "Try boundary values.",
                        "Try invalid formats.",
                        "Try concurrency or rapid interactions.",
                        "Try unexpected user flows."
                );
        }
    }

    // ============================================================
    // PUBLIC API — EXISTING (API TESTS)
    // ============================================================

    public static Map<String, Object> generateUserPayload() {
        ensureAiEnabled();

        Map<String, Object> user = new HashMap<>();
        user.put("name", randomFullName());
        user.put("username", randomUsername());
        user.put("email", randomEmail());

        attach("ai-user-payload", user);
        return user;
    }

    public static String generatePostTitle() {
        ensureAiEnabled();
        String title = randomSentence();
        attach("ai-post-title", title);
        return title;
    }

    public static String generatePostBody() {
        ensureAiEnabled();
        String body = randomParagraph();
        attach("ai-post-body", body);
        return body;
    }

    public static String generateInvalidJson() {
        ensureAiEnabled();
        String invalid = "{ invalid-json-" + random.nextInt(9999) + " }";
        attach("ai-invalid-json", invalid);
        return invalid;
    }

    public static String generateMaliciousPayload() {
        ensureAiEnabled();

        String payload = """
                {
                    "attack": "<script>alert('xss')</script>",
                    "sql": "' OR 1=1; DROP TABLE users; --",
                    "unicode": "SAFE",
                    "overflow": "%s"
                }
                """.formatted(generateLongString(500));

        attach("ai-malicious-payload", payload);
        return payload;
    }

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