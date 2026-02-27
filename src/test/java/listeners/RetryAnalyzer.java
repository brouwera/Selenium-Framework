package listeners;

import io.qameta.allure.Allure;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetryCount = 1; // Retry failed test once

    // ============================================================
    // Retry Logic
    // ============================================================

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < maxRetryCount) {
            retryCount++;

            Allure.label("flaky", "true");

            Allure.step(
                    "Retrying test: " + result.getName() +
                            " | Attempt " + retryCount + " of " + maxRetryCount
            );

            System.out.println(
                    "🔁 RETRYING TEST: " + result.getName() +
                            " | Attempt " + retryCount + " of " + maxRetryCount
            );

            return true;
        }

        return false;
    }
}