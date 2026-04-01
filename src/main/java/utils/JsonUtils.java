package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidTestDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public final class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
        // Prevent instantiation
    }

    // ============================================================
    // Pretty JSON Helpers
    // ============================================================

    /**
     * Pretty prints a JSON string. If the input is not valid JSON,
     * the raw string is returned unchanged.
     */
    public static String toPrettyJson(String json) {
        if (json == null || json.isEmpty()) {
            return "";
        }

        try {
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            // Not valid JSON → return raw string
            return json;
        }
    }

    /**
     * Pretty prints any Java object (Map, List, POJO, etc.).
     * Falls back to toString() if serialization fails.
     */
    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return "";
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    // ============================================================
    // JSON Data Provider Loader
    // ============================================================

    /**
     * Reads a JSON array of objects and converts it into Object[][] for TestNG.
     *
     * @param path relative path under src/test/resources/testData/
     * @return Object[][] where each row contains a Map<String, String>
     */
    public static Object[][] readJsonAsDataProvider(String path) {

        log.debug("Loading JSON test data from {}", path);

        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path)) {

            if (is == null) {
                throw new InvalidTestDataException(
                        "JSON file not found: " + path +
                                ". Ensure it exists under src/test/resources/testData/"
                );
            }

            List<Map<String, String>> list = mapper.readValue(
                    is,
                    new TypeReference<List<Map<String, String>>>() {}
            );

            if (list.isEmpty()) {
                throw new InvalidTestDataException(
                        "JSON file '" + path + "' contains no data rows."
                );
            }

            log.info("Loaded {} rows from {}", list.size(), path);

            Object[][] data = new Object[list.size()][1];
            for (int i = 0; i < list.size(); i++) {
                data[i][0] = list.get(i);
            }

            return data;

        } catch (Exception e) {
            throw new InvalidTestDataException(
                    "Failed to parse JSON file '" + path + "': " + e.getMessage(), e
            );
        }
    }
}