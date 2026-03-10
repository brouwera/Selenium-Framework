package listeners;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Global Retry Analyzer
 * Retries failed tests once unless overridden.
 * Integrates with Allure and MDC for clean reporting.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(RetryAnalyzer.class);

    private static final int maxRetryCount = 1;
    private int retryCount = 0;

    private static final SimpleDateFormat TS =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < maxRetryCount) {
            retryCount++;

            String testName = buildTestName(result);
            String timestamp = TS.format(new Date());

            // Mark flaky in Allure
            Allure.label("flaky", "true");

            String message = "Retrying test: " + testName +
                    " | Attempt " + retryCount + " of " + maxRetryCount +
                    " | At " + timestamp;

            // Allure step
            Allure.step(message);

            // Log retry
            log.warn("🔁 {}", message);

            // Reset MDC step counter for clean logs
            MDC.put("step", "0");

            return true;
        }

        return false;
    }

    private String buildTestName(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();

        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            String paramString = String.join("_",
                    java.util.Arrays.stream(params)
                            .map(Object::toString)
                            .map(s -> s.replaceAll("[^a-zA-Z0-9_-]", ""))
                            .toArray(String[]::new)
            );
            return className + "." + methodName + "_" + paramString;
        }

        return className + "." + methodName;
    }
}