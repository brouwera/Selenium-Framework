package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public WebDriver getDriver() {
        return driver;
    }

    // Centralized base URL for all tests
    protected String baseUrl = "https://practicetestautomation.com";

    @Parameters({"browser", "headless"})
    @BeforeMethod
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("false") String headless
    ) {

        System.out.println("=== Starting Test Setup ===");
        System.out.println("Browser: " + browser);
        System.out.println("Headless: " + headless);

        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        // Headless mode for CI/CD
        if (headless.equalsIgnoreCase("true")) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        // Browser selection (future expansion)
        switch (browser.toLowerCase()) {
            case "chrome":
            default:
                driver = new ChromeDriver(options);
                break;
        }

        // Explicit waits only — implicit waits disabled
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        // Shared explicit wait instance
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("=== Test Setup Complete ===");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("=== Starting Test Teardown ===");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("=== Test Teardown Complete ===");
    }
}