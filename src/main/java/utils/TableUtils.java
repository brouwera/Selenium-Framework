package utils;

import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for parsing, filtering, and validating table data.
 * Designed for use with TablePage and other table-driven modules.
 */
public final class TableUtils {

    private TableUtils() {}

    // ============================================================
    // Numeric Parsing
    // ============================================================

    @Step("Parse enrollment value: {text}")
    public static int parseEnrollment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(text.replace(",", "").trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Unable to parse enrollment value: '" + text + "'", e
            );
        }
    }

    @Step("Parse list of enrollment values")
    public static List<Integer> parseEnrollmentList(List<String> values) {
        if (values == null) {
            return Collections.emptyList();
        }

        List<Integer> parsed = new ArrayList<>();
        for (String value : values) {
            parsed.add(parseEnrollment(value));
        }
        return parsed;
    }

    // ============================================================
    // Sorting Validation
    // ============================================================
    @Step("Check if numeric list is sorted ascending")
    public static boolean isSortedAscending(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return true;
        }

        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        return sorted.equals(values);
    }

    @Step("Check if string list is sorted alphabetically (A→Z)")
    public static boolean isSortedAlphabetically(List<String> values) {
        if (values == null || values.isEmpty()) {
            return true;
        }

        List<String> sorted = new ArrayList<>(values);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        return sorted.equals(values);
    }

    // ============================================================
    // Filtering Helpers
    // ============================================================
    @Step("Filter values by minimum enrollment: {min}")
    public static List<Integer> filterByMinEnrollment(List<Integer> values, int min) {
        if (values == null) {
            return Collections.emptyList();
        }

        List<Integer> filtered = new ArrayList<>();
        for (int value : values) {
            if (value >= min) {
                filtered.add(value);
            }
        }
        return filtered;
    }

    @Step("Filter strings by keyword: {keyword}")
    public static List<String> filterByKeyword(List<String> values, String keyword) {
        if (values == null || keyword == null) {
            return Collections.emptyList();
        }

        String lowerKeyword = keyword.toLowerCase();
        List<String> filtered = new ArrayList<>();

        for (String value : values) {
            if (value != null && value.toLowerCase().contains(lowerKeyword)) {
                filtered.add(value);
            }
        }
        return filtered;
    }

    // ============================================================
    // Row Comparison Helpers
    // ============================================================
    @Step("Check if all values equal expected: {expected}")
    public static boolean allEqual(List<String> values, String expected) {
        if (values == null || expected == null) {
            return false;
        }

        String expectedTrimmed = expected.trim();

        for (String value : values) {
            if (value == null || !value.trim().equalsIgnoreCase(expectedTrimmed)) {
                return false;
            }
        }
        return true;
    }

    @Step("Check if all numeric values >= {min}")
    public static boolean allGreaterOrEqual(List<Integer> values, int min) {
        if (values == null) {
            return false;
        }

        for (int value : values) {
            if (value < min) {
                return false;
            }
        }
        return true;
    }
}