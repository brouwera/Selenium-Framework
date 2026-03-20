package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import exceptions.InvalidTestDataException;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public final class TestDataManager {

    private static final Logger log = LoggerFactory.getLogger(TestDataManager.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private TestDataManager() {}

    public static Object[][] loadJson(String basePath, String schemaPath) {

        String env = ConfigManager.getEnvironment().toLowerCase();
        String envPath = basePath.replace("testData/", "testData/" + env + "/");

        log.info("Loading test data for environment '{}' from {}", env, envPath);

        try (InputStream dataStream = getResource(envPath);
             InputStream schemaStream = getResource(schemaPath)) {

            if (dataStream == null) {
                throw new InvalidTestDataException("Test data file not found: " + envPath);
            }
            if (schemaStream == null) {
                throw new InvalidTestDataException("Schema file not found: " + schemaPath);
            }

            // Load schema
            JSONObject schemaJson = new JSONObject(new JSONTokener(schemaStream));
            Schema schema = SchemaLoader.load(schemaJson);

            // Parse JSON array into List<Map<String,String>>
            List<Map<String, String>> list = mapper.readValue(
                    dataStream,
                    new TypeReference<List<Map<String, String>>>() {}
            );

            if (list.isEmpty()) {
                throw new InvalidTestDataException("Test data file '" + envPath + "' contains no rows.");
            }

            // Convert list to JSONArray for schema validation
            JSONArray jsonArray = new JSONArray(list);

            // Validate entire array against schema
            schema.validate(jsonArray);

            log.info("Validated {} rows against schema {}", list.size(), schemaPath);

            // Convert to Object[][]
            Object[][] data = new Object[list.size()][1];
            for (int i = 0; i < list.size(); i++) {
                data[i][0] = list.get(i);
            }

            return data;

        } catch (Exception e) {
            throw new InvalidTestDataException(
                    "Failed to load or validate test data: " + e.getMessage(), e
            );
        }
    }

    private static InputStream getResource(String path) {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
    }
}