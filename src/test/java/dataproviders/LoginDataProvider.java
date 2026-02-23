package dataproviders;

import org.testng.annotations.DataProvider;
import utils.CSVUtils;

public class LoginDataProvider {

    private static final String LOGIN_DATA_PATH = "testData/loginData.csv";

    @DataProvider(name = "loginData")
    public Object[][] loginData() {

        Object[][] data = CSVUtils.readCsvAsDataProvider(LOGIN_DATA_PATH);

        if (data.length == 0) {
            throw new RuntimeException(
                    "CSV file '" + LOGIN_DATA_PATH + "' returned no rows. " +
                            "Please verify the file is not empty or incorrectly formatted."
            );
        }

        return data;
    }
}