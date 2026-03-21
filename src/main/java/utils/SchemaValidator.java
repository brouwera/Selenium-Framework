package utils;

import io.qameta.allure.Allure;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

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

        JSONObject jsonSchema = new JSONObject(new JSONTokener(inputStream));
        JSONObject jsonObject = new JSONObject(jsonResponse);

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonObject); // throws exception if invalid
    }
}