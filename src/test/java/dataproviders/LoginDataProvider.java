package dataproviders;

import config.ConfigManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import utils.AiDataGenerator;
import utils.CSVUtils;

import java.util.Map;

public final class LoginDataProvider {

    private static final String LOGIN_DATA_PATH = "testData/loginData.csv";
    private static final Logger log = LoggerFactory.getLogger(LoginDataProvider.class);

    private LoginDataProvider() {}

    // ============================================================
    // EXISTING CSV PROVIDER
    // ============================================================
    @Step("Load CSV login test data from: " + LOGIN_DATA_PATH)
    @DataProvider(name = "loginDataCsv")
    public static Object[][] loginData() {

        Object[][] data = CSVUtils.readCsvAsDataProvider(LOGIN_DATA_PATH);

        for (int i = 0; i < data.length; i++) {
            Map<String, String> row = (Map<String, String>) data[i][0];
            log.info("CSV ROW {} → {}", i, row);
        }

        return data;
    }

    // ============================================================
    // AI-ONLY LOGIN DATA
    // ============================================================
    @DataProvider(name = "loginDataAi")
    public static Object[][] loginDataAi() {

        if (!ConfigManager.isAiDataEnabled()) {
            throw new IllegalStateException(
                    "AI login data requested but aiDataEnabled=false in config.json"
            );
        }

        Map<String, String> creds = AiDataGenerator.generateInvalidLogin();

        Allure.addAttachment("AI Login Credentials", creds.toString());

        return new Object[][]{
                {creds.get("username"), creds.get("password")}
        };
    }

    // ============================================================
    // HYBRID (AI → FALLBACK CSV)
    // ============================================================
    @DataProvider(name = "loginDataHybrid")
    public static Object[][] loginDataHybrid() {

        if (ConfigManager.isAiDataEnabled()) {
            try {
                Map<String, String> creds = AiDataGenerator.generateInvalidLogin();

                Allure.addAttachment("Hybrid Login Data (AI)", creds.toString());

                return new Object[][]{
                        {creds.get("username"), creds.get("password")}
                };

            } catch (Exception e) {
                log.warn("AI login data failed — falling back to CSV", e);
            }
        }

        // fallback to CSV
        return loginData();
    }
}