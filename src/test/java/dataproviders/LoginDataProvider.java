package dataproviders;

import org.testng.annotations.DataProvider;
import utils.CSVUtils;

import java.util.List;

public class LoginDataProvider {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        String filePath = "src/test/resources/testdata/loginData.csv";
        List<String[]> records = CSVUtils.readCSV(filePath);

        Object[][] data = new Object[records.size()][3];

        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i)[0]; // username
            data[i][1] = records.get(i)[1]; // password
            data[i][2] = records.get(i)[2]; // expectedResult
        }

        return data;
    }
}
