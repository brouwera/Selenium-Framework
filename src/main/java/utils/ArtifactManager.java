package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArtifactManager {

    private static final Logger log = LoggerFactory.getLogger(ArtifactManager.class);

    private static final DateTimeFormatter RUN_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static final ThreadLocal<Path> currentTestDir = new ThreadLocal<>();

    private static Path runRootDir;
    private static String runTimestamp;

    private static LocalDateTime runStartTime;
    private static LocalDateTime runEndTime;

    private static final List<TestResultInfo> testResults = new ArrayList<>();
    private static int totalTests = 0;
    private static int passed = 0;
    private static int failed = 0;
    private static int skipped = 0;

    private static final int MAX_RUNS_TO_KEEP = 10;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    // ============================================================
    // Run-level lifecycle
    // ============================================================

    public static void initRun() {
        try {
            runStartTime = LocalDateTime.now();
            runTimestamp = runStartTime.format(RUN_FORMATTER);

            String root = ConfigManager.getArtifactRoot(); // e.g. target/artifacts
            runRootDir = Paths.get(root, runTimestamp);

            Files.createDirectories(runRootDir);
            log.info("Initialized run directory: {}", runRootDir.toAbsolutePath());

        } catch (IOException e) {
            log.error("Failed to initialize run directory", e);
            throw new RuntimeException(e);
        }
    }

    public static Path getRunRootDir() {
        return runRootDir;
    }

    // ============================================================
    // Per-test directory + metadata
    // ============================================================

    public static Path createTestDirectory(String testName) {
        try {
            Path testDir = runRootDir.resolve(testName);

            Files.createDirectories(testDir.resolve("screenshots"));
            Files.createDirectories(testDir.resolve("logs"));
            Files.createDirectories(testDir.resolve("pagesource"));
            Files.createDirectories(testDir.resolve("browserlogs"));

            Path logFile = testDir.resolve("logs").resolve("test.log");
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }

            currentTestDir.set(testDir);

            log.info("Created test artifact directory: {}", testDir.toAbsolutePath());
            return testDir;

        } catch (IOException e) {
            log.error("Failed to create artifact directory for {}", testName, e);
            throw new RuntimeException(e);
        }
    }

    public static Path getCurrentTestDir() {
        return currentTestDir.get();
    }

    public static void writeMetadata(String testName, Map<String, Object> extraFields) {
        try {
            Map<String, Object> metadata = new LinkedHashMap<>();
            metadata.put("testName", testName);
            metadata.put("browser", ConfigManager.getBrowser());
            metadata.put("environment", ConfigManager.getEnvironment());
            metadata.put("timestamp", LocalDateTime.now().format(RUN_FORMATTER));
            metadata.put("practiceBaseUrl", ConfigManager.getPracticeBaseUrl());
            metadata.put("herokuBaseUrl", ConfigManager.getHerokuBaseUrl());

            if (extraFields != null) {
                metadata.putAll(extraFields);
            }

            String json = gson.toJson(metadata);

            Path file = currentTestDir.get().resolve("metadata.json");
            Files.writeString(file, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            log.info("Created metadata.json for {}", testName);

        } catch (Exception e) {
            log.error("Failed to write metadata.json", e);
        }
    }

    // ============================================================
    // Per-test log writing + global log copy
    // ============================================================

    public static void appendToTestLog(String message) {
        try {
            Path logFile = currentTestDir.get().resolve("logs").resolve("test.log");
            Files.writeString(
                    logFile,
                    message + System.lineSeparator(),
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            log.error("Failed to write to per-test log file", e);
        }
    }

    public static void copyGlobalLog() {
        try {
            Path globalLog = Paths.get("logs", "framework.log");
            if (!Files.exists(globalLog)) {
                log.warn("Global framework.log not found, skipping copy.");
                return;
            }

            Path dest = currentTestDir.get().resolve("logs").resolve("framework.log");
            Files.copy(globalLog, dest, StandardCopyOption.REPLACE_EXISTING);

            log.info("Copied global log into test artifacts: {}", dest);

        } catch (Exception e) {
            log.error("Failed to copy global log file", e);
        }
    }

    // ============================================================
    // File writing helpers
    // ============================================================

    public static Path writeScreenshot(byte[] data, String name) {
        return writeFile("screenshots", name + ".png", data);
    }

    public static Path writePageSource(byte[] data) {
        return writeFile("pagesource", "pageSource.html", data);
    }

    public static Path writeBrowserLogs(String logs) {
        return writeFile("browserlogs", "console.log", logs.getBytes());
    }

    public static Path writeFailureMessage(String message) {
        return writeFile("logs", "failure.txt", message.getBytes());
    }

    public static Path writeFile(String subDir, String fileName, byte[] data) {
        try {
            Path dir = currentTestDir.get().resolve(subDir);
            Path file = dir.resolve(fileName);
            Files.write(file, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return file;
        } catch (IOException e) {
            log.error("Failed to write artifact file {}", fileName, e);
            return null;
        }
    }

    // ============================================================
    // Summary tracking
    // ============================================================

    public static void recordTestResult(
            String testName,
            String status,
            long durationMs,
            String failureMessage,
            long testStart,
            long testEnd
    ) {
        try {
            totalTests++;
            switch (status) {
                case "PASSED":
                    passed++;
                    break;
                case "FAILED":
                    failed++;
                    break;
                case "SKIPPED":
                    skipped++;
                    break;
                default:
                    break;
            }

            TestResultInfo info = new TestResultInfo();
            info.name = testName;
            info.status = status;
            info.durationMs = durationMs;
            info.failureMessage = failureMessage;
            info.testStart = testStart;
            info.testEnd = testEnd;

            Path base = runRootDir.resolve(testName);

            // Per-test log
            Path logFile = base.resolve("logs").resolve("test.log");
            if (Files.exists(logFile)) {
                info.logPath = runRootDir.relativize(logFile).toString();
            }

            // Screenshot: pick first file in screenshots folder if any
            Path screenshotsDir = base.resolve("screenshots");
            if (Files.exists(screenshotsDir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(screenshotsDir)) {
                    for (Path p : stream) {
                        if (Files.isRegularFile(p)) {
                            info.screenshotPath = runRootDir.relativize(p).toString();
                            break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }

            // Page source
            Path pageSource = base.resolve("pagesource").resolve("pageSource.html");
            if (Files.exists(pageSource)) {
                info.pageSourcePath = runRootDir.relativize(pageSource).toString();
            }

            // Browser logs
            Path browserLogs = base.resolve("browserlogs").resolve("console.log");
            if (Files.exists(browserLogs)) {
                info.browserLogsPath = runRootDir.relativize(browserLogs).toString();
            }

            testResults.add(info);

        } catch (Exception e) {
            log.error("Failed to record test result for {}", testName, e);
        }
    }

    public static void writeRunSummary() {
        try {
            runEndTime = LocalDateTime.now();
            long runDurationMs = Duration.between(runStartTime, runEndTime).toMillis();

            Map<String, Object> summary = new LinkedHashMap<>();
            summary.put("runTimestamp", runTimestamp);
            summary.put("runStart", runStartTime.toString());
            summary.put("runEnd", runEndTime.toString());
            summary.put("runDurationMs", runDurationMs);
            summary.put("environment", ConfigManager.getEnvironment());
            summary.put("browser", ConfigManager.getBrowser());
            summary.put("suiteName", "AutomationSuite");

            try {
                summary.put("machineName", InetAddress.getLocalHost().getHostName());
            } catch (Exception ignored) {
                summary.put("machineName", "unknown");
            }

            summary.put("os", System.getProperty("os.name"));
            summary.put("javaVersion", System.getProperty("java.version"));

            summary.put("totalTests", totalTests);
            summary.put("passed", passed);
            summary.put("failed", failed);
            summary.put("skipped", skipped);

            List<Map<String, Object>> tests = new ArrayList<>();
            for (TestResultInfo info : testResults) {
                Map<String, Object> t = new LinkedHashMap<>();
                t.put("name", info.name);
                t.put("status", info.status);
                t.put("durationMs", info.durationMs);
                t.put("failureMessage", info.failureMessage);
                t.put("testStart", info.testStart);
                t.put("testEnd", info.testEnd);
                t.put("testDurationMs", info.testEnd - info.testStart);

                Map<String, Object> artifacts = new LinkedHashMap<>();
                artifacts.put("log", info.logPath);
                artifacts.put("screenshot", info.screenshotPath);
                artifacts.put("pageSource", info.pageSourcePath);
                artifacts.put("browserLogs", info.browserLogsPath);

                t.put("artifacts", artifacts);
                tests.add(t);
            }

            summary.put("tests", tests);

            String json = gson.toJson(summary);

            Path file = runRootDir.resolve("summary.json");
            Files.writeString(file, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            log.info("Wrote run summary.json at {}", file.toAbsolutePath());

        } catch (Exception e) {
            log.error("Failed to write run summary.json", e);
        }
    }

    // ============================================================
    // Zipping + cleanup
    // ============================================================

    public static void zipRunDirectory() {
        Path zipFile = runRootDir.resolve("run.zip");
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Files.walk(runRootDir)
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> !path.equals(zipFile))
                    .forEach(path -> {
                        Path relative = runRootDir.relativize(path);
                        try (InputStream is = Files.newInputStream(path)) {
                            ZipEntry entry = new ZipEntry(relative.toString().replace("\\", "/"));
                            zos.putNextEntry(entry);
                            is.transferTo(zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            log.error("Failed to add file to zip: {}", path, e);
                        }
                    });

            log.info("Created run zip: {}", zipFile.toAbsolutePath());

        } catch (IOException e) {
            log.error("Failed to zip run directory", e);
        }
    }

    public static void cleanupOldRuns() {
        try {
            String root = ConfigManager.getArtifactRoot(); // target/artifacts
            Path rootPath = Paths.get(root);
            if (!Files.exists(rootPath)) {
                return;
            }

            List<Path> runDirs = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootPath)) {
                for (Path p : stream) {
                    if (Files.isDirectory(p)) {
                        runDirs.add(p);
                    }
                }
            }

            runDirs.sort(Comparator.comparing(Path::getFileName).reversed());

            if (runDirs.size() <= MAX_RUNS_TO_KEEP) {
                return;
            }

            List<Path> toDelete = runDirs.subList(MAX_RUNS_TO_KEEP, runDirs.size());
            for (Path dir : toDelete) {
                deleteRecursively(dir);
                log.info("Deleted old run directory: {}", dir.toAbsolutePath());
            }

        } catch (Exception e) {
            log.error("Failed to cleanup old runs", e);
        }
    }

    private static void deleteRecursively(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        log.error("Failed to delete {}", p, e);
                    }
                });
    }

    // ============================================================
    // Internal model
    // ============================================================

    private static class TestResultInfo {
        String name;
        String status;
        long durationMs;
        String failureMessage;
        long testStart;
        long testEnd;
        String logPath;
        String screenshotPath;
        String pageSourcePath;
        String browserLogsPath;
    }
}