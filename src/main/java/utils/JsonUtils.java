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