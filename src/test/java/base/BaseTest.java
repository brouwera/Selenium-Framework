package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Test setup: initializes WebDriver, configures browser, and opens the starting URL
    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Explicit waits only — implicit waits disabled for full control and consistency
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        // Shared explicit wait instance for all page objects
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Default starting point for all tests
        driver.get("https://practicetestautomation.com/practice-test-login/");
    }

    // Test teardown: closes the browser after each test method
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}