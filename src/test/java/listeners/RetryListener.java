package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Global Retry Listener
 * Applies RetryAnalyzer to all tests unless explicitly overridden.
 * Ensures consistent retry behavior across the entire suite.
 */
public class RetryListener implements IAnnotationTransformer {

    private static final Logger log = LoggerFactory.getLogger(RetryListener.class);

    public RetryListener() {
        log.info("RetryListener initialized");
    }

    @Override
    public void transform(
            ITestAnnotation annotation,
            Class testClass,
            Constructor testConstructor,
            Method testMethod) {

        try {
            if (annotation.getRetryAnalyzerClass() == null) {
                annotation.setRetryAnalyzer(RetryAnalyzer.class);
                log.debug("Applied RetryAnalyzer to test: {}.{}",
                        testClass != null ? testClass.getSimpleName() : "UnknownClass",
                        testMethod != null ? testMethod.getName() : "UnknownMethod");
            } else {
                log.debug("Test already defines its own RetryAnalyzer: {}.{}",
                        testClass != null ? testClass.getSimpleName() : "UnknownClass",
                        testMethod != null ? testMethod.getName() : "UnknownMethod");
            }

        } catch (Exception e) {
            log.error("Failed to apply RetryAnalyzer to test {}.{}: {}",
                    testClass != null ? testClass.getSimpleName() : "UnknownClass",
                    testMethod != null ? testMethod.getName() : "UnknownMethod",
                    e.getMessage());
        }
    }
}