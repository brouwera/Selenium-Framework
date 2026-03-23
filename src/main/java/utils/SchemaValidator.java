package utils;

import io.qameta.allure.Allure;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

/**
 * Validates JSON responses (objects or arrays) against JSON Schema files.
 */
public class SchemaValidator {

    // ============================================================
    // Public Validation Method
    // ============================================================
    public static void validate(String schemaFile, String jsonResponse) {

        Allure.step("Validate JSON response against schema: " + schemaFile);

        InputStream inputStream = SchemaValidator.class
                .getClassLoader()
                .getResourceAsStream("schema/" + schemaFile);

        if (inputStream == null) {
            throw new IllegalArgumentException("Schema file not found in /schema/: " + schemaFile);
        }

        // Load schema
        JSONObject jsonSchema = new JSONObject(new JSONTokener(inputStream));
        Schema schema = SchemaLoader.load(jsonSchema);

        // Detect object vs array
        Object json;
        String trimmed = jsonResponse.trim();

        if (trimmed.startsWith("{")) {
            json = new JSONObject(trimmed);
        } else if (trimmed.startsWith("[")) {
            json = new JSONArray(trimmed);
        } else {
            throw new IllegalArgumentException(
                    "Invalid JSON response. Must start with '{' or '[': " + trimmed
            );
        }

        // Validate
        schema.validate(json); // throws exception if invalid
    }
}