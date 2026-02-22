package dataproviders;

import org.testng.annotations.DataProvider;
import utils.CSVUtils;

public class LoginDataProvider {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {

        // Load from classpath: src/test/resources/testData/loginData.csv
        Object[][] data = CSVUtils.readCsvAsDataProvider("testData/loginData.csv");

        if (data.length == 0) {
            throw new RuntimeException("loginData.csv returned no rows. Please verify the file is not empty.");
        }

        return data;
    }
}