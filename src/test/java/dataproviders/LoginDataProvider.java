package dataproviders;

import org.testng.annotations.DataProvider;
import utils.CSVUtils;

public class LoginDataProvider {

    private static final String LOGIN_DATA_PATH = "testData/loginData.csv";

    // ============================================================
    // Data Provider
    // ============================================================

    @DataProvider(name = "loginData")
    public Object[][] loginData() {

        Object[][] data = CSVUtils.readCsvAsDataProvider(LOGIN_DATA_PATH);

        if (data.length == 0) {
            throw new RuntimeException(
                    "CSV file '" + LOGIN_DATA_PATH + "' returned no rows. " +
                            "Verify that the file is not empty and is correctly formatted."
            );
        }

        return data;
    }
}