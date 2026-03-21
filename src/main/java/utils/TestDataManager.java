package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unified, environment-aware Test Data Manager.
 * Supports:
 *  - JSON object loading
 *  - JSON array loading
 *  - CSV loading
 *  - Schema validation
 *  - Caching
 *  - Backward-compatible DataProvider method
 */
public final class TestDataManager {

    // ============================================================
    // Constants & Fields
    // ============================================================
    private static final Logger log = LoggerFactory.getLogger(TestDataManager.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String TEST_DATA_BASE = "testData/";
    private static final String SCHEMA_BASE = "schema/";

    // Cache for JSON objects, arrays, CSV maps, etc.
    private static final Map<String, Object> cache = new HashMap<>();

    private TestDataManager() {}

    // ============================================================
    // Path Resolution
    // ============================================================
    private static String resolveDataPath(String fileName) {
        String env = ConfigManager.getEnvironment().toLowerCase();
        return TEST_DATA_BASE + env + "/" + fileName;
    }

    private static String resolveSchemaPath(String schemaFile) {
        return SCHEMA_BASE + schemaFile;
    }

    private static InputStream getResource(String path) {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
    }

    // ============================================================
    // JSON Array Loader
    // ============================================================
    public static List<Map<String, String>> loadJsonArray(String fileName) {
        String path = resolveDataPath(fileName);

        if (cache.containsKey(path)) {
            return (List<Map<String, String>>) cache.get(path);
        }

        try (InputStream stream = getResource(path)) {

            if (stream == null) {
                throw new InvalidTestDataException("JSON file not found: " + path);
            }

            List<Map<String, String>> list = mapper.readValue(
                    stream,
                    new TypeReference<List<Map<String, String>>>() {}
            );

            cache.put(path, list);
            return list;

        } catch (Exception e) {
            throw new InvalidTestDataException("Failed to load JSON array: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // JSON Object Loader
    // ============================================================
    public static JsonNode loadJsonObject(String fileName) {
        String path = resolveDataPath(fileName);

        if (cache.containsKey(path)) {
            return (JsonNode) cache.get(path);
        }

        try (InputStream stream = getResource(path)) {

            if (stream == null) {
                throw new InvalidTestDataException("JSON file not found: " + path);
            }

            JsonNode node = mapper.readTree(stream);
            cache.put(path, node);
            return node;

        } catch (Exception e) {
            throw new InvalidTestDataException("Failed to load JSON object: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // CSV Loader
    // ============================================================
    public static List<Map<String, String>> loadCsv(String fileName) {
        String path = resolveDataPath(fileName);

        if (cache.containsKey(path)) {
            return (List<Map<String, String>>) cache.get(path);
        }

        try {
            List<Map<String, String>> csv = CSVUtils.readCsvAsListOfMaps(path);
            cache.put(path, csv);
            return csv;

        } catch (Exception e) {
            throw new InvalidTestDataException("Failed to load CSV: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // Schema Validation
    // ============================================================
    public static void validateJsonArrayAgainstSchema(String dataFile, String schemaFile) {
        String dataPath = resolveDataPath(dataFile);
        String schemaPath = resolveSchemaPath(schemaFile);

        try (InputStream dataStream = getResource(dataPath);
             InputStream schemaStream = getResource(schemaPath)) {

            if (dataStream == null) {
                throw new InvalidTestDataException("Test data file not found: " + dataPath);
            }
            if (schemaStream == null) {
                throw new InvalidTestDataException("Schema file not found: " + schemaPath);
            }

            JSONObject schemaJson = new JSONObject(new JSONTokener(schemaStream));
            Schema schema = SchemaLoader.load(schemaJson);

            List<Map<String, String>> list = mapper.readValue(
                    dataStream,
                    new TypeReference<List<Map<String, String>>>() {}
            );

            if (list.isEmpty()) {
                throw new InvalidTestDataException(
                        "Test data file '" + dataPath + "' contains no rows."
                );
            }

            JSONArray jsonArray = new JSONArray(list);
            schema.validate(jsonArray);

            log.info("Validated {} rows against schema {}", list.size(), schemaFile);

        } catch (Exception e) {
            throw new InvalidTestDataException("Schema validation failed: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // DataProvider Loader (Backward Compatible)
    // ============================================================
    public static Object[][] loadJsonDataProvider(String dataFile, String schemaFile) {

        validateJsonArrayAgainstSchema(dataFile, schemaFile);

        List<Map<String, String>> list = loadJsonArray(dataFile);

        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i);
        }

        return data;
    }

    // ============================================================
    // Key-Based Access
    // ============================================================
    public static String get(String jsonFile, String key) {
        JsonNode node = loadJsonObject(jsonFile);
        JsonNode value = node.at("/" + key.replace(".", "/"));

        if (value.isMissingNode()) {
            throw new InvalidTestDataException("Key not found: " + key);
        }

        return value.asText();
    }
}