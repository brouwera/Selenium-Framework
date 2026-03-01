package base;

import config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.MDC;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    // ============================================================
    // ThreadLocal WebDriver + Wait (Parallel Execution Safe)
    // ============================================================
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    // ============================================================
    // Accessors (Instance-Based for Page Object Constructors)
    // ============================================================
    public WebDriver getDriver() {
        return driver.get();
    }

    public WebDriverWait getWait() {
        return wait.get();
    }

    // ============================================================
    // Test Lifecycle (MDC Only — Logging handled by TestListener)
    // ============================================================
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {

        // Set MDC for the entire test method
        String testName = this.getClass().getSimpleName() + "." + method.getName();
        MDC.put("testName", testName);

        String browser = ConfigManager.getBrowser();
        boolean headless = ConfigManager.isHeadless();

        WebDriver webDriver = createDriver(browser, headless);
        driver.set(webDriver);

        int explicitWait = ConfigManager.getExplicitWait();
        wait.set(new WebDriverWait(webDriver, Duration.ofSeconds(explicitWait)));

        webDriver.get(ConfigManager.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method, ITestResult result) {

        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
        }

        driver.remove();
        wait.remove();

        // Clear MDC after test completes
        MDC.remove("testName");
    }

    // ============================================================
    // Driver Factory
    // ============================================================
    private WebDriver createDriver(String browser, boolean headless) {

        switch (browser.toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                boolean isCI = System.getenv("CI") != null;

                if (isCI) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--window-size=1920,1080");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                    if (headless) {
                        chromeOptions.addArguments("--headless=new");
                    }
                }

                return new ChromeDriver(chromeOptions);

            case "edge":
                System.setProperty("webdriver.edge.driver", ConfigManager.getEdgeDriverPath());
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new");
                return new EdgeDriver(edgeOptions);

            case "firefox":
                System.setProperty("webdriver.gecko.driver", ConfigManager.getGeckoDriverPath());
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                return new FirefoxDriver(firefoxOptions);

            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }
    }
}