package api;

import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;

/**
 * Base class for all API test classes.
 * Initializes the shared ApiClient instance before test execution.
 */
@Epic("API Testing")
@Feature("API Base Setup")
public class ApiBaseTest {

    // ============================================================
    // Fields
    // ============================================================
    protected ApiClient apiClient;

    // ============================================================
    // Setup
    // ============================================================
    @Story("Initialize API client")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Adam Brouwer")
    @Description("Sets up the ApiClient instance before executing API tests.")
    @BeforeClass(alwaysRun = true)
    public void setUpApiClient() {
        apiClient = new ApiClient();
    }
}