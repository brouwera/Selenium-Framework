package dataproviders;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import utils.TestDataManager;

import java.util.Map;

public final class JsonDataProvider {

    private static final Logger log = LoggerFactory.getLogger(JsonDataProvider.class);

    private JsonDataProvider() {}

    @Step("Load JSON login test data (environment-aware + schema validated)")
    @DataProvider(name = "loginDataJson")
    public static Object[][] loginDataJson() {

        Object[][] raw = TestDataManager.loadJsonDataProvider(
                "loginData.json",
                "loginData.schema.json"
        );

        for (int i = 0; i < raw.length; i++) {
            Map<String, String> row = (Map<String, String>) raw[i][0];
            log.info("JSON ROW {} → {}", i, row);
        }

        return raw;
    }
}