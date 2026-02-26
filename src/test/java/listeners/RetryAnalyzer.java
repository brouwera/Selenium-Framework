package listeners;

import io.qameta.allure.Allure;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetryCount = 1; // Retry failed test once

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < maxRetryCount) {
            retryCount++;

            // Mark test as flaky in Allure
            Allure.label("flaky", "true");

            // Add retry attempt info to Allure
            Allure.step("Retrying test: " + result.getName() +
                    " | Attempt " + retryCount + " of " + maxRetryCount);

            System.out.println("🔁 RETRYING TEST: " + result.getName() +
                    " | Attempt " + retryCount + " of " + maxRetryCount);

            return true; // Retry the test
        }

        return false; // No more retries
    }
}