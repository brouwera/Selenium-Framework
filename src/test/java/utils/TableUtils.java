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

    private TableUtils() {
        // Utility class - prevent instantiation
    }

    // ============================================================
    // Numeric Parsing
    // ============================================================

    @Step("Parse enrollment value: {text}")
    public static int parseEnrollment(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(text.replace(",", "").trim());
    }

    @Step("Parse list of enrollment values")
    public static List<Integer> parseEnrollmentList(List<String> values) {
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
        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        return sorted.equals(values);
    }

    @Step("Check if string list is sorted alphabetically (A→Z)")
    public static boolean isSortedAlphabetically(List<String> values) {
        List<String> sorted = new ArrayList<>(values);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        return sorted.equals(values);
    }

    // ============================================================
    // Filtering Helpers
    // ============================================================

    @Step("Filter values by minimum enrollment: {min}")
    public static List<Integer> filterByMinEnrollment(List<Integer> values, int min) {
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
        List<String> filtered = new ArrayList<>();
        for (String value : values) {
            if (value.toLowerCase().contains(keyword.toLowerCase())) {
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
        for (String value : values) {
            if (!value.equalsIgnoreCase(expected)) {
                return false;
            }
        }
        return true;
    }

    @Step("Check if all numeric values >= {min}")
    public static boolean allGreaterOrEqual(List<Integer> values, int min) {
        for (int value : values) {
            if (value < min) {
                return false;
            }
        }
        return true;
    }
}