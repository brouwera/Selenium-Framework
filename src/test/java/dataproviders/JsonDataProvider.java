package dataproviders;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import utils.TestDataManager;

public final class JsonDataProvider {

    // ============================================================
    // Logger
    // ============================================================
    private static final Logger log = LoggerFactory.getLogger(JsonDataProvider.class);

    // ============================================================
    // Constructor
    // ============================================================
    private JsonDataProvider() {
        // Prevent instantiation
    }

    // ============================================================
    // JSON Data Provider (Environment + Schema Validated)
    // ============================================================
    @Step("Load JSON login test data (environment-aware + schema validated)")
    @DataProvider(name = "loginDataJson")
    public static Object[][] loginDataJson() {

        log.debug("Loading JSON login test data with schema validation");

        Object[][] data = TestDataManager.loadJsonDataProvider(
                "loginData.json",
                "loginData.schema.json"
        );

        // Optional row-level debug logging
        for (int i = 0; i < data.length; i++) {
            log.info("JSON ROW {}: {}", i, data[i][0]);
        }

        log.info("Loaded {} rows of validated JSON test data", data.length);
        return data;
    }
}