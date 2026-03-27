package dataproviders;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import utils.CSVUtils;

import java.util.Map;

public final class LoginDataProvider {

    private static final String LOGIN_DATA_PATH = "testData/loginData.csv";
    private static final Logger log = LoggerFactory.getLogger(LoginDataProvider.class);

    private LoginDataProvider() {}

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
}