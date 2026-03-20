package dataproviders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import utils.TestDataManager;

public final class JsonDataProvider {

    private static final Logger log = LoggerFactory.getLogger(JsonDataProvider.class);

    private static final String BASE_PATH = "testData/loginData.json";
    private static final String SCHEMA_PATH = "testData/schema/loginData.schema.json";

    private JsonDataProvider() {}

    @DataProvider(name = "loginDataJson")
    public static Object[][] loginDataJson() {

        log.debug("Loading JSON login test data with schema validation");

        Object[][] data = TestDataManager.loadJson(BASE_PATH, SCHEMA_PATH);

        log.info("Loaded {} rows of validated JSON test data", data.length);
        return data;
    }
}