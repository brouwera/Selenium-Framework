package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CSVUtils {

    private static final String DEFAULT_DELIMITER = ",";

    private CSVUtils() {
        // Utility class - prevent instantiation
    }

    // ============================================================
    // Public API
    // ============================================================

    public static List<Map<String, String>> readCsvAsListOfMaps(String resourcePath) {
        return readCsvAsListOfMaps(resourcePath, DEFAULT_DELIMITER);
    }

    public static List<Map<String, String>> readCsvAsListOfMaps(String resourcePath, String delimiter) {
        List<String[]> rows = readCsvRaw(resourcePath, delimiter);

        if (rows.isEmpty()) {
            return Collections.emptyList();
        }

        String[] headers = rows.get(0);
        validateHeaders(headers);

        List<Map<String, String>> result = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            validateRowLength(resourcePath, i + 1, headers, row);

            Map<String, String> rowMap = new LinkedHashMap<>();
            for (int j = 0; j < headers.length; j++) {
                rowMap.put(headers[j].trim(), row[j].trim());
            }
            result.add(rowMap);
        }

        return result;
    }

    public static Object[][] readCsvAsDataProvider(String resourcePath) {
        return readCsvAsDataProvider(resourcePath, DEFAULT_DELIMITER);
    }

    public static Object[][] readCsvAsDataProvider(String resourcePath, String delimiter) {
        List<Map<String, String>> data = readCsvAsListOfMaps(resourcePath, delimiter);

        if (data.isEmpty()) {
            throw new CsvParsingException("CSV file '" + resourcePath + "' contains no data rows.");
        }

        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }

        return result;
    }

    // ============================================================
    // Internal Helpers
    // ============================================================

    private static List<String[]> readCsvRaw(String resourcePath, String delimiter) {
        InputStream inputStream = getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new CsvParsingException("CSV resource not found on classpath: " + resourcePath);
        }

        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                line = stripBom(line).trim();

                if (line.isEmpty()) {
                    continue; // skip empty lines
                }

                String[] tokens = line.split(delimiter, -1);
                rows.add(tokens);
            }

        } catch (IOException e) {
            throw new CsvParsingException("Error reading CSV resource: " + resourcePath, e);
        }

        return rows;
    }

    private static void validateHeaders(String[] headers) {
        if (headers == null || headers.length == 0) {
            throw new CsvParsingException("CSV file has no header row or header is empty.");
        }

        for (String header : headers) {
            if (header == null || header.trim().isEmpty()) {
                throw new CsvParsingException("CSV header contains an empty or blank column name.");
            }
        }
    }

    private static void validateRowLength(String resourcePath, int lineNumber, String[] headers, String[] row) {
        if (row.length != headers.length) {
            throw new CsvParsingException(
                    String.format(
                            "Malformed CSV row in '%s' at line %d: expected %d columns but found %d.",
                            resourcePath, lineNumber, headers.length, row.length
                    )
            );
        }
    }

    private static InputStream getResourceAsStream(String resourcePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = CSVUtils.class.getClassLoader();
        }
        return classLoader.getResourceAsStream(resourcePath);
    }

    private static String stripBom(String line) {
        if (line != null && !line.isEmpty() && line.charAt(0) == '\uFEFF') {
            return line.substring(1);
        }
        return line;
    }
}