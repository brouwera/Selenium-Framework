package dataproviders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import utils.CSVUtils;

public final class LoginDataProvider {

    // ============================================================
    // Constants
    // ============================================================
    private static final String LOGIN_DATA_PATH = "testData/loginData.csv";

    // ============================================================
    // Logger
    // ============================================================
    private static final Logger log = LoggerFactory.getLogger(LoginDataProvider.class);

    // ============================================================
    // Constructor
    // ============================================================
    private LoginDataProvider() {
        // Prevent instantiation
    }

    // ============================================================
    // Data Provider
    // ============================================================
    @DataProvider(name = "loginData")
    public static Object[][] loginData() {

        log.debug("Loading login test data from {}", LOGIN_DATA_PATH);

        Object[][] data = CSVUtils.readCsvAsDataProvider(LOGIN_DATA_PATH);

        if (data == null || data.length == 0) {
            throw new RuntimeException(
                    "CSV file '" + LOGIN_DATA_PATH + "' returned no data rows. " +
                            "Verify that the file exists, is not empty, and is correctly formatted."
            );
        }

        log.info("Loaded {} rows from {}", data.length, LOGIN_DATA_PATH);
        return data;
    }
}