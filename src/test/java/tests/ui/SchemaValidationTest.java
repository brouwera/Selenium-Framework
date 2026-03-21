package tests.ui;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import utils.TestDataManager;

@Epic("Test Data Validation")
@Feature("Schema Validation")
public class SchemaValidationTest {

    // ============================================================
    // Schema Validation Test
    // ============================================================
    @Story("Validate JSON test data structure")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
    @Description("Ensures that loginData.json conforms to loginData.schema.json for all environments.")
    @Step("Validate loginData.json against loginData.schema.json")
    @Test(groups = {"regression"})
    public void validateLoginSchema() {

        TestDataManager.validateJsonArrayAgainstSchema(
                "loginData.json",
                "loginData.schema.json"
        );
    }
}