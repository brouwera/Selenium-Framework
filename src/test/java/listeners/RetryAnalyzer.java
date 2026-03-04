package listeners;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    // ============================================================
    // Logger
    // ============================================================
    private static final Logger log = LoggerFactory.getLogger(RetryAnalyzer.class);

    // ============================================================
    // Retry State
    // ============================================================
    private int retryCount = 0;
    private static final int maxRetryCount = 1; // Retry failed test once

    // ============================================================
    // Retry Logic
    // ============================================================
    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < maxRetryCount) {
            retryCount++;

            // Mark test as flaky in Allure
            Allure.label("flaky", "true");

            // Log retry attempt
            String message = "Retrying test: " + result.getName() +
                    " | Attempt " + retryCount + " of " + maxRetryCount;

            Allure.step(message);
            log.warn("🔁 {}", message);

            return true;
        }

        return false;
    }
}