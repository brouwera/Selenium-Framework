package dataproviders;

import config.ConfigManager;
import io.qameta.allure.Allure;
import org.testng.annotations.DataProvider;
import utils.AiDataGenerator;

import java.util.Map;

/**
 * Centralized DataProviders for API test cases.
 * Includes:
 * - Original static providers (required by existing tests)
 * - AI-generated positive payloads
 * - AI-generated negative payloads
 * - Hybrid (AI → fallback) providers
 * - Static fallback data for deterministic runs
 */
public class ApiDataProviders {

    // ============================================================
    // STATIC ID PROVIDERS (existing tests depend on these)
    // ============================================================
    @DataProvider(name = "validUserIds")
    public Object[][] validUserIds() {
        return new Object[][]{
                {1}, {2}, {3}, {4}, {5}
        };
    }

    @DataProvider(name = "validPostIds")
    public Object[][] validPostIds() {
        return new Object[][]{
                {1}, {10}, {50}, {75}, {100}
        };
    }

    @DataProvider(name = "validCommentIds")
    public Object[][] validCommentIds() {
        return new Object[][]{
                {1}, {5}, {25}, {50}, {100}
        };
    }

    // ============================================================
    // STATIC NEGATIVE ID PROVIDER (required by NegativeApiTests)
    // ============================================================
    @DataProvider(name = "invalidIds")
    public Object[][] invalidIds() {
        return new Object[][]{
                {-1},
                {0},
                {9999},
                {123456},
                {Integer.MAX_VALUE}
        };
    }

    // ============================================================
    // STATIC PAYLOAD PROVIDERS (fallback)
    // ============================================================
    @DataProvider(name = "createUserPayloads")
    public Object[][] createUserPayloads() {
        return new Object[][]{
                {"John Doe", "johnd", "john@example.com"},
                {"Jane Smith", "jsmith", "jane@example.com"},
                {"Adam Brouwer", "abrouwer", "adam@example.com"}
        };
    }

    @DataProvider(name = "createCommentPayloads")
    public Object[][] createCommentPayloads() {
        return new Object[][]{
                {1, "Adam", "adam@example.com", "This is a test comment"},
                {2, "John", "john@example.com", "Another comment"},
                {3, "Jane", "jane@example.com", "Yet another comment"}
        };
    }

    // ============================================================
    // STATIC NEGATIVE PAYLOAD PROVIDERS (required by NegativeApiTests)
    // ============================================================
    @DataProvider(name = "invalidAuthPayloads")
    public Object[][] invalidAuthPayloads() {
        return new Object[][]{
                {"", ""},                     // empty username/password
                {"user", ""},                 // missing password
                {"", "password"},             // missing username
                {"invalid", "invalid"},       // wrong credentials
                {null, null}                  // null values
        };
    }

    @DataProvider(name = "invalidCommentPayloads")
    public Object[][] invalidCommentPayloads() {
        return new Object[][]{
                {null, "name", "email@example.com", "body"},   // null postId
                {1, "", "email@example.com", "body"},          // empty name
                {1, "name", "", "body"},                       // empty email
                {1, "name", "email@example.com", ""},          // empty body
                {1, null, null, null}                          // all null
        };
    }

    // ============================================================
    // AI-GENERATED POSITIVE PAYLOADS
    // ============================================================
    @DataProvider(name = "createUserPayloadsAi")
    public Object[][] createUserPayloadsAi() {

        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException("AI user payloads requested but aiDataEnabled=false");
        }

        Map<String, Object> user = AiDataGenerator.generateUserPayload();
        Allure.addAttachment("AI User Payload", user.toString());

        return new Object[][]{
                {
                        user.get("name"),
                        user.get("username"),
                        user.get("email")
                }
        };
    }

    @DataProvider(name = "createPostPayloadsAi")
    public Object[][] createPostPayloadsAi() {

        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException("AI post payloads requested but aiDataEnabled=false");
        }

        Map<String, Object> post = AiDataGenerator.generatePostPayload();
        Allure.addAttachment("AI Post Payload", post.toString());

        return new Object[][]{
                {
                        post.get("title"),
                        post.get("body"),
                        post.get("userId")
                }
        };
    }

    @DataProvider(name = "createCommentPayloadsAi")
    public Object[][] createCommentPayloadsAi() {

        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException("AI comment payloads requested but aiDataEnabled=false");
        }

        Map<String, Object> comment = AiDataGenerator.generateCommentPayload();
        Allure.addAttachment("AI Comment Payload", comment.toString());

        return new Object[][]{
                {
                        comment.get("postId"),
                        comment.get("name"),
                        comment.get("email"),
                        comment.get("body")
                }
        };
    }

    // ============================================================
    // AI-GENERATED NEGATIVE PAYLOADS
    // ============================================================
    @DataProvider(name = "invalidJsonAi")
    public Object[][] invalidJsonAi() {

        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException("AI invalid JSON requested but aiDataEnabled=false");
        }

        String invalid = AiDataGenerator.generateInvalidJson();
        Allure.addAttachment("AI Invalid JSON", invalid);

        return new Object[][]{
                {invalid}
        };
    }

    @DataProvider(name = "maliciousPayloadsAi")
    public Object[][] maliciousPayloadsAi() {

        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException("AI malicious payloads requested but aiDataEnabled=false");
        }

        String payload = AiDataGenerator.generateMaliciousPayload();
        Allure.addAttachment("AI Malicious Payload", payload);

        return new Object[][]{
                {payload}
        };
    }

    @DataProvider(name = "oversizedPayloadsAi")
    public Object[][] oversizedPayloadsAi() {

        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException("AI oversized payloads requested but aiDataEnabled=false");
        }

        String longString = AiDataGenerator.generateLongString(500);
        Allure.addAttachment("AI Oversized Payload", longString);

        return new Object[][]{
                {longString}
        };
    }

    // ============================================================
    // HYBRID PROVIDERS (AI → FALLBACK)
    // ============================================================
    @DataProvider(name = "createPostPayloadsHybrid")
    public Object[][] createPostPayloadsHybrid() {

        if (ConfigManager.isAiDataEnabled()) {
            try {
                Map<String, Object> post = AiDataGenerator.generatePostPayload();
                Allure.addAttachment("Hybrid Post Payload (AI)", post.toString());

                return new Object[][]{
                        {
                                post.get("title"),
                                post.get("body"),
                                post.get("userId")
                        }
                };
            } catch (Exception ignored) {}
        }

        return createUserPayloads();
    }

    @DataProvider(name = "invalidJsonHybrid")
    public Object[][] invalidJsonHybrid() {

        if (ConfigManager.isAiDataEnabled()) {
            try {
                String invalid = AiDataGenerator.generateInvalidJson();
                Allure.addAttachment("Hybrid Invalid JSON (AI)", invalid);

                return new Object[][]{
                        {invalid}
                };
            } catch (Exception ignored) {}
        }

        return new Object[][]{
                {"{ invalid json }"}
        };
    }

    @DataProvider(name = "maliciousPayloadsHybrid")
    public Object[][] maliciousPayloadsHybrid() {

        if (ConfigManager.isAiDataEnabled()) {
            try {
                String payload = AiDataGenerator.generateMaliciousPayload();
                Allure.addAttachment("Hybrid Malicious Payload (AI)", payload);

                return new Object[][]{
                        {payload}
                };
            } catch (Exception ignored) {}
        }

        return new Object[][]{
                {"{ \"attack\": \"<script>alert('xss')</script>\" }"}
        };
    }
}