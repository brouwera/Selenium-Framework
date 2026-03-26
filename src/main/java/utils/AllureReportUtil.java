package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class AllureReportUtil {

    private static final String ALLURE_RESULTS = "allure-results";
    private static final String ALLURE_REPORT = "allure-report";
    private static final String HISTORY = "history";

    // Absolute path to Allure CLI (Scoop installation)
    private static final String ALLURE_CLI =
            "C:\\Users\\adamb\\scoop\\apps\\allure\\current\\bin\\allure.bat";

    /**
     * Main entry point for generating the Allure report.
     */
    public static void generateReport() {
        try {
            copyHistory();
            generateAllureReport();
            openReport();
        } catch (Exception e) {
            System.err.println("Failed to generate Allure report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Copies the history folder from the previous report into the new allure-results directory.
     */
    private static void copyHistory() throws IOException {
        Path historySource = Paths.get(ALLURE_REPORT, HISTORY);
        Path historyTarget = Paths.get(ALLURE_RESULTS, HISTORY);

        if (Files.exists(historySource)) {
            System.out.println("Copying Allure history...");
            if (!Files.exists(historyTarget)) {
                Files.createDirectories(historyTarget);
            }
            copyDirectory(historySource, historyTarget);
        } else {
            System.out.println("No previous Allure history found.");
        }
    }

    /**
     * Generates the Allure report using the Allure CLI.
     */
    private static void generateAllureReport() throws IOException, InterruptedException {
        System.out.println("Generating Allure report...");

        ProcessBuilder pb = new ProcessBuilder(
                ALLURE_CLI,
                "generate",
                ALLURE_RESULTS,
                "-o",
                ALLURE_REPORT,
                "--clean"
        );

        pb.inheritIO();
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Allure report generation failed with exit code: " + exitCode);
        }

        System.out.println("Allure report generated successfully.");
    }

    /**
     * Opens the Allure report in the default browser (local runs only).
     */
    private static void openReport() throws IOException {
        if (isCiEnvironment()) {
            System.out.println("CI environment detected — skipping automatic report opening.");
            return;
        }

        File reportIndex = new File(ALLURE_REPORT + "/index.html");

        if (reportIndex.exists()) {
            System.out.println("Opening Allure report...");
            java.awt.Desktop.getDesktop().browse(reportIndex.toURI());
        } else {
            System.err.println("Allure report index.html not found.");
        }
    }

    /**
     * Utility method to recursively copy directories.
     */
    private static void copyDirectory(Path source, Path target) throws IOException {
        Files.walk(source).forEach(path -> {
            try {
                Path destination = target.resolve(source.relativize(path));
                if (Files.isDirectory(path)) {
                    if (!Files.exists(destination)) {
                        Files.createDirectories(destination);
                    }
                } else {
                    Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy history directory: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Detects if running in CI (GitHub Actions, Jenkins, etc.)
     */
    private static boolean isCiEnvironment() {
        return System.getenv("CI") != null ||
                System.getenv("GITHUB_ACTIONS") != null ||
                System.getenv("JENKINS_HOME") != null;
    }

    /**
     * Allows this class to be executed directly via:
     * java -cp "target/classes;target/test-classes" utils.AllureReportUtil
     */
    public static void main(String[] args) {
        generateReport();
    }
}