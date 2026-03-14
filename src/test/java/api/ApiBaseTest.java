package api;

import org.testng.annotations.BeforeClass;

public class ApiBaseTest {

    protected ApiClient apiClient;

    @BeforeClass
    public void setUpApiClient() {
        apiClient = new ApiClient();
    }
}