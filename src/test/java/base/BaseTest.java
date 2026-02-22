package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected WebDriverWait wait;

    // Start directly on the Practice page (correct for this site)
    protected String baseUrl = "https://practicetestautomation.com/practice/";

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver webDriver = new ChromeDriver(options);
        driver.set(webDriver);

        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        // Always start from a known URL
        getDriver().get(baseUrl);

        // Optional: ensure page is fully loaded
        try {
            Thread.sleep(500); // small buffer to avoid race conditions
        } catch (InterruptedException ignored) {}
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public WebDriverWait getWait() {
        return wait;
    }
}