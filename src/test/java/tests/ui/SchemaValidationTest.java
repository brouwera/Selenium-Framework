package tests.ui;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import utils.TestDataManager;

@Epic("Test Data Validation")
@Feature("Schema Validation")
@Owner("Adam Brouwer")
public class SchemaValidationTest {

    // ============================================================
    // Schema Validation Helper
    // ============================================================
    @Step("Validate loginData.json against loginData.schema.json")
    private void validateLoginDataSchema() {
        TestDataManager.validateJsonArrayAgainstSchema(
                "loginData.json",
                "loginData.schema.json"
        );
    }

    // ============================================================
    // Test Case: Login Data Schema Validation
    // ============================================================
    @Story("Validate JSON test data structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that loginData.json conforms to loginData.schema.json for all environments.")
    @Test
    public void validateLoginSchema() {
        validateLoginDataSchema();
    }
}