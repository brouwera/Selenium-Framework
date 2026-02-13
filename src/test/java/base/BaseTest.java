package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;

    // ============================
    // Test Setup
    // ============================

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practicetestautomation.com/practice-test-login/");
    }

    // ============================
    // Test Teardown
    // ============================

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit(); }
    }
}